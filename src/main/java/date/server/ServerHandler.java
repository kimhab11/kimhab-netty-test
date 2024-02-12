package date.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String date) {
        log.info("=>server received from client: "+date);
        ctx.writeAndFlush("=>server send back to client: thank you");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("=>Channel Active, connect success from client: {}", getRemoteAddress(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("Channel Inactive, client disconnect {}", getRemoteAddress(ctx));
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info("Channel Read Complete");

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Server Connection Exception {}",getRemoteAddress(ctx), cause);
        ctx.close();
    }

    public String getRemoteAddress(ChannelHandlerContext context){
        return context.channel().remoteAddress().toString();
    }
}
