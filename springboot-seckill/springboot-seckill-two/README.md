# springboot秒杀系统———第一章 初建模型


## 包架构

```
└─ sql 建表sql
└─src
    └─bdbk
        └─seckill
            ├─config 配置类
            ├─constant 常量类
            ├─controller 控制类
            ├─dao 数据库类
            ├─domain 实体类
            ├─service 逻辑类
            ├─util 工具类
            ├─validator 校验类
            ├─vo 前端vo
```

## 实现部分

### 商品

* 商品详情
* 秒杀功能

### 压测（jmeter）
* 基本配置，压测地址是goods/list

[!jmeter1](https://github.com/little-eight-china/image/blob/master/springboot-seckill/jmeter1.png?raw=true)

* 当线程数为1000时，mysql就会报错，打开MYSQL安装目录打开MY.INI找到max_connections（在大约第93行）默认是100 一般设置到500～1000比较合适，重启mysql,这样1040错误就解决啦。
                       max_connections=1000
                       
[!test1](https://github.com/little-eight-china/image/blob/master/springboot-seckill/test1.png?raw=true)

* 1000并发时，还能有400多TPS,当设置到10000，由于机器本身的性能问题，导致只有200多TPS

[!jmeter2](https://github.com/little-eight-china/image/blob/master/springboot-seckill/jmeter2.png?raw=true)
    

