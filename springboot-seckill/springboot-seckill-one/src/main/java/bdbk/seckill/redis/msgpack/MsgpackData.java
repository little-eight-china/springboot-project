package babk.seckill.redis.msgpack;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.Serializable;

/**
 * 所有需要序列化的类都要实现此接口。
 * 在此接口内手工打包和解包。
 * 实现此接口后，修改字段需非常谨慎，pack和unpack一定要结对，如果用于缓存，更新后必须清除缓存。
 */
public interface MsgpackData extends Serializable{


	/**
	 * 打包。
	 */
	public abstract void pack(MessagePacker packer) throws Exception;

	/**
	 * 解包。
	 */
	public abstract void unpack(MessageUnpacker unpacker) throws Exception;
}