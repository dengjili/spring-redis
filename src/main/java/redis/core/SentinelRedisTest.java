package redis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class SentinelRedisTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SentinelRedisTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/sentinel-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		
		Object execute = redisTemplate.execute((RedisOperations ros) -> {
			ros.boundValueOps("key1").set("abc");
			String value = (String) ros.boundValueOps("key1").get();
			return value;
		});
		
		logger.debug("返回值：{}", execute);
	}

}
