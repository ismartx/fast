package org.smartx.fast.bean;

import org.smartx.commons.utils.UuidUtils;

import java.io.Serializable;

/**
 * API用户信息
 *
 * @author kext
 * @since 1.0
 */
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 7075103450710706249L;

    private String uid;

    private String sid;

    public static final String UID = "uid";

    public static final String SID = "sid";

    public SessionUser() {
    }

    public SessionUser(String uid, String sid) {
        this.uid = uid;
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public static SessionUser create(String uid) {
        return new SessionUser(uid, UuidUtils.create().toLowerCase());
    }

    public static SessionUser create(int uid) {
        return new SessionUser(String.valueOf(uid), UuidUtils.create().toLowerCase());
    }

}
