package springApp.Nettty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class NettyServer implements CommandLineRunner {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    NettyServerInit nettyServerInit;

    @Override
    public void run(String... args) throws Exception {
        start();
    }

    @SneakyThrows
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(nettyServerInit)
                .option(ChannelOption.SO_BACKLOG, 1111)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
//                    .bind()
                .bind( "127.0.0.1",808)
                .sync();
        log.info("Server start...");
    }

    @PreDestroy
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("Server stop...");
    }
}
