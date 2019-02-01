package babk.seckill.constant;

public enum  CodeMsg {
	//通用的错误码
	SUCCESS(0,"success"),
	SERVER_ERROR(500100,"服务端异常"),
	BIND_ERROR(500100,"参数校验异常：%s"),
	//登录模块 5002XX
	SESSION_ERROR(500210,"Session不存在或者已经失效"),
	PASSWORD_EMPTY(500211,"登录密码不能为空"),
	MOBILE_EMPTY(500212,"手机号不能为空"),
	MOBILE_ERROR(500213,"手机号不能为空"),
	MOBILE_NOT_EXIST(500214, "手机号不存在"),
	PASSWORD_ERROR(500215, "密码错误");

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
