1.什么是MQ？
	消息总线（Message Queue），后文称MQ，是一种跨进程的通信机制，用于上下游传递消息。

2.MQ和调用的区别？
	MQ是互联网分层架构中的解耦利器。
	MQ的优点是：
	1）解耦 -> 将消息写入消息队列，需要消息的系统自己从消息队列中订阅，从而系统A不需要做任何修改。
	2）异步 -> 将消息写入消息队列，非必要的业务逻辑以异步的方式运行，加快响应速度
	3）削峰 -> 系统A慢慢的按照数据库能处理的并发量，从消息队列中慢慢拉取消息。在生产中，这个短暂的高峰期积压是允许的。
	MQ的不足是：
	1）系统更复杂，多了一个MQ组件
	2）消息传递路径更长，延时会增加
	3）消息可靠性和重复性互为矛盾，消息不丢不重难以同时保证
	4）上游无法知道下游的执行结果，这一点是很致命的
	
	无论如何，记住这个结论：调用方实时依赖执行结果的业务场景，请使用调用，而不是MQ。

3.什么时候使用MQ？
	例：【典型场景一：数据驱动的任务依赖】
		什么是任务依赖，举个栗子，互联网公司经常在凌晨进行一些数据统计任务，这些任务之间有一定的依赖关系，比如：
		1）task3需要使用task2的输出作为输入
		2）task2需要使用task1的输出作为输入
		这样的话，tast1, task2, task3之间就有任务依赖关系，必须task1先执行，再task2执行，载task3执行。
	结论：执行任务有先后顺序的时候，需要用到MQ。

4.MQ的类型：
	1. ActiveMQ/ApolloMQ
　　优点：老牌的消息队列，使用Java语言编写。对JMS支持最好，采用多线程并发，资源消耗比较大。如果你的主语言是Java，可以重点考虑。
　　缺点：由于历史悠久，历史包袱较多，版本更新很缓慢。集群模式需要依赖Zookeeper实现。最新架构的产品被命名为Apollo，号称下一代ActiveMQ，目前案例较少。
 
	2. RocketMQ/Kafka
　　优点：专为海量消息传递打造，主张使用拉模式，天然的集群、HA、负载均衡支持。话说还是那句话，适合不适合看你有没有那么大的量。
　　缺点：所谓鱼和熊掌不可兼得，放弃了一些消息中间件的灵活性，使用的场景较窄，需关注你的业务模式是否契合，否则山寨变相使用很别扭。
　　除此之外，RocketMQ没有.NET下的客户端可用。RocketMQ身出名门，但使用者不多，生态较小，毕竟消息量能达到这种体量的公司不多，你也可以直接去购买阿里云的消息服务。
　　Kafka生态完善，其代码是用Scala语言写成，可靠性比RocketMQ低一些。
 
　	3. RabbitMQ
　　优点：生态丰富，使用者众，有很多人在前面踩坑。AMQP协议的领导实现，支持多种场景。淘宝的MySQL集群内部有使用它进行通讯，OpenStack开源云平台的通信组件，最先在金融行业得到运用。
　　缺点：Erlang代码你Hold得住不? 虽然Erlang是天然集群化的，但RabbitMQ在高可用方面做起来还不是特别得心应手，别相信广告。

5.ActiveMQ
	主要有这么几个中间件：
		5.1 Broker
			消息服务器，作为server提供消息核心服务
		5.2 Producer
			消息生产者，业务的发起方，负责生产消息传输给broker
		5.3 Consumer
			消息消费者，业务的处理方，负责从broker获取消息并进行业务逻辑处理
		5.4 Topic
			主题，发布订阅模式下的消息统一汇集地，不同生产者向topic发送消息，由MQ服务器分发到不同的订阅者，实现消息的广播
		5.5 Queue
			队列，PTP模式下，特定生产者向特定queue发送消息，消费者订阅特定的queue完成指定消息的接收
		5.6 Message
			消息体，根据不同通信协议定义的固定格式进行编码的数据包，来封装业务数据，实现消息的传输

6.与Springboot结合的单服务的运用
	1）配置文件
		server:
		  port: 8080

		spring:
		  activemq:
			broker-url: tcp://127.0.0.1:61616
			user: admin
			password: admin
			close-timeout: 15s   # 在考虑结束之前等待的时间
			in-memory: true      # 默认代理URL是否应该在内存中。如果指定了显式代理，则忽略此值。
			non-blocking-redelivery: false  # 是否在回滚回滚消息之前停止消息传递。这意味着当启用此命令时，消息顺序不会被保留。
			send-timeout: 0     # 等待消息发送响应的时间。设置为0等待永远。

		#  packages:
		#    trust-all: true #不配置此项，会报错
		  pool:
			enabled: true
			max-connections: 10   #连接池最大连接数
			idle-timeout: 30000   #空闲的连接过期时间，默认为30秒

		  #jms:
			#pub-sub-domain: true  #默认情况下activemq提供的是queue模式，若要使用topic模式需要配置下面配置（上面的JMS也要接触注释）

		# 是否信任所有包
		#spring.activemq.packages.trust-all=
		# 要信任的特定包的逗号分隔列表（当不信任所有包时）
		#spring.activemq.packages.trusted=
		# 当连接请求和池满时是否阻塞。设置false会抛“JMSException异常”。
		#spring.activemq.pool.block-if-full=true
		# 如果池仍然满，则在抛出异常前阻塞时间。
		#spring.activemq.pool.block-if-full-timeout=-1ms
		# 是否在启动时创建连接。可以在启动时用于加热池。
		#spring.activemq.pool.create-connection-on-startup=true
		# 是否用Pooledconnectionfactory代替普通的ConnectionFactory。
		#spring.activemq.pool.enabled=false
		# 连接过期超时。
		#spring.activemq.pool.expiry-timeout=0ms
		# 连接空闲超时
		#spring.activemq.pool.idle-timeout=30s
		# 连接池最大连接数
		#spring.activemq.pool.max-connections=1
		# 每个连接的有效会话的最大数目。
		#spring.activemq.pool.maximum-active-session-per-connection=500
		# 当有"JMSException"时尝试重新连接
		#spring.activemq.pool.reconnect-on-exception=true
		# 在空闲连接清除线程之间运行的时间。当为负数时，没有空闲连接驱逐线程运行。
		#spring.activemq.pool.time-between-expiration-check=-1ms
		# 是否只使用一个MessageProducer
		#spring.activemq.pool.use-anonymous-producers=true
	2）生产者-producer
		A.先将producer的类加入bean池（@compenent）
		A-B.创建一个Queue或者Topic的Destination，参数为Destination的名称，则下面convertAndSend需要用到的参数。 
			例:	Destination destination = new ActiveMQQueue("test-queue"); \\//  Destination destination = new ActiveMQTopic("test-queue");
		B.注入JmsMessageTemplate类，调用JmsMessageTemplate的convertAndSend()方法，需要注意的是，convertAndSend的参数除了destination之外，还需要一个payload(荷载)
		C.如果需要其他功能，例如延时定时功能，则需要在producer类中，引入JmsTemplate类，调用send方法。
			例：jmsTemplate.send(destination,session ->{
					TextMessage textMessage = session.createTextMessage(message);
					// 设置延时时间【关键】
					textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5000);// 延时5秒
					return textMessage;
				});
			其中，message是荷载信息，ScheduledMessage的选项有以下这么几个，可以混合使用。
				AMQ_SCHEDULED_DELAY 消息延迟时间单位：毫秒 LONG
				AMQ_SCHEDULED_PERIOD 消息发送周期单位时间：毫秒 LONG
				AMQ_SCHEDULED_REPEAT 消息重复发送次数 INT
				AMQ_SCHEDULED_CRON 使用Cron 表达式 设置定时发送 String
				四个选项可以混合使用，但是CRON执行顺序大于DELAY。
				例：1）重复发送3次，每次之间间隔2秒
					textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);// 重复3次，共4次
					// 设置周期时间，注意这里是 setLongProperty
					textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD,2000);
					2）每个小时的第21分钟执行
					textMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON,"25 * * * *");
					cron的*格式： // 分钟 小时 日期 月份 星期  每个小时的第21分钟执行
	3）消费者 - consumer
		A.先将producer的类加入bean池（@compenent）
		B.加入@JmsListener（监听的queue名或者topic名）
		C.监听到了producer中创建对应的queue名或者topic名后，则开始其他的业务逻辑。
	4）混合使用，使得queue可以共用的情况。
		/**
		 * 可以让Queue和Topic共用，只需要在JmsListener注解后面加上对应的（containerFactory）模式即可
		 * 例：@JmsListener(destination="ztest-queue",containerFactory = "jmsListenerContainerQueue")
		 */
		@Configuration
		@EnableJms
		public class JmsConfiguration {
			// topic模式的ListenerContainer
			@Bean
			public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
				DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
				bean.setPubSubDomain(true);
				bean.setConnectionFactory(activeMQConnectionFactory);
				return bean;
			}
			// queue模式的ListenerContainer
			@Bean
			public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory activeMQConnectionFactory) {
				DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
				bean.setConnectionFactory(activeMQConnectionFactory);
				return bean;
			}
		}
	5)经典例子在Ztest中，得到一个需要，需要得到第二步的返回结果，但是第二步的返回结果必须要等第一步的返回结果才可以进行。
		1）先建立一个queue：建立一个controller层，创建第一个destination对象，然后producer调用jmsMessageTemplate/jmsTemplate 去发送队列或者话题。
		2）第一步的消费者监听到了之后，做好自己的事情，再创建第二个destination对象，在调用producer去发送队列话题。
		3）第二步的消费者监听到了之后，再进行相关服务。