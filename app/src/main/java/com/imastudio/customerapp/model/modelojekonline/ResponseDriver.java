package com.imastudio.customerapp.model.modelojekonline;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseDriver{

	@SerializedName("result")
	private String result;

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private List<DataInfoDriver> data;

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

	public void setData(List<DataInfoDriver> data){
		this.data = data;
	}

	public List<DataInfoDriver> getData(){
		return data;
	}
}