package redis.lua;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import redis.bean.Person;

public class RedisObjectTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisObjectTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("redis-conf/string/string-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		
		DefaultRedisScript<Person> redisScript = new DefaultRedisScript<>();
		// 设置脚本
		redisScript.setScriptText("redis.call('set', KEYS[1], ARGV[1]) return redis.call('get', KEYS[1])");
		
		List<String> keyList = new ArrayList<>();
		keyList.add("role1");
		
		Person person = new Person();
		person.setId(1);
		person.setName("和钟娜公司1");
		
		String sha1 = redisScript.getSha1();
		logger.debug("sha值：{}", sha1);
		
		// 设置返回类型，不然拿不到值
		redisScript.setResultType(Person.class);
		
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		
		Person person2 = (Person) redisTemplate.execute(redisScript, jdkSerializationRedisSerializer, jdkSerializationRedisSerializer, keyList, person);
		logger.debug("person2值：{}", person2.getName());
	}

}
