package org.smartx.fast.bean;

import java.io.Serializable;

/**
 * Response
 *
 * @author kext
 */
public class Response implements Serializable {

    private static final long serialVersionUID = -6426569063954402514L;

    private String id;

    private State state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
