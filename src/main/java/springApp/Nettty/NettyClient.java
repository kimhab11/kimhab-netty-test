package springApp.Nettty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyClient implements CommandLineRunner {
    @Resource
    NettyClientInit nettyClientInit;

    ChannelFuture channelFuture;

    Bootstrap bootstrap;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();

    @Override
    public void run(String... args) throws Exception {
        start();
    }

    @SneakyThrows
    public void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(nettyClientInit);
        channelFuture = bootstrap.connect("127.0.0.1",808).sync().addListener((ChannelFuture f) -> {
            final EventLoop eventLoop = f.channel().eventLoop();
            if (!f.isSuccess()){
                log.info("Client Restart...");
                eventLoop.schedule(this::start, 3L, TimeUnit.MILLISECONDS);
            }
        });
        channelFuture.channel().closeFuture().sync();
        log.info("Client start...");
    }

    @PreDestroy
    public void stop() {
        bossGroup.shutdownGracefully();
        log.info("Client stop...");
    }
}
