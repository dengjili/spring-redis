package redis.zset;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

public class RedisZsetTest {

	private static final Logger logger = LoggerFactory.getLogger(RedisZsetTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/zset/zset-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		ZSetOperations opsForZSet = redisTemplate.opsForZSet();
		Set<TypedTuple<String>> set1 = new HashSet<>();
		Set<TypedTuple<String>> set2 = new HashSet<>();

		int j = 9;
		for (int i = 1; i < 10; i++) {
			j--;
			Double score1 = Double.valueOf(i);
			String value1 = "x" + i;
			Double score2 = Double.valueOf(j);
			String value2 = j % 2 == 1 ? "y" + j : "x" + j;

			TypedTuple<String> typedTuple1 = new DefaultTypedTuple<String>(value1, score1);
			TypedTuple<String> typedTuple2 = new DefaultTypedTuple<String>(value2, score2);
			set1.add(typedTuple1);
			set2.add(typedTuple2);
		}
		// 1.添加
		opsForZSet.add("zset1", set1);
		opsForZSet.add("zset2", set2);
		// 2.读取元素，但是不返回分数
		Set zset1 = opsForZSet.range("zset1", 0, -1);
		Set zset2 = opsForZSet.range("zset2", 0, -1);
		logger.debug("zset1所有值值：{}", zset1);
		logger.debug("zset1所有值值：{}", zset2);
		// 3.求元素个数
		Long zCard = opsForZSet.zCard("zset1");
		logger.debug("zset1的元素个数：{}", zCard);
		// 4.求分数在3-6之间的元素个数
		Long count = opsForZSet.count("zset1", 6, 9);
		logger.debug("zset1数在3-6之间的元素个数：{}", count);
		// 5.读取元素，并返回分数
		Set<TypedTuple<String>> rangeWithScores1 = opsForZSet.rangeWithScores("zset1", 0, -1);
		Set<TypedTuple<String>> rangeWithScores2 = opsForZSet.rangeWithScores("zset2", 0, -1);
		logger.debug("zset1所有值值：{}", rangeWithScores1);
		for (TypedTuple<String> object : rangeWithScores1) {
			Double score = object.getScore();
			String value = object.getValue();
			logger.debug("值：{}, 分数：{}", value, score);
		}
		logger.debug("zset1所有值值：{}", rangeWithScores2);
		for (TypedTuple<String> object : rangeWithScores2) {
			Double score = object.getScore();
			String value = object.getValue();
			logger.debug("值：{}, 分数：{}", value, score);
		}
		// 6.交集,分数相加
		opsForZSet.intersectAndStore("zset1", "zset2", "inter_zset");
		Set<TypedTuple<String>> inter_zset = opsForZSet.rangeWithScores("inter_zset", 0, -1);
		logger.debug("zset1所有值值：{}", inter_zset);
		for (TypedTuple<String> object : inter_zset) {
			Double score = object.getScore();
			String value = object.getValue();
			logger.debug("值：{}, 分数：{}", value, score);
		}
	}
}
