package redis.lua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

public class RedisStringTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisStringTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
		String rtn = (String) jedis.eval("return 'hello lua redis'");
		logger.debug("redis返回值：{}", rtn);
		
		// 带参数的
		jedis.eval("redis.call('set', KEYS[1], ARGV[1])", 1, "test-key", "test-value");
		rtn = jedis.get("test-key");
		logger.debug("带参数的redis返回值：{}", rtn);
		
		// 缓存脚本
		String sha1 = jedis.scriptLoad("redis.call('set', KEYS[1], ARGV[1])");
		logger.debug("缓存脚本返回sha值：{}", sha1);
		
		jedis.evalsha(sha1, 1, "sha-key", "sha-value");
		rtn = jedis.get("sha-key");
		logger.debug("缓存脚本的redis返回值：{}", rtn);

		jedis.close();
	}

}
