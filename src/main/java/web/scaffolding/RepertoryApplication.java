package web.scaffolding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

/**
 * @Author: lilongzhou
 * @Description:
 * @Date: Created in 18:36 2018/7/31
 */
@SpringBootApplication
public class RepertoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepertoryApplication.class, args);
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
