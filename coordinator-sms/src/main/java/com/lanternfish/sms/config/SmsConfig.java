package com.lanternfish.sms.config;

import com.lanternfish.sms.config.properties.SmsProperties;
import com.lanternfish.sms.core.AliyunSmsTemplate;
import com.lanternfish.sms.core.SmsTemplate;
import com.lanternfish.sms.core.TencentSmsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信配置类
 *
 * @author Liam
 * @version 4.2.0
 */
@Configuration
public class SmsConfig {

    @Configuration
    @ConditionalOnProperty(value = "sms.enabled", havingValue = "true")
    @ConditionalOnClass(com.aliyun.dysmsapi20170525.Client.class)
    static class AliyunSmsConfig {

        @Bean
        public SmsTemplate aliyunSmsTemplate(SmsProperties smsProperties) {
            return new AliyunSmsTemplate(smsProperties);
        }

    }

    @Configuration
    @ConditionalOnProperty(value = "sms.enabled", havingValue = "true")
    @ConditionalOnClass(com.tencentcloudapi.sms.v20190711.SmsClient.class)
    static class TencentSmsConfig {

        @Bean
        public SmsTemplate tencentSmsTemplate(SmsProperties smsProperties) {
            return new TencentSmsTemplate(smsProperties);
        }

    }

}
