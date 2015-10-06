package com.hry.dispatch.domain;

import java.util.Map;

public class Message {

	private String code = "";
	private String result = "";
	private String message = "";
	private Object data = null;
	
	public Message() {
		super();
	}
	public Message(String code, String result, String message) {
		super();
		this.code = code;
		this.result = result;
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Message [code=" + code + ", result=" + result + ", message=" + message + ", data=" + data + "]";
	}
	
}
