package ex2_chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {
    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ChatMessageDecoder());
                            pipeline.addLast(new ChatMessageEncoder());
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("Connected to chat server at " + host + ":" + port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            Channel channel = future.channel();
            while (true) {
                String message = reader.readLine();
                if (message == null || message.equalsIgnoreCase("bye")) {
                    break;
                }
                ChatMessage chatMessage = new ChatMessage(message);
                channel.writeAndFlush(chatMessage);
            }

            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class ChatClientHandler extends SimpleChannelInboundHandler<ChatMessage> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) {
            System.out.println(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            System.err.println("Exception caught on client");
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("localhost", 8080);
        client.run();
    }
}
