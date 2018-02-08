# springboot+rabbitmq 整合示例项目
### 实现方式一(application.yml 简易配置)
1. receiver/sender包下的hello包下的send和receiver是生产者、消费者一对一、一对多、多对多关系的简单演示，不具有实际意义
2. receiver/sender包下的user包下的send和receiver是关于实体类信息的简单演示
3. receiver/sender包下的topic包下的sender和receiver和启动类中exchange和queue的定义及绑定演示了topic类型的生产消费关系
4. receiver/sender包下的fanout包下的sender和receiver和启动类中exchange和queue的定义及绑定演示了topic类型的生产消费关系
5. callback包下的call演示了具有回调信息的生产者配置，仅是简单演示
6. 启动类中定义了需要演示的队列、交换器以及队列和交换器之间的绑定

### 实现方式二(代码配置)
1. application.yml中配置了rabbitmq的相关配置
2. config包下配置rabbitmq的配置以及队列和交换器之间的绑定配置
3. receiver包下的topic包下configuration配置了消息监听的相关信息
4. receiver包下的direct包下configuration配置了消费监听的相关信息
5. sender包下的util下配置了生产者配置的相关信息

### 说明
1. controller包下的类统一对配置进行测试
2. model包下定义了演示示例中用到的实体类
3. 实现方式二相对实现方式一可以定制一些功能，实际开发中，可以结合两种方式综合使用

### 参考文献
1. 演示说明：https://www.cnblogs.com/boshen-hzb/p/6841982.html
2. 开发实际应用说明：https://segmentfault.com/a/1190000011797667