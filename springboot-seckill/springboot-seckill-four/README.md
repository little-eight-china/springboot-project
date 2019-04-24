# springboot秒杀系统———第四章 加入消息件


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

### 整合rabbitmq
* 基础安装知识
> https://little8.top/2018/08/09/springboot%E6%95%B4%E5%90%88RabbitMQ%EF%BC%88windows%E7%8E%AF%E5%A2%83%EF%BC%89/
 
* 居然要手动添加消息名称。。。
