package org.smartx.fast.bean;

import java.io.Serializable;
import java.util.List;

/**
 * API组合返回
 *
 * @author kext
 */
public class CombineResponse extends Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CombineResponseData> data;

    public List<CombineResponseData> getData() {
        return data;
    }

    public void setData(List<CombineResponseData> data) {
        this.data = data;
    }

    /**
     * 不带msg的ok返回码，减少返回信息
     *
     * @since microrders 1.0
     */
    public static CombineResponse ok() {
        CombineResponse response = new CombineResponse();
        response.setState(StateCode.SUCCESSFUL);
        return response;
    }

    public static CombineResponse okWithMsg() {
        CombineResponse response = new CombineResponse();
        response.setState(StateCode.SUCCESSFUL);
        return response;
    }

    public static CombineResponse error() {
        CombineResponse response = new CombineResponse();
        response.setState(StateCode.SERVER_ERROR);
        return response;
    }

    public static CombineResponse reqContentError() {
        CombineResponse response = new CombineResponse();
        response.setState(StateCode.REQUEST_CONTENT_ERROR);
        return response;
    }

    public static CombineResponse invalidSignError() {
        CombineResponse response = new CombineResponse();
        response.setState(StateCode.INVALID_SIGH_ERROR);
        return response;
    }

    public static CombineResponse missIdError() {
        CombineResponse response = new CombineResponse();
        response.setState(StateCode.MISS_ID_ERROR);
        return response;
    }

}
