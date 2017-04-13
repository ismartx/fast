package org.smartx.fast.bean;

import java.util.List;

/**
 * 组合请求
 *
 * @author kext
 */
public class CombineRequest extends Request {

    private static final long serialVersionUID = -7805271112416454149L;

    private List<CombineRequestData> data;

    public List<CombineRequestData> getData() {
        return data;
    }

    public void setData(List<CombineRequestData> data) {
        this.data = data;
    }

}
