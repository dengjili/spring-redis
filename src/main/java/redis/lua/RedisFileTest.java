package redis.lua;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

public class RedisFileTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisFileTest.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		
		// 组件流
		InputStream inputStream = RedisFileTest.class.getClassLoader().getResourceAsStream("compare.lua");
		ByteArrayOutputStream baos = new ByteArrayOutputStream(inputStream.available());
		// 包装流
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		BufferedOutputStream bos = new BufferedOutputStream(baos);
		
		byte[] buffer = new byte[1024]; 
		int len = 0;
		while ((len = bis.read(buffer)) > 0) {
			bos.write(buffer, 0, len);
			// 如果不调用该方法，数据都保持在缓存区,最终baos为空数据
			bos.flush();
		}
		// 打印文件内容
		byte[] byteArray = baos.toByteArray();
		logger.debug("{}", new String(byteArray));
		
		Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
		byte[] sha1 = jedis.scriptLoad(byteArray);
		
		logger.debug("sha1值：{}", new String(sha1));
		
		// Object rtn = jedis.evalsha(sha1, 2, "key1".getBytes(), "key2".getBytes(), "2".getBytes(), "4".getBytes());
		Object rtn = jedis.evalsha(new String(sha1), 2, "key1", "key2", "5", "4");
		logger.debug("返回值：{}", rtn);
	}

}
