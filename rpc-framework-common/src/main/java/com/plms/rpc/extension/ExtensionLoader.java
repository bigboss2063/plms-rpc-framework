package com.plms.rpc.extension;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 参考dubbo的SPI实现 Service Provider Interface  ExtensionLoader 可以理解为接口实现类加载器
 *
 * @Author bigboss
 * @Date 2021/10/29 21:51
 */
@Slf4j
public final class ExtensionLoader<T> {
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Holder<Object>> CACHED_INSTANCES = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> CACHED_CLASSES = new Holder<>();
    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");
    private static String CACHED_DEFAULT_NAME = null;
    private final Class<?> type;
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 获取实现类加载器
     *
     * @param type 接口类型
     * @return 实现类加载器
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("extension type can not be null!");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("extension type have to be interface!");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("extension type have to be annotated by @SPI!");
        }
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type); // 去缓存里找有没有对应实现类加载器
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * 根据实现类名称获取实现类实例
     *
     * @param name 实现类名称
     * @return 实现类
     */
    public T getExtension(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("extension name can not be null");
        }
        if ("default".equals(name)) {
            return getDefaultExtension();
        }
        Holder<Object> holder = CACHED_INSTANCES.get(name); // 去缓存里找有没有实现类名称对应的持有目标对象
        if (holder == null) {
            CACHED_INSTANCES.putIfAbsent(name, new Holder<Object>());
            holder = CACHED_INSTANCES.get(name);
        }
        Object instance = holder.get(); // 获取实现类
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    private T getDefaultExtension() {
        getExtensionClasses();
        if (StrUtil.hasBlank(CACHED_DEFAULT_NAME) || "default".equals(CACHED_DEFAULT_NAME)) {
            return null;
        }
        return getExtension(CACHED_DEFAULT_NAME);
    }

    private T createExtension(String name) {
        Class<?> extensionClass = getExtensionClasses().get(name);
        if (extensionClass == null) {
            throw new RuntimeException("No such extension of name " + name);
        }
        T instance = (T) EXTENSION_INSTANCES.get(extensionClass);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(extensionClass, extensionClass.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(extensionClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = CACHED_CLASSES.get();
        if (classes == null) {
            synchronized (CACHED_CLASSES) {
                classes = CACHED_CLASSES.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    CACHED_CLASSES.set(classes);
                }
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if (defaultAnnotation != null) {
            String value = defaultAnnotation.value();
            if ((value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                if (names.length > 1) {
                    throw new IllegalArgumentException("more than 1 default extension name on extension" +
                            type.getName() + ": " + Arrays.toString(names));
                }
                if (names.length == 1) {
                    CACHED_DEFAULT_NAME = names[0];
                }
            }
        }
        HashMap<String, Class<?>> extensionClasses = new HashMap<>();
        loadDirectory(extensionClasses);
        return extensionClasses;
    }

    private void loadDirectory(HashMap<String, Class<?>> extensionClasses) {
        String fileName = SERVICE_DIRECTORY + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadResource(HashMap<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final int ci = line.indexOf('#');
                if (ci >= 0) {
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        final int ei = line.indexOf('=');
                        String name = line.substring(0, ei).trim();
                        String extensionName = line.substring(ei + 1).trim();
                        if (name.length() > 0 && extensionName.length() > 0) {
                            Class<?> loadClass = classLoader.loadClass(extensionName);
                            extensionClasses.put(name, loadClass);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
