package com.example.wisatareligi.model.komentar;

import com.google.gson.annotations.SerializedName;

public class KomentarItem{

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("waktu")
	private String waktu;

	@SerializedName("id")
	private String id;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
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

	public void setWaktu(String waktu){
		this.waktu = waktu;
	}

	public String getWaktu(){
		return waktu;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"KomentarItem{" + 
			"pesan = '" + pesan + '\'' + 
			",nama = '" + nama + '\'' + 
			",foto = '" + foto + '\'' + 
			",waktu = '" + waktu + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}