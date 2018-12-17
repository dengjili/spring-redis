package redis.list;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisListCommands.Position;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * 非阻塞命令，同时也是线程不安全的
 * 
 * @author it
 */
public class RedisListTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisListTest.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/linkedlist/list-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		ListOperations opsForList = redisTemplate.opsForList();
		
		// left表示最前面的元素，right表示最末端的元素
		
		// 1.删除,重复测试
		redisTemplate.delete("list");
		// 2.放入单个元素
		opsForList.leftPush("list", "node3");
		// 3.放入多个元素
		List list = new ArrayList();
		list.add("node2");
		list.add("node1");
		opsForList.leftPushAll("list", list);
		// 4.末端插入元素
		opsForList.rightPush("list", "node4");
		// 5.获取下标为0的节点
		String indexStr0 = (String) opsForList.index("list", 0);
		logger.debug("获取list下标为0的节点值：{}", indexStr0);
		// 6.list长度
		Long size = opsForList.size("list");
		logger.debug("list长度值：{}", size);
		// 7.从左边弹出一个节点
		String element = (String) opsForList.leftPop("list");
		logger.debug("元素element值：{}", element);
		// 8.从右边弹出一个节点
		element = (String) opsForList.rightPop("list");
		logger.debug("元素element值：{}", element);
		size = opsForList.size("list");
		logger.debug("list长度值：{}", size);
		// 9.在node2前插入一个节点
		redisTemplate.getConnectionFactory().getConnection().lInsert("list".getBytes("utf-8"), Position.BEFORE, "node2".getBytes("utf-8"), "node0".getBytes("utf-8"));
		// 10.在node2后插入一个节点
		redisTemplate.getConnectionFactory().getConnection().lInsert("list".getBytes("utf-8"), Position.AFTER, "node2".getBytes("utf-8"), "node2.5".getBytes("utf-8"));
		
		// 11.如果list存在，左端插入一个节点
		opsForList.leftPushIfPresent("list", "head");
		// 12.如果list存在，右端插入一个节点
		opsForList.rightPushIfPresent("list", "end");
		// 13.截取list元素
		List range = opsForList.range("list", 1, -1);
		logger.debug("range值[没有head元素，head下标为0]：{}", range);
		// 13.删除所有end元素  count = 0 : 移除表中所有与 VALUE 相等的值
		opsForList.remove("list", 0, "end");
		// 14.输出所有list值
		range = opsForList.range("list", 0, -1);
		logger.debug("所有list值：{}", range);
	}

}
