package redis.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class RedisManagerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisManagerTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		Object execute = redisTemplate.execute((RedisOperations operations) -> {
			operations.boundValueOps("key1").set("value1");
			String keyValue1 = (String) operations.boundValueOps("key1").get();
			Long expire = operations.getExpire("key1");
			logger.debug("key1的失效时间为：{}", expire);
			Boolean b = operations.expire("key1", 20L, TimeUnit.SECONDS);
			logger.debug("{}", b);
			b = operations.persist("key1");
			logger.debug("{}", b);
			expire = operations.getExpire("key1");
			logger.debug("key1的失效时间为：{}", expire);
			
			long now = System.currentTimeMillis();
			Date date = new Date();
			date.setTime(now + 12000);
			operations.expireAt("key1", date);
			expire = operations.getExpire("key1");
			logger.debug("key1的失效时间为：{}", expire);
			return null;
		});
		logger.debug("事务提交后，获取test值：{}", execute);
	}

}
