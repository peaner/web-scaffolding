package web.scaffolding.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lilongzhou
 * @Description:
 * @Date: Created in 下午5:12 2018/8/29
 */
@Configuration
@ConfigurationProperties(prefix = "hikari.datasource")
public class HikariCPDataSourceConfig extends HikariConfig {

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        return new HikariDataSource(this);
    }

}
