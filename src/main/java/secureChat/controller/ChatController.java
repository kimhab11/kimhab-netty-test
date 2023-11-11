package secureChat.controller;


import secureChat.ChatServerHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    ChatServerHandler chatClientHandler;
    @PostMapping("chat")
    public void chatTo(@RequestBody String chat, ChannelHandlerContext ctx) throws Exception {
        chatClientHandler.channelRead0(ctx,chat);
    }
}
