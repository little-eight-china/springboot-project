package bdbk.seckill.vo;

/**
 * 执行返回结果。
 *
 * @author axeon
 */
public final class ReturnDataVo {

    /**
     * 成功状态值。
     */
    public static final String SUCCESS = "success";

    /**
     * 报警状态值。
     */
    public static final String WARN = "warn";

    /**
     * 错误状态值。
     */
    public static final String ERROR = "error";

    /**
     * 位置状态值。
     */
    public static final String UNKOWN = "unknow";

    /**
     * 状态
     */
    private String state = UNKOWN;

    /**
     * 信息。
     */
    private String msg;

    /**
     * 代码，可能代表错误代码。
     */
    private String code;

    public ReturnDataVo() {
    }

    private ReturnDataVo(String state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    /**
     * 附带消息的成功返回值。
     *
     */
    public static ReturnDataVo success(String msg) {
        return new ReturnDataVo(SUCCESS, msg);
    }

    /**
     * 成功返回值。
     *
     */
    public static ReturnDataVo success() {
        return new ReturnDataVo(SUCCESS, null);
    }

    /**
     * 附带消息的错误返回值。
     *
     */
    public static ReturnDataVo error(String msg) {
        return new ReturnDataVo(ERROR, msg);
    }

    /**
     * 附带消息的报警返回值。
     *
     */
    public static ReturnDataVo warn(String msg) {
        return new ReturnDataVo(WARN, msg);
    }

    /**
     * 是否成功。
     *
     */
    public boolean isSuccess() {
        return !ERROR.equals(this.state);
    }

    /**
     * 设置代码。
     *
     */
    public ReturnDataVo code(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "ReturnDataVo{" +
                "state=" + state +
                ", msg='" + msg + '\'' +
                '}';
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
