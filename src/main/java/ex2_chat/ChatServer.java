package ex2_chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private final List<Channel> channels = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new ChatServer().run();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ChatMessageDecoder());
                            pipeline.addLast(new ChatMessageEncoder());
                            pipeline.addLast(new ChatServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(8080).sync();
            System.out.println("Chat Server started on port 8080.");

            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChatServerHandler extends SimpleChannelInboundHandler<ChatMessage> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            Channel channel = ctx.channel();
            channels.add(channel);
            System.out.println("New client connected: " + channel.remoteAddress());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            Channel channel = ctx.channel();
            channels.remove(channel);
            System.out.println("Client disconnected: " + channel.remoteAddress());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) {
            Channel sender = ctx.channel();
            System.out.println("Received message from " + sender.remoteAddress() + ": " + msg.getMessage());

            // Broadcast the message to all connected clients
            for (Channel channel : channels) {
                if (channel != sender) {
                    channel.writeAndFlush(msg);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            Channel channel = ctx.channel();
            System.err.println("Exception caught on " + channel.remoteAddress());
            cause.printStackTrace();
            ctx.close();
        }
    }
}
