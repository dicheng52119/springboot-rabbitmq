# springboot+rabbitmq 整合示例项目
### 演示说明
1. hello包下的send和receiver是生产者、消费者一对一、一对多、多对多关系的简单演示，不具有实际意义
2. user包下的send和receiver是关于实体类信息的简单演示，不具有实际意义
3. topic包下的send和receiver和启动类中exchange和queue的定义及绑定演示了topic类型的生产消费关系，仅是简单演示
4. fanout包下的send和receiver和启动类中exchange和queue的定义及绑定演示了topic类型的生产消费关系，仅是简单演示
5. callback包下的call演示了具有回调信息的生产者配置，仅是简单演示

### 开发实际应用说明
1. application.yml中配置了rabbitmq的相关配置
2. config包下配置rabbitmq的配置和交换机exchange配置
3. topic包下receive configuration配置了消费者配置的相关信息
4. direct包下receive configuration配置了消费者配置的相关信息
5. sender包下的util下配置了生产者配置的相关信息
6. controller包下的类统一对配置进行测试

### 参考文献
1. 演示说明：https://www.cnblogs.com/boshen-hzb/p/6841982.html
2. 开发实际应用说明：https://segmentfault.com/a/1190000011797667
