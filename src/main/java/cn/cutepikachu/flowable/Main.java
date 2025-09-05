package cn.cutepikachu.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/7/23 13:45:03
 */
@EnableTransactionManagement
@SpringBootApplication(proxyBeanMethods = false)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
