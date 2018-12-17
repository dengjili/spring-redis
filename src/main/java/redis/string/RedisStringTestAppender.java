package redis.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisStringTestAppender {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisStringTestAppender.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		ValueOperations opsForValue = redisTemplate.opsForValue();
		
		// 1.设置值
		opsForValue.set("i", "3");
		// 2.获取值
		String name = (String) opsForValue.get("i");
		logger.debug("从redis中获取i值：{}", name);
		// 3.+1设置
		opsForValue.increment("i", 1);
		name = (String) opsForValue.get("i");
		logger.debug("从redis中获取i值：{}", name);
		// 4.-1设置
		opsForValue.increment("i", -1);
		name = (String) opsForValue.get("i");
		logger.debug("从redis中获取i值：{}", name);
		// 5.-1设置
		redisTemplate.getConnectionFactory().getConnection().decr(redisTemplate.getKeySerializer().serialize("i"));
		name = (String) opsForValue.get("i");
		logger.debug("从redis中获取i值：{}", name);
		// 6.-2设置
		redisTemplate.getConnectionFactory().getConnection().decrBy(redisTemplate.getKeySerializer().serialize("i"), 2);
		name = (String) opsForValue.get("i");
		logger.debug("从redis中获取i值：{}", name);
	}

}
