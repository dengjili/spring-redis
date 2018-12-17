package redis.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import redis.bean.Person;

public class RunTestOneConnection {

	private static final Logger logger = LoggerFactory.getLogger(RunTestOneConnection.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-redis.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);

		Person person = new Person();
		person.setId(12345);
		person.setName("中国2");

		SessionCallback<Person> callback = new SessionCallback<Person>() {
			@Override
			public Person execute(RedisOperations operations) throws DataAccessException {
				operations.boundValueOps("person").set(person);
				return (Person) operations.boundValueOps("person").get();
			}
		};

		Person person1 = (Person) redisTemplate.execute(callback);
		logger.debug("{}", person1);
	}

}
