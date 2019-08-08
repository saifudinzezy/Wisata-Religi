package com.example.wisatareligi.model.komentar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseKomentar{

	@SerializedName("msg")
	private String msg;

	@SerializedName("total")
	private String total;

	@SerializedName("code")
	private int code;

	@SerializedName("komentar")
	private List<KomentarItem> komentar;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setKomentar(List<KomentarItem> komentar){
		this.komentar = komentar;
	}

	public List<KomentarItem> getKomentar(){
		return komentar;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseKomentar{" + 
			"msg = '" + msg + '\'' + 
			",total = '" + total + '\'' + 
			",code = '" + code + '\'' + 
			",komentar = '" + komentar + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}