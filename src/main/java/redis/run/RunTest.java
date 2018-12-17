package redis.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import redis.bean.Person;

public class RunTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RunTest.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		
		Person person = new Person();
		person.setId(1234);
		person.setName("中国");
		
		redisTemplate.opsForValue().set("person", person);
		Person person1 = (Person) redisTemplate.opsForValue().get("person");
		logger.debug("{}", person1);
	}

}
