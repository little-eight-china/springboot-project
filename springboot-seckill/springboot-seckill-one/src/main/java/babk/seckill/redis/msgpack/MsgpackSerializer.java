package babk.seckill.redis.msgpack;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

/**
 * 基于msgpack的序列化支持。 使用msgpack可以大大的减少内存占用，并提高解析速度。
 */
public class MsgpackSerializer<T extends Object> implements RedisSerializer<T> {

	private static final Logger logger = LoggerFactory.getLogger(MsgpackSerializer.class);

	private Class<? extends Object> cls = null;

	public MsgpackSerializer(Class<? extends Object> type) {
		this.cls = type;
	}

	@Override
	public byte[] serialize(T msg) throws SerializationException {
		if (msg == null) {
			return new byte[0];
		}
		MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
		try {
			packer.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return packer.toByteArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] data) throws SerializationException {
		Object msg = null;
		if (data == null || data.length == 0)
			return null;
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);
		if (unpacker != null) {
			try {
				msg = cls.newInstance();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					unpacker.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return (T) msg;
	}

}
