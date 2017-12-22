package hello;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public @Bean
    MongoClient mongoClient() {
        MongoClient client = new MongoClient();
        // TODO: 2017/12/19 set ip port user and password
        return client;
    }
}
