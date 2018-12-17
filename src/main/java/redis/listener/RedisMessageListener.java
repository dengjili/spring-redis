package redis.listener;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

	RedisTemplate redisTemplate;
	
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		// 获取消息
		byte[] body = message.getBody();
		String receiveMsg = (String) redisTemplate.getValueSerializer().deserialize(body);
		logger.debug("body:{}", receiveMsg);
		// channel
		byte[] channel = message.getChannel();
		String channelMsg = (String) redisTemplate.getStringSerializer().deserialize(channel);
		logger.debug("channel:{}", channelMsg);
		// 渠道pattern
		String string = new String(pattern, Charset.defaultCharset());
		logger.debug("渠道pattern:{}", string);
	}

}
