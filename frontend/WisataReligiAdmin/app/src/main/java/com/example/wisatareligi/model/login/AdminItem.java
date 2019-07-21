package com.example.wisatareligi.model.login;

import com.google.gson.annotations.SerializedName;

public class AdminItem{

	@SerializedName("password")
	private String password;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_admin")
	private String idAdmin;

	@SerializedName("email")
	private String email;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setIdAdmin(String idAdmin){
		this.idAdmin = idAdmin;
	}

	public String getIdAdmin(){
		return idAdmin;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"AdminItem{" + 
			"password = '" + password + '\'' + 
			",nama = '" + nama + '\'' + 
			",id_admin = '" + idAdmin + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}