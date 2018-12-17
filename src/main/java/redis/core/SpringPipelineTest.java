package redis.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class SpringPipelineTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringPipelineTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		
		long start = System.currentTimeMillis();
		SessionCallback callback = operations -> {
			// 开启流水线
			for (int i = 1; i <= 50000; i++) {
				operations.boundValueOps("pipeline_key_" + i).set("pipeline_value_" + i * 2);
				operations.boundValueOps("pipeline_key_" + i).get();
			}
			
			return null;
		};
		List syncAndReturnAll = redisTemplate.executePipelined(callback);
		long end = System.currentTimeMillis();
		logger.debug("耗时：{}, 操作读写次数：{}", (end - start), syncAndReturnAll.size());
	}

}
