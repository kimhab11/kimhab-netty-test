package date.coder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
@ChannelHandler.Sharable
public class Encoder extends MessageToMessageEncoder<Date> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Date date, List<Object> list) throws Exception {
        log.info("decode data");
     //   date = new Date()
    }
}
