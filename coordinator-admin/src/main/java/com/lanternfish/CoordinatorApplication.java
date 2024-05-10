package com.lanternfish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 启动程序
 *
 * @author Liam
 */

@SpringBootApplication
public class CoordinatorApplication {

    public static void main(String[] args) {

        System.setProperty("spring.devtools.restart.enabled", "false");

        SpringApplication application = new SpringApplication(CoordinatorApplication.class);

        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  coordinator启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
