package redis.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

public class PipelineTest {
	private static final Logger logger = LoggerFactory.getLogger(PipelineTest.class);

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig();
		 
		//最大空闲连接数, 默认8个
		config.setMaxIdle(50);
		 
		//最大连接数, 默认8个
		config.setMaxTotal(100);
		 
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		config.setMaxWaitMillis(20000);
		 
		JedisPool pool = new JedisPool(config, "localhost");
		
		Jedis jedis = pool.getResource();
		long start = System.currentTimeMillis();
		// 开启流水线
		Pipeline pipeline = jedis.pipelined();
		for (int i = 1; i <= 50000; i++) {
			pipeline.set("pipeline_key_" + i, "pipeline_value_" + i);
			pipeline.get("pipeline_key_" + i);
		}
		
//		pipeline.sync();  //执行同步，但是不返回结果
//		pipeline.syncAndReturnAll(); //执行同步，返回结果
		
		List<Object> syncAndReturnAll = pipeline.syncAndReturnAll();
		
		long end = System.currentTimeMillis();
		
		logger.debug("耗时：{}, 操作读写次数：{}", (end - start), syncAndReturnAll.size());
	}
}
 