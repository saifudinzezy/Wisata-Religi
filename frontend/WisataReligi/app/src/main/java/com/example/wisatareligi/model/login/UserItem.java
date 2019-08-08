package com.example.wisatareligi.model.login;

import com.google.gson.annotations.SerializedName;

public class UserItem{

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("pass")
	private String pass;

	@SerializedName("id")
	private String id;

	@SerializedName("tmp_lahir")
	private String tmpLahir;

	@SerializedName("tgl_lahir")
	private String tglLahir;

	@SerializedName("alamat")
	private String alamat;

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setPass(String pass){
		this.pass = pass;
	}

	public String getPass(){
		return pass;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTmpLahir(String tmpLahir){
		this.tmpLahir = tmpLahir;
	}

	public String getTmpLahir(){
		return tmpLahir;
	}

	public void setTglLahir(String tglLahir){
		this.tglLahir = tglLahir;
	}

	public String getTglLahir(){
		return tglLahir;
	}

	@Override
 	public String toString(){
		return 
			"UserItem{" + 
			"nama = '" + nama + '\'' + 
			",foto = '" + foto + '\'' + 
			",pass = '" + pass + '\'' + 
			",id = '" + id + '\'' + 
			",tmp_lahir = '" + tmpLahir + '\'' + 
			",tgl_lahir = '" + tglLahir + '\'' + 
			"}";
		}
}