package com.example.wisatareligi.model.galeri;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGaleri{

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("galeri")
	private List<GaleriItem> galeri;

	@SerializedName("response")
	private String response;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setGaleri(List<GaleriItem> galeri){
		this.galeri = galeri;
	}

	public List<GaleriItem> getGaleri(){
		return galeri;
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
			"ResponseGaleri{" + 
			"pesan = '" + pesan + '\'' + 
			",galeri = '" + galeri + '\'' + 
			",response = '" + response + '\'' + 
			"}";
		}
}