package throwPackage;

import java.io.Serializable;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/13 17:51
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1111L;

    /**
     * 错误码
     */
    private String code;

    private String tmp;

    /**
     * 错误码对应的参数
     */
    private Object args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}
