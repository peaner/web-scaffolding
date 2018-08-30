package web.scaffolding.config.database;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lilongzhou
 * @Description: 数据源配置 mybatis
 * @Date: Created in 下午5:07 2018/8/29
 */
@Configuration
//@MapperScan("持久层接口命名空间")
@MapperScan("web.scaffolding.dao.mapper")
public class MybatisConfig {
}
