package bdbk;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class DubboClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboClientApplication.class, args);
	}
}
