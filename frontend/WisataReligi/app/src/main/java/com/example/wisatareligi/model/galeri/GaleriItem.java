package com.example.wisatareligi.model.galeri;

import com.google.gson.annotations.SerializedName;

public class GaleriItem{

	@SerializedName("desk_foto")
	private String deskFoto;

	@SerializedName("id_galeri")
	private String idGaleri;

	@SerializedName("nama_foto")
	private String namaFoto;

	public void setDeskFoto(String deskFoto){
		this.deskFoto = deskFoto;
	}

	public String getDeskFoto(){
		return deskFoto;
	}

	public void setIdGaleri(String idGaleri){
		this.idGaleri = idGaleri;
	}

	public String getIdGaleri(){
		return idGaleri;
	}

	public void setNamaFoto(String namaFoto){
		this.namaFoto = namaFoto;
	}

	public String getNamaFoto(){
		return namaFoto;
	}

	@Override
 	public String toString(){
		return 
			"GaleriItem{" + 
			"desk_foto = '" + deskFoto + '\'' + 
			",id_galeri = '" + idGaleri + '\'' + 
			",nama_foto = '" + namaFoto + '\'' + 
			"}";
		}
}