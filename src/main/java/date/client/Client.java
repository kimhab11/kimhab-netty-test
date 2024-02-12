package date.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Client implements CommandLineRunner {
    private final EventLoopGroup boss = new NioEventLoopGroup();
    Bootstrap bootstrap;

    ChannelFuture channelFuture;
    @Resource
    ClientInitializer clientInitializer;

//    public static void main(String[] args) throws Exception {
//        Client client = new Client();
//        client.run1();
//    }

    public void send(String date) {
        channelFuture.channel().writeAndFlush(date);
    }
//
//    @PostConstruct
//    public void run1() throws Exception {
//        start();
//    }

    @SneakyThrows
    public void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(boss)
                .channel(NioSocketChannel.class)
                .handler(clientInitializer)
                .option(ChannelOption.TCP_NODELAY, true);
        channelFuture = bootstrap.connect("0.0.0.0",2222)
                .sync()
                .addListener((ChannelFuture futureListener)-> {
                    final EventLoopGroup eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()){
                        log.info("Client Restart");
                        eventLoop.schedule(this::start, 3L, TimeUnit.SECONDS);
                    }
                });
        log.info("=>Client Start Successful");
    }

    @PreDestroy
    public void stop(){
        boss.shutdownGracefully();
        log.info("Client Stop");
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
