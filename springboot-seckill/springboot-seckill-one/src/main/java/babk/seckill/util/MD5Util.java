package babk.seckill.util;
import java.security.MessageDigest;
public class MD5Util {

	/**
	 * md5加密（32位小写）
	 */
	public static String getMD5(String str) {
		String reMd5;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (byte aB : b) {
				i = aB;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			reMd5 = buf.toString();

		} catch (Exception e) {
			throw new RuntimeException("MD5加密出现错误", e);
		}
		return reMd5;
	}

}
