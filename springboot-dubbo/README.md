# SpringBoot整合Dubbo（入门篇）
## 遇到的坑

> * zookeeper版本不一致问题
> * 虚拟器防火墙没关


## 如何搭建
### Linux上安装zookeeper
因为最近在学习Linux，所以这个东西直接安装在本机的虚拟机上。（你也可以直接在windows上安装和启用zookeeper，道理是一样的，这里不多描述了。）

* linux命令直接下载
```
wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.8/zookeeper-3.4.8.tar.gz
```
> 这里有个坑，就是版本必须与程序里的依赖对应的版本一致，否则会出bug，而且不容易发现。

* 解压下载的tar,然后进入zookeeper根目录，新建data和logs俩个目录
* 用pwd复制目录名字，后面有用
* 从根目录进入conf目录下，把zoo_sample.cfg命名为zoo.cfg，删除zoo_sample.cfg
* 编辑zoo.cfg，把里面的dataDir=...删除，加上下面2行代码
```
dataDir=/粘贴你自己zookeeper的目录/data
dataLogDir=/粘贴你自己zookeeper的目录/logs
```

* 返回zookeeper根目录，进入bin，然后运行以下代码

```
./zkServer.sh start
```
> 输出Starting zookeeper ... STARTED,代表启动成功。你也可以用
./zkServer.sh status 查看，输出Mode: standalone 代表启动中

* 关闭zookeeper是./zkServer.sh stop，暂时用不上
* 记得要把防火墙关了
```
systemctl stop firewalld.service

这个你选择性用。关闭开机自启动：systemctl disable firewalld.service
```

### 搭建springboot项目
这里会分服务端跟消费端
#### 先新建主项目，作版本控制 springboot-dubbo
* 我用的是idea，File -> new-> Project ,选择maven，第一页不用勾选任何东西，点next，填好项目的一些信息，一直next下去即可。
* 新建完成后，把不要的文件夹删除（.idea和src），然后在pom.xml里加入必要的依赖
```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>bdbk</groupId>
  <artifactId>springboot-dubbo</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>war</packaging>

  <name>springboot-dubbo Maven Webapp</name>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.1.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>

  <dependencies>
    <!--web必要-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--test必要-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <!--引入dubbo的依赖-->
    <dependency>
      <groupId>com.alibaba.spring.boot</groupId>
      <artifactId>dubbo-spring-boot-starter</artifactId>
      <version>2.0.0</version>
    </dependency>
    <!-- 引入zookeeper的依赖，就是这里引用的zookeeper版本必须对应服务器上的 -->
    <dependency>
      <groupId>com.101tec</groupId>
      <artifactId>zkclient</artifactId>
      <version>0.9</version>
    </dependency>
  </dependencies>
</project>

```

#### 然后新建服务端 dubbo-server
* 直接在 springboot-dubbo 项目上右键 选择new -> Module
* 不用勾选next，然后输入 dubbo-server
* 新建后，在java目录下创建你自己的包（这里是bdbk），然后创建启动类 DubboServerApplication
```java

package bdbk;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class DubboServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboServerApplication.class, args);
	}
}

```

* 同包下（或者你再自己创建一个 service 包，因为是入门篇东西比较简单就不再区分结构了）创建接口类 HelloService
```java
package bdbk;

/**
 * 接口类
 * @author little_eight
 * @since 2018/12/3
 */
public interface HelloService {
    public  String sayHello(String name);
}
```

* 创建实现类 HelloServiceImpl

```java
package bdbk;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * 接口实现类
 * @author little_eight
 * @since 2018/12/3
 */
@Component
@Service
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```

* 在resources目录下新建 application.yml 配置文件
```
# 配置端口
server:
    port: 8081

# dubbo配置
spring:
    dubbo:
        application:
            # 应用名称
            name: dubbo-server
            # 注册中心地址 这里要填你自己zookeeper的地址
            registry: zookeeper://192.168.211.128:2181
        protocol:
            # 协议端口
            port: 20880
```

#### 再创建消费端项目 dubbo-client ,新建跟 dubbo-server 一样
* 因为要引用服务端，所以先在pom.xml里添加对应的依赖
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>bdbk</groupId>
	<artifactId>dubbo-client</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>dubbo-client</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>bdbk</groupId>
		<artifactId>springboot-dubbo</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>
	<!-- 添加服务端 -->
	<dependencies>
		<dependency>
			<groupId>bdbk</groupId>
			<artifactId>dubbo-server</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	</dependencies>
</project>
```
* 新建包bdbk,然后创建启动类 DubboClientApplication
```java
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

```

* 再写个控制类 HelloController，作为测试
```java
package bdbk;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制类 用于测试
 * @author little_eight
 * @since 2018/12/3
 */
@RestController
public class HelloController {
    @Reference
    private HelloService helloService;

    @RequestMapping("/")
    public String hello() {
        String hello = helloService.sayHello("World");
        return hello;
    }
}
```

* 在resources目录下新建application.yml配置文件
```
# 配置端口
server:
    port: 8082

# dubbo配置
spring:
    dubbo:
        application:
            # 应用名称
            name: dubbo-client
            # 注册中心地址 这里要填你自己zookeeper的地址
            registry: zookeeper://192.168.211.128:2181
        protocol:
            # 协议端口
            port: 20881
```

#### 最后先启动服务端，再启动消费端。

[点击访问](http://localhost:8082),页面输出Hello World即为成功。
