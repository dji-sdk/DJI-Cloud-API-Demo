package com.dji.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.dji.sample.*.dao")
@SpringBootApplication
@EnableScheduling
//@EnableConfigurationProperties(OssConfiguration.class)
public class CloudApiSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudApiSampleApplication.class, args);
	}

}
