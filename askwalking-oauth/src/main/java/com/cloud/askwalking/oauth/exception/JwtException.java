package com.cloud.askwalking.oauth.exception;

/**
 * @author niuzhiwei
 */
public class JwtException extends RuntimeException {

    private static final long serialVersionUID = 7988894630081319767L;
    private int errorCode;
    private String errorMsg;

    protected JwtException(Throwable t) {
        super(t);
    }

    protected JwtException(Integer errorCode, String errorMsg) {
        super(String.format("[errorCode:%s errorMsg:%s]", errorCode, errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    protected JwtException(Integer errorCode, String errorMsg, Throwable cause) {
        super(String.format("[errorCode:%s errorMsg:%s]", errorCode, errorMsg), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
