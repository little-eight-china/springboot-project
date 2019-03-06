package babk.seckill.redis;

import babk.seckill.redis.msgpack.MsgpackSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;

/**
 * Redis配置。
 */
@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Cookie> hotelPrice(RedisConnectionFactory factory) {
		RedisTemplate<String, Cookie> template = new RedisTemplate<String, Cookie>();
		template.setKeySerializer(new MsgpackSerializer<String>(String.class));
		template.setValueSerializer(new MsgpackSerializer<Cookie>(Cookie.class));
		template.setConnectionFactory(factory);
		template.afterPropertiesSet();
		return template;
	}

}
