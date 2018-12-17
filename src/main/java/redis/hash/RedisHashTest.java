package redis.hash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisHashTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisHashTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/hash/hash-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		HashOperations opsForHash = redisTemplate.opsForHash();
		
		// 1.设置值
		Map map = new HashMap();
		map.put("f1", "test");
		map.put("f2", "张三");
		opsForHash.putAll("hash", map);
		opsForHash.put("hash", "f3", "12");
		// 2.读取值
		String value = (String) opsForHash.get("hash", "f3");
		logger.debug("从hash中获取f3值：{}", value);
		// 3.判断关键字
		Boolean hasKey = opsForHash.hasKey("hash", "f3");
		logger.debug("hash中是否包含f3值：{}", hasKey);
		// 4.获取所有值
		Map entries = opsForHash.entries("hash");
		logger.debug("hash所有值：{}", entries);
		// 5.增加1
		// 配置类型为String，导致报错，暂不解决
		/*opsForHash.increment("hash", "f3", 1);
		value = (String) opsForHash.get("hash", "f3");
		logger.debug("从hash中获取f3值：{}", value);*/
	    // hkeys命令
		Set keys = opsForHash.keys("hash");
		// hvals命令
		List values = opsForHash.values("hash");
		// hmget命令
		opsForHash.multiGet("hash", Arrays.asList("f", "f2"));
		// hsetnx命令
		opsForHash.putIfAbsent("hash", "f4", "12");
		// hdel命令
		opsForHash.delete("hash", "f4");
	}

}
