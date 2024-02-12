package date.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
@Component
public class Server {
    private final int port = 2222;
    private final String ipHost = "0.0.0.0";

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private final EventLoopGroup boss = new NioEventLoopGroup(); // boss thread
    private final EventLoopGroup worker = new NioEventLoopGroup(); // worker thread
    @Resource
    ServerInitializer serverInitializer;

    @PostConstruct
    public void run1() {
        start();
    }

    private void start() {
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer)
                    // queue size
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(ipHost,port)
                    .sync();
            log.info("=>Server start success , port: {}", port);
        } catch (InterruptedException e) {
            log.info("Server start failed");
            Thread.currentThread().interrupt();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreDestroy
    public void stop(){
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        log.info("Server stop running");
    }
}
