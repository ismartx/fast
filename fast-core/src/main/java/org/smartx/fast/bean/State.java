package org.smartx.fast.bean;

import java.io.Serializable;

/**
 * 返回状态信息
 *
 * @author kext
 * @since 1.0
 */
public class State implements Serializable {

    private static final long serialVersionUID = -2557017842500913562L;

    private int code;

    private String msg;

    public State() {
    }

    public State(int code, String msg) {
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
