package redis.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisStringTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisStringTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		ValueOperations opsForValue = redisTemplate.opsForValue();
		
		// 1.设置值
		opsForValue.set("name", "张三");
		// 2.获取值
		String name = (String) opsForValue.get("name");
		logger.debug("从redis中获取name值：{}", name);
		// 3.判断redis是否含有这个属性
		Boolean hasKey = redisTemplate.hasKey("name");
		logger.debug("从redis中是否有name属性：{}", hasKey);
		// 4.删除redis这个属性
		redisTemplate.delete("name");
		hasKey = redisTemplate.hasKey("name");
		logger.debug("从redis中是否有name属性：{}", hasKey);
		// 5.获取长度
		opsForValue.set("name", "abcdefg");
		Long length = opsForValue.size("name");
		logger.debug("name属性值长度：{}", length);
		// 6.设置新值，返回旧值
		name = (String) opsForValue.getAndSet("name", "hijklmn");
		logger.debug("从redis中获取name值：{}", name);
		name = (String) opsForValue.get("name");
		logger.debug("从redis中获取name值：{}", name);
		// 7.求子串
		String subName = (String) opsForValue.get("name", 1, 4);
		logger.debug("从redis中获取name子串值：{}", subName);
		// 8.追加
		opsForValue.append("name", "opqrst");
		name = (String) opsForValue.get("name");
		logger.debug("从redis中获取name值：{}", name);
	}

}
