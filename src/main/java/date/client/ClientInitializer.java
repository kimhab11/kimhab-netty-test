package date.client;

import date.client.ClientHandler;
import date.coder.Decoder;
import date.coder.Encoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ClientInitializer extends ChannelInitializer<Channel> {
    @Resource
    Encoder encoder;
    @Resource
    Decoder decoder;

    @Resource
    ClientHandler clientHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
//        pipeline.addLast(encoder);
//        pipeline.addLast(decoder);
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(clientHandler);

    }
}
