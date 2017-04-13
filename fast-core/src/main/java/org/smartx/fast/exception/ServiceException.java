package org.smartx.fast.exception;

import org.smartx.fast.bean.State;

/**
 * 服务异常
 *
 * @author kext
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	private String logMsg;
	private int logCode;
	private State state;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, String logMsg) {
		super(message);
		this.logMsg = logMsg;
	}

	public ServiceException(String message, int logCode, String logMsg) {
		super(message);
		this.logMsg = logMsg;
		this.logCode = logCode;
		this.state = new State(logCode, logMsg);
	}

	public ServiceException(State state) {
		super(state.getMsg());
		this.logCode = state.getCode();
		this.logMsg = state.getMsg();
		this.state = state;
	}

	public ServiceException(String message, Throwable cause, State state) {
		super(message, cause);
		this.logCode = state.getCode();
		this.logMsg = state.getMsg();
		this.state = state;
	}

	public ServiceException(String message, State state) {
		super(message);
		this.logCode = state.getCode();
		this.logMsg = state.getMsg();
		this.state = state;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public String getLogMsg() {
		return logMsg;
	}

	public int getLogCode() {
		return logCode;
	}

	public State getState() {
		return state;
	}

}
