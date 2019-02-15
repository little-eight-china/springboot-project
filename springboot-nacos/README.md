# 使用Nacos实现服务注册与发现

## 什么是 Nacos

官网解释：
Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

Nacos 帮助您更敏捷和容易地构建、交付和管理微服务平台。 Nacos 是构建以“服务”为中心的现代应用架构 (例如微服务范式、云原生范式) 的服务基础设施。

个人解释：
微服务管理平台

## Nacos优缺点

### 优点
* 开箱即用，适用于dubbo，spring cloud
* AP模型，数据最终一致性
* 注册中心，配置中心二合一，提供控制台管理
* 纯国产，久经双十一考验

### 缺点
* 刚刚开源不久，社区热度不够，依然存在bug
> 优缺点4比1，肯定选它呀，

## 快速开始

### 下载Nacos

[下载地址(本文版本：0.8.0)](ttps://github.com/alibaba/nacos/releases)

下载完成之后，解压。根据不同平台，执行不同命令。本人windows，直接点击bin包下的startup.cmd

启动完成之后，访问：http://127.0.0.1:8848/nacos/，默认账号密码均为nacos，进入Nacos的服务管理页面，如下图
![图片1](http://m.qpic.cn/psb?/V10I7pAv28bqXz/FRliIMZ6Ky1H.7VQ7sO0eA57sEryF7oUAD5xjnHQoto!/b/dFIBAAAAAAAA&bo=VwevAQAAAAARF94!&rf=viewer_4)
### 创建服务提供和消费者

#### 项目架构

```
└─springboot-nacos
    └─nacos-server
        └─pom.xml
    └─nacos-client
        └─pom.xml
    └─pom.xml
```
* 创建一个名为springboot-nacos的springboot项目，然后项目里只留pom.xml文件，其余全部删除。pom.xml的依赖如下

```
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
				<version>0.2.1.RELEASE</version>
			</dependency>
		</dependencies>
	</dependencyManagement>
```
> 注意，版本兼容性官方说明：版本 0.2.x.RELEASE 对应的是 Spring Boot 2.0.x 版本，版本 0.1.x.RELEASE 对应的是 Spring Boot 1.x 版本，所以要看清楚自己的springboot是什么版本，如果是2.1.x,会有未知的bug(比如服务注册不上)，本人使用2.0.6

* 在父项目中new一个module，名为nacos-server作为服务提供者，由于依赖可全继承父层，所以pom.xml可删除其它，只留 parent
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<parent>
		<groupId>bdbk</groupId>
		<artifactId>springboot-nacos</artifactId>
		<version>1.0.0</version>
	</parent>
	<modelVersion>4.0.0</modelVersion>
	<artifactId>nacos-server</artifactId>
	<version>1.0.0</version>
</project>
```

* 配置好application.yml

```
server:
  port: 8080
spring:
  application:
    name: nacos-server #服务名
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848 #nacos地址
```

* 在启动类里写内部接口服务提供类
```
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
```

* 启动，如果控制台日志看到如下说明注册成功 即可在nacos的管理界面看到我们的服务注册了上去，你可以点击详情查看服务的具体信息。此时还可以改下端口开2个服务，后面做负载均衡的测试
![图片2](http://m.qpic.cn/psb?/V10I7pAv28bqXz/Qx45qPIDBJt2GzSQ3DWb*mQIWIUtJrqksyULq.a9KQ4!/b/dDUBAAAAAAAA&bo=YwcfAgAAAAARF1k!&rf=viewer_4)
```
o.s.c.a.n.registry.NacosServiceRegistry  : nacos registry, nacos-client 192.168.96.122:8090 register finished
```

* 同理创建nacos-client作为服务消费者，配置好application.yml
```
server:
  port: 8090
spring:
  application:
    name: nacos-client
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848

```

* 在启动类里写内部接口服务消费类

```
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
```

* 启动，注册成功后，访问localhost:8090,可以看到结果显示在2个端口不断切换，说明不同请求真正实际调用的服务提供者实例是不同的，也就是说，通过LoadBalancerClient接口在获取服务实例的时候，已经实现了对服务提供方实例的负载均衡。

### 参考资料

[Nacos官方文档](https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html)

### 代码示例

[springboot-nacos](https://github.com/little-eight-china/springboot-project/tree/master/springboot-nacos)




