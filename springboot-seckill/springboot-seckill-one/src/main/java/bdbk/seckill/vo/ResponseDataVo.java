package bdbk.seckill.vo;


/**
 * 用于作为返回给前端的vo类。
 *
 */
public final class ResponseDataVo<T> {

    /**
     * 状态
     */
    private String state = ReturnDataVo.UNKOWN;

    /**
     * 信息。
     */
    private String msg;

    /**
     * 代码，可能代表错误代码。
     */
    private String code;

    /**
     * 返回数据
     */
    private T data;

    private ResponseDataVo(String state, String code, String msg, T data) {
        this.state = state;
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public ResponseDataVo() {
        super();
    }

    /**
     * 成功返回值。
     *
     */
    public static <T> ResponseDataVo<T> success() {
        return new ResponseDataVo<T>(ReturnDataVo.SUCCESS, null, null, null);
    }

    /**
     * 成功返回值。
     *
     */
    public static <T> ResponseDataVo<T> success(T t) {
        return new ResponseDataVo<T>(ReturnDataVo.SUCCESS, null, null, t);
    }

    /**
     * 成功返回值。
     *
     */
    public static <T> ResponseDataVo<T> success(T t, String msg) {
        return new ResponseDataVo<T>(ReturnDataVo.SUCCESS, null, msg, t);
    }


    /**
     * 成功返回值。
     *
     */
    public static <T> ResponseDataVo<T> warn() {
        return new ResponseDataVo<T>(ReturnDataVo.WARN, null, null, null);
    }

    /**
     * 成功返回值。
     *
     */
    public static <T> ResponseDataVo<T> warn(T t) {
        return new ResponseDataVo<T>(ReturnDataVo.WARN, null, null, t);
    }

    /**
     * 成功返回值。
     *
     */
    public static <T> ResponseDataVo<T> warn(T t, String msg) {
        return new ResponseDataVo<T>(ReturnDataVo.WARN, null, msg, t);
    }

    /**
     * 附带消息的失败返回值。
     *
     */
    public static <T> ResponseDataVo<T> error(String msg) {
        return new ResponseDataVo<T>(ReturnDataVo.ERROR, null, msg, null);
    }

    /**
     * 附带代码，消息的失败返回值。
     * 此方法是为了防止没有T参数，而调用code()时候出错误提示。
     *
     */
    public static <T> ResponseDataVo<T> errorCode(String code, String msg) {
        return new ResponseDataVo<T>(ReturnDataVo.ERROR, code, msg, null);
    }

    /**
     * 附带消息的失败返回值。
     *
     */
    public static <T> ResponseDataVo<T> error(T t, String msg) {
        return new ResponseDataVo<T>(ReturnDataVo.ERROR, null, msg, t);
    }

    /**
     * 是否成功。
     *
     */
    public boolean isSuccess() {
        return !ReturnDataVo.WARN.equals(this.state);
    }

    /**
     * 设置代码。
     *
     */
    public ResponseDataVo<T> code(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseDataVo{" +
                "state=" + state +
                ", msg='" + msg + '\'' +
                '}';
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
