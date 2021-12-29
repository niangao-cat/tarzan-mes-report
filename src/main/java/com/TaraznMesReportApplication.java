package com;

import java.util.TimeZone;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;

@Order(value = 1)
//@EnableHasor()
//@EnableHasorWeb(order = 10, at = WorkAt.Interceptor)
@ComponentScan({"com.ruike","tarzan"})
@EnableChoerodonResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"com.ruike"})
public class TaraznMesReportApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(TaraznMesReportApplication.class, args);

    }
}


