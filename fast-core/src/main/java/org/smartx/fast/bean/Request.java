package org.smartx.fast.bean;

import java.io.Serializable;

/**
 * Request
 *
 * @author kext
 * @since 1.0
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 8548585751220528426L;

    private String id;

    private String sign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
