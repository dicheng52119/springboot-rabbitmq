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


### 可靠性消息发送(几种保障机制)
1. 持久化设置
    * Message
    生产者在生产消息的时候，将消息的投递模式（MessageDeliveryMode）设置为2（持久）
    * Exchange
    发送到持久化的交换器（配置了durable=true）
    * Queue
    到达持久化的队列（配置了durable=true）
    > 注意：
    > 1. 必须Exchange持久化了，才能实现Queue持久化，必须Queue持久化了，才能实现Message持久化
    > 2. Exchange和Queue在定义时进行持久化设置，而调用发送消息的convertAndSend方法时默认发的持久化的信息
2. 消息发送方(确认消息发送到Rabbitmq服务器上)
    方式一：通过AMQP事务机制实现，这也是从AMQP协议层面提供的解决方案；
    
    方式二：通过将channel设置成confirm模式来实现
    
    * 使用事务
    1. RabbitMQ中与事务机制有关的方法有三个：txSelect(), txCommit()以及txRollback(), txSelect用于将当前channel设置成transaction模式，txCommit用于提交事务，txRollback用于回滚事务，在通过txSelect开启事务之后，我们便可以发布消息给broker代理服务器了，如果txCommit提交成功了，则消息一定到达了broker了，如果在txCommit执行之前broker异常崩溃或者由于其他原因抛出异常，这个时候我们便可以捕获异常通过txRollback回滚事务了。
    2. 事务确实能够解决producer与broker之间消息确认的问题，只有消息成功被broker接受，事务提交才能成功，否则我们便可以在捕获异常进行事务回滚操作同时进行消息重发，但是使用事务机制的话会降低RabbitMQ的性能
    * confirm模式
    1. 生产者将信道设置成confirm模式，一旦信道进入confirm模式，所有在该信道上面发布的消息都将会被指派一个唯一的ID(从1开始)，一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者(包含消息的唯一ID)，这就使得生产者知道消息已经正确到达目的队列了，如果消息和队列是可持久化的，那么确认消息会在将消息写入磁盘之后发出，broker回传给生产者的确认消息中delivery-tag域包含了确认消息的序列号，此外broker也可以设置basic.ack的multiple域，表示到这个序列号之前的所有消息都已经得到了处理；
    2. confirm模式最大的好处在于他是异步的，一旦发布一条消息，生产者应用程序就可以在等信道返回确认的同时继续发送下一条消息，当消息最终得到确认之后，生产者应用便可以通过回调方法来处理该确认消息，如果RabbitMQ因为自身内部错误导致消息丢失，就会发送一条nack消息，生产者应用程序同样可以在回调方法中处理该nack消息；
    > 注意：这是生产者和rabbit服务器之间的互动，没有消费者的联系
3. 消息接收方(手动确认消息已经正确处理完毕)
    springboot注解方式监听队列，无法手动指定回调，所以采用了实现ChannelAwareMessageListener接口。
    * 监听的方法内部必须使用channel进行消息确认，包括消费成功或消费失败
    * 如果不手动确认，也不抛出异常，消息不会自动重新推送（包括其他消费者），因为对于rabbitmq来说始终没有接收到消息消费是否成功的确认，并且Channel是在消费端有缓存的，没有断开连接
    * 如果rabbitmq断开，连接后会自动重新推送（不管是网络问题还是宕机）
    * 如果消费端应用重启，消息会自动重新推送
    * 如果消费端处理消息的时候宕机，消息会自动推给其他的消费者
    * 如果监听消息的方法抛出异常，消息会按照listener.retry的配置进行重发，但是重发次数完了之后还抛出异常的话，消息不会重发（也不会重发到其他消费者），只有应用重启后会重新推送。因为retry是消费端内部处理的，包括异常也是内部处理，对于rabbitmq是不知道的（此场景解决方案后面有）
    * spring.rabbitmq.listener.retry配置的重发是在消费端应用内处理的，不是rabbitqq重发
    * rabbitmq服务器收到消费方的确认消费信息后，会将对应的持久化的信息从队列中进行删除
        
### 参考文献
1. 实现方式一：https://www.cnblogs.com/boshen-hzb/p/6841982.html
2. 实现方式二：https://segmentfault.com/a/1190000011797667