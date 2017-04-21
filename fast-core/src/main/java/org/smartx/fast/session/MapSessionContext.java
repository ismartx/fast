package org.smartx.fast.session;

import org.apache.commons.lang3.StringUtils;
import org.smartx.fast.bean.ApiRequest;
import org.smartx.fast.bean.Client;
import org.smartx.fast.bean.SessionUser;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author kext
 */
public class MapSessionContext implements SessionContext {

    private final Map<String, Map<String, Object>> map = new HashMap<>();

    @Override
    public boolean existSessionUser(ApiRequest request) {
        SessionUser user = request.getSessionUser();
        if (user != null) {
            String key =
                    MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), user.getUid());
            return map.containsKey(key);
        }
        return false;
    }

    @Override
    public boolean existSessionId(ApiRequest request) {
        SessionUser user = request.getSessionUser();
        if (user != null) {
            String key =
                    MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), user.getUid());
            if (map.containsKey(key)) {
                return map.get(key).containsKey(SessionUser.SID);
            }
        }
        return false;
    }

    @Override
    public boolean validLogin(String uid, String sid) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(sid)) {
            return false;
        }
        String key = MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), uid);
        return map.containsKey(key) && sid.equals(map.get(key).get(SessionUser.SID));
    }

    @Override
    public String getSidByUid(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        String key = MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), uid);
        if (map.containsKey(key)) {
            Object value = map.get(key).get(SessionUser.SID);
            return value != null ? value.toString() : null;
        }
        return null;
    }

    @Override
    public boolean isSameSessionUser(ApiRequest request) {
        SessionUser user = request.getSessionUser();
        if (user != null) {
            String key =
                    MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), user.getUid());
            if (map.containsKey(key)) {
                return user.getSid().equals(map.get(key).get(SessionUser.SID));
            }
        }
        return false;
    }

    @Override
    public void putSessionUser(SessionUser user) {
        String key = MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), user.getUid());
        if (map.containsKey(key)) {
            map.get(key).put(SessionUser.SID, user.getSid());
        } else {
            Map<String, Object> sessionMap = new HashMap<>();
            sessionMap.put(SessionUser.SID, user.getSid());
            map.put(key, sessionMap);
        }
    }

    @Override
    public void destroySessionUser(ApiRequest request) {
        SessionUser user = request.getSessionUser();
        if (user != null) {
            String key =
                    MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), user.getUid());
            map.remove(key);
        }
    }

    @Override
    public void destroySessionId(ApiRequest request) {
        SessionUser user = request.getSessionUser();
        if (user != null) {
            String key =
                    MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), user.getUid());
            map.get(key).remove(SessionUser.SID);
        }
    }

    @Override
    public void putClient(Client client, String uid) {
        String key = MessageFormat.format(SessionKeyEnum.USER_SESSION.getKey(), uid);
        if (map.containsKey(key)) {
            map.get(key).put("client", client);
        } else {
            Map<String, Object> sessionMap = new HashMap<>();
            sessionMap.put("client", client);
            map.put(key, sessionMap);
        }
    }
}
