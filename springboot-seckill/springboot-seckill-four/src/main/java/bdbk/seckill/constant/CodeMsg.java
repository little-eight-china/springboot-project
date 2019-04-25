package bdbk.seckill.constant;

public enum  CodeMsg {
	//通用的错误码
	SUCCESS(0,"请求成功"),
	WARN(500001,"请求异常警告"),
	ERROR(500002,"请求失败"),
	SERVER_ERROR(500100,"服务端异常"),
	BIND_ERROR(500100,"参数校验异常：%s"),
	VERIFYCODE_ERROR(500101,"验证码错误，请重新输入！"),
	REQUEST_ILLEGAL(500102,"请求非法"),
	ACCESS_LIMIT_REACHED(500104,"访问太频繁！"),
	//登录模块 5002XX
	SESSION_ERROR(500210,"Session不存在或者已经失效"),
	PASSWORD_EMPTY(500211,"登录密码不能为空"),
	MOBILE_EMPTY(500212,"手机号不能为空"),
	MOBILE_ERROR(500213,"手机号不能为空"),
	MOBILE_NOT_EXIST(500214, "手机号不存在"),
	PASSWORD_ERROR(500215, "密码错误"),
	//秒杀模块 5005XX
	SECKILL_OVER(500500, "商品已经秒杀完毕"),
	REPEATE_SECKILL(500501, "不能重复秒杀"),
	SECKILL_SECKILL(500502, "秒杀失败");

	private int code;
	private String msg;
	CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
