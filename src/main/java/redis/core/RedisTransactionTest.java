package redis.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class RedisTransactionTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisTransactionTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		SessionCallback callback = operations -> {
			// 开启事务
			operations.multi();
			
			operations.boundValueOps("test").set("abc");
			String value = (String) operations.boundValueOps("test").get();
			logger.debug("事务还未提交，获取test值：{}", value);
			
			// 提交事务
			List exec = operations.exec();
			
			value = (String) operations.boundValueOps("test").get();
			return value;
		};
		Object execute = redisTemplate.execute(callback);
		logger.debug("事务提交后，获取test值：{}", execute);
	}

}
