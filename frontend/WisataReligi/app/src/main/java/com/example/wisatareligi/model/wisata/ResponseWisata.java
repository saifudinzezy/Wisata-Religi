package com.example.wisatareligi.model.wisata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWisata{

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("wisata")
	private List<WisataItem> wisata;

	@SerializedName("response")
	private String response;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setWisata(List<WisataItem> wisata){
		this.wisata = wisata;
	}

	public List<WisataItem> getWisata(){
		return wisata;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	@Override
 	public String toString(){
		return 
			"ResponseWisata{" + 
			"pesan = '" + pesan + '\'' + 
			",wisata = '" + wisata + '\'' + 
			",response = '" + response + '\'' + 
			"}";
		}
}