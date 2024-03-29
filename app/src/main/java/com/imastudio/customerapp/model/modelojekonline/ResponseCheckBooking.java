package com.imastudio.customerapp.model.modelojekonline;

import com.google.gson.annotations.SerializedName;

public class ResponseCheckBooking{

	@SerializedName("result")
	private String result;

	@SerializedName("msg")
	private String msg;

	@SerializedName("driver")
	private String driver;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setDriver(String driver){
		this.driver = driver;
	}

	public String getDriver(){
		return driver;
	}
}