package date.server;

import date.coder.Decoder;
import date.coder.Encoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ServerInitializer extends ChannelInitializer<SocketChannel>{
    @Resource
    Encoder encoder;
    @Resource
    Decoder decoder;

    @Resource
    ServerHandler serverHandler;

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
//        channelPipeline.addLast(encoder);
//        channelPipeline.addLast(decoder);
        channelPipeline.addLast(new StringDecoder());
        channelPipeline.addLast(new StringEncoder());
        channelPipeline.addLast(serverHandler);
    }
}
