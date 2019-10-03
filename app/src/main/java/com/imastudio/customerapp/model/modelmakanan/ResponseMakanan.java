package com.imastudio.customerapp.model.modelmakanan;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseMakanan{

	@SerializedName("DataMakanan")
	private ArrayList<DataMakananItem> dataMakanan;

	public void setDataMakanan(ArrayList<DataMakananItem> dataMakanan){
		this.dataMakanan = dataMakanan;
	}

	public ArrayList<DataMakananItem> getDataMakanan(){
		return dataMakanan;
	}
}