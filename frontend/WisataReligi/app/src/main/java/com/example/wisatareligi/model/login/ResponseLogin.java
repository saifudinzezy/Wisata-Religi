package com.example.wisatareligi.model.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLogin{

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private List<UserItem> user;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUser(List<UserItem> user){
		this.user = user;
	}

	public List<UserItem> getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"ResponseLogin{" + 
			"code = '" + code + '\'' + 
			",message = '" + message + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}