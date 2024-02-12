package date;

import date.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("date")
public class Controller {
    @Resource
    Client client;

    @PostMapping("/send")
    public void send() {
        String date = new Date().toString();
        log.info("=>date send: {}", date);
        client.send(date);
    }
}
