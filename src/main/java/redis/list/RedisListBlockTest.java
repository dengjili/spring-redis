package redis.list;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * 阻塞命令，同时也是线程安全的,一定程度能保证数据而性能不佳
 * 
 * @author it
 */
public class RedisListBlockTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisListBlockTest.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/linkedlist/list-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		ListOperations opsForList = redisTemplate.opsForList();
		
		// left表示最前面的元素，right表示最末端的元素
		
		// 1.删除,重复测试
		redisTemplate.delete("list1");
		redisTemplate.delete("list2");
		
		List<String> nodeList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			nodeList.add("node" + i);
		}
		opsForList.leftPushAll("list1", nodeList);
		
		String leftPop = (String) opsForList.leftPop("list1", 1, TimeUnit.SECONDS);
		String rightPop = (String) opsForList.rightPop("list1", 1, TimeUnit.SECONDS);
		
		logger.debug("leftPop值：{}", leftPop);
		logger.debug("rightPop值：{}", rightPop);
		
		nodeList.clear();
		for (int i = 1; i <= 3; i++) {
			nodeList.add("data" + i);
		}
		
		opsForList.leftPushAll("list2", nodeList);
		logger.debug("list1值：{}", opsForList.range("list1", 0, -1));
		logger.debug("list2值：{}", opsForList.range("list2", 0, -1));
		
		// 相当于rpoplpush
		opsForList.rightPopAndLeftPush("list1", "list2");
		// 相当于brpoplpush
		opsForList.rightPopAndLeftPush("list1", "list2", 1, TimeUnit.SECONDS);
		
		logger.debug("list1值：{}", opsForList.range("list1", 0, -1));
		logger.debug("list2值：{}", opsForList.range("list2", 0, -1));
	}

}
