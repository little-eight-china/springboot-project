package bdbk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// 开启Spring Cloud的服务注册与发现
@EnableDiscoveryClient
@SpringBootApplication
public class NacosServerApplication {

	public static void main(String[] args) {
SpringApplication.run(NacosServerApplication.class, args);
	}

	// 开放服务接口
	@RestController
	static class ServerController {
		@GetMapping("/")
		public String hello() {
			return "hello clien";
		}
	}
}

