package babk.seckill.redis.msgpack;

import org.springframework.data.redis.serializer.SerializationException;


public class MsgpackKeySerializer<T extends MsgpackData> extends MsgpackSerializer<T> {

	public MsgpackKeySerializer(Class<? extends MsgpackData> type) {
		super(type);
	}

	@Override
	public byte[] serialize(T msg) throws SerializationException {
		return super.serialize(msg);
	}

	@Override
	public T deserialize(byte[] data) throws SerializationException {
   		return super.deserialize(data);
	}

}
