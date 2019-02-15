package bdbk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// 开启Spring Cloud的服务注册与发现
@EnableDiscoveryClient
@SpringBootApplication
public class NacosClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosClientApplication.class, args);
	}

	// 调用服务接口
	@RestController
	static class ClientController {
		@Autowired
		private LoadBalancerClient loadBalancerClient;
		@GetMapping("/")
		public String test() {
			// 负载均衡接口选取服务提供节点实现接口调用
			ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-server");
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(serviceInstance.getUri().toString(), String.class);
			return "端口号为 : " + serviceInstance.getPort() + ", 返回结果 : " + result;
		}
	}
}

