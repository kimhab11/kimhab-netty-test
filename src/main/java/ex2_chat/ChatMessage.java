package ex2_chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatMessage {
    private final String message;

    public ChatMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}

class ChatMessageEncoder extends MessageToByteEncoder<ChatMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ChatMessage msg, ByteBuf out) {
        byte[] messageBytes = msg.getMessage().getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length;

        out.writeInt(messageLength);
        out.writeBytes(messageBytes);
    }
}

class ChatMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int messageLength = in.readInt();

        if (in.readableBytes() < messageLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] messageBytes = new byte[messageLength];
        in.readBytes(messageBytes);

        String message = new String(messageBytes, StandardCharsets.UTF_8);
        out.add(new ChatMessage(message));
    }
}

