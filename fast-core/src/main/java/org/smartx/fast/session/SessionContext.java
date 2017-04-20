package org.smartx.fast.session;

import org.apache.commons.lang3.StringUtils;
import org.smartx.fast.bean.ApiRequest;
import org.smartx.fast.bean.ApiResponse;
import org.smartx.fast.bean.Client;
import org.smartx.fast.bean.SessionUser;

/**
 * session上下文
 *
 * @author kext
 * @since fast 1.0
 */
public interface SessionContext {

    /**
     * 判断session user是否存在
     */
    boolean existSessionUser(ApiRequest request);

    /**
     * 判断session id是否存在
     */
    boolean existSessionId(ApiRequest request);

    /**
     * 判断是否登录
     */
    boolean validLogin(String uid, String sid);

    String getSidByUid(String uid);

    /**
     * 判断session user是否相同
     */
    boolean isSameSessionUser(ApiRequest request);

    /**
     * put session user
     */
    void putSessionUser(SessionUser user);

    /**
     * put session user
     */
    void putSessionUser(SessionUser user, ApiResponse response);

    /**
     * destroy session user
     */
    void destroySessionUser(ApiRequest request);

    /**
     * destroy session id
     */
    void destroySessionId(ApiRequest request);

    /**
     * put client
     */
    void putClient(Client client, String uid);

    default boolean validateSessionParam(SessionUser user) {
        return null != user && StringUtils.isNotEmpty(user.getSid()) && StringUtils.isNotEmpty(user.getUid());
    }

    default SessionUser getSessionUserByUid(String uid) {
        return new SessionUser(uid, this.getSidByUid(String.valueOf(uid)));
    }
}
