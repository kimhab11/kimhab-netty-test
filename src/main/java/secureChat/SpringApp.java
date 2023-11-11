package secureChat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChatServer.main(args);
        ChatClient.main(args);
    }
}
