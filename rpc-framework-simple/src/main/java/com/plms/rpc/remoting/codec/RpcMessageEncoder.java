package com.plms.rpc.remoting.codec;

import com.plms.rpc.constant.RpcConstants;
import com.plms.rpc.enums.CompressTypeEnum;
import com.plms.rpc.extension.ExtensionLoader;
import com.plms.rpc.remoting.dto.RpcMessage;
import com.plms.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import com.plms.rpc.compress.Compress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author bigboss
 * @Date 2021/10/27 10:34
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) throws Exception {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            out.writerIndex(out.writerIndex() + 4);
            out.writeByte(rpcMessage.getMessageType());
            out.writeByte(rpcMessage.getCodec());
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            int fullLength = RpcConstants.HEAD_LENGTH;
            byte[] body = null;
            if (rpcMessage.getMessageType() != RpcConstants.HEARTBEAT_REQUEST_TYPE
                && rpcMessage.getMessageType() != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension("kyro");
                body = serializer.serializer(rpcMessage.getData());
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
                body = compress.compress(body);
                fullLength += body.length;
            }
            if (body != null) {
                out.writeBytes(body);
            }
            int writerIndex = out.writerIndex();
            out.writerIndex(writerIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writerIndex);
        } catch (Exception e) {
            log.error("encoding failed!", e);
        }
    }
}
