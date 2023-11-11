package springApp.Nettty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.apache.catalina.Pipeline;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NettyServerInit extends ChannelInitializer<SocketChannel> {
    @Resource
    NettyServerHandler nettyServerHandler;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(nettyServerHandler);
    }
}
