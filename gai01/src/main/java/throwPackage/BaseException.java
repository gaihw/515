package throwPackage;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/13 15:50
 */
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = 1111L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public BaseException(){}

    public BaseException(String code,Object obj,String msg){
        this.code = code;
        this.args = obj;
        this.defaultMessage = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getMessage()
    {

        return this.getDefaultMessage();
    }
}
