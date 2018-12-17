package redis.set;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

public class RedisSetTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisSetTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/set/set-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		SetOperations opsForSet = redisTemplate.opsForSet();
		// 可重复测试
		redisTemplate.delete("set1");
		redisTemplate.delete("set2");
		// 1.添加元素
		opsForSet.add("set1", "v1", "v2", "v3", "v4", "v5", "v6");
		opsForSet.add("set2", "v0", "v2", "v4", "v6", "v8");
		// 2.求长度
		Long size = opsForSet.size("set1");
		logger.debug("set1长度：{}", size);
		// 3.差集
		Set difference = opsForSet.difference("set1", "set2");
		logger.debug("set1差集：{}", difference);
		// 4.交集
		Set intersect = opsForSet.intersect("set1", "set2");
		logger.debug("set1与set2交集：{}", intersect);
		// 5.判断集合是否含有某个元素
		Boolean isMember = opsForSet.isMember("set1", "v2");
		logger.debug("set1是否含有v2元素：{}", isMember);
		// 6.获取所有set元素
		Set members = opsForSet.members("set1");
		logger.debug("set元素：{}", members);
		// 7.从set中随机弹出一个元素
	    String pop = (String) opsForSet.pop("set1");
	    logger.debug("pop的值：{}", pop);
	    members = opsForSet.members("set1");
		logger.debug("set元素：{}", members);
		// 8.从set中随机获取一个元素
		String randomMember = (String) opsForSet.randomMember("set1");
		logger.debug("randomMember的值：{}", randomMember);
		// 9.从set中随机获取多个元素
		List randomMembers = opsForSet.randomMembers("set1", 2L);
		logger.debug("randomMembers的值：{}", randomMembers);
		// 10.删除元素
		opsForSet.remove("set1", "v1");
		// 11.并集
		Set union = opsForSet.union("set1", "set2");
		logger.debug("set1与set2并集：{}", union);
		// 12.储存
		opsForSet.unionAndStore("set1", "set2", "set_union");
	}

}
