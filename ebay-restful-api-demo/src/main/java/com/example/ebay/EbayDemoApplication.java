package com.example.ebay;

import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.example.ebay.exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class EbayDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbayDemoApplication.class, args);
    }

    /**
     * 项目启动执行
     *
     * @return
     */
    @Bean
    CommandLineRunner init() {
        return (args) -> {
            try {
                CredentialUtil.load(this.getClass().getResourceAsStream("/ebay-config.yaml"));
                log.info(">>>>> 加载eBay配置文件完成 <<<<<");
            } catch (Exception e) {
                log.error(BasicException.stackTraceToString(e));
            }
        };
    }

}
