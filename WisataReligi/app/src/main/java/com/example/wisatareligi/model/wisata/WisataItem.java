package com.example.wisatareligi.model.wisata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WisataItem implements Parcelable {

	@SerializedName("id_wisata")
	private String idWisata;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("galeri")
	private List<GaleriItem> galeri;

	@SerializedName("desk")
	private String desk;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("longitude")
	private String longitude;

	public void setIdWisata(String idWisata){
		this.idWisata = idWisata;
	}

	public String getIdWisata(){
		return idWisata;
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

	public void setGaleri(List<GaleriItem> galeri){
		this.galeri = galeri;
	}

	public List<GaleriItem> getGaleri(){
		return galeri;
	}

	public void setDesk(String desk){
		this.desk = desk;
	}

	public String getDesk(){
		return desk;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"WisataItem{" + 
			"id_wisata = '" + idWisata + '\'' + 
			",nama = '" + nama + '\'' + 
			",foto = '" + foto + '\'' + 
			",galeri = '" + galeri + '\'' + 
			",desk = '" + desk + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",alamat = '" + alamat + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.idWisata);
		dest.writeString(this.nama);
		dest.writeString(this.foto);
		dest.writeList(this.galeri);
		dest.writeString(this.desk);
		dest.writeString(this.latitude);
		dest.writeString(this.alamat);
		dest.writeString(this.longitude);
	}

	public WisataItem() {
	}

	protected WisataItem(Parcel in) {
		this.idWisata = in.readString();
		this.nama = in.readString();
		this.foto = in.readString();
		this.galeri = new ArrayList<GaleriItem>();
		in.readList(this.galeri, GaleriItem.class.getClassLoader());
		this.desk = in.readString();
		this.latitude = in.readString();
		this.alamat = in.readString();
		this.longitude = in.readString();
	}

	public static final Parcelable.Creator<WisataItem> CREATOR = new Parcelable.Creator<WisataItem>() {
		@Override
		public WisataItem createFromParcel(Parcel source) {
			return new WisataItem(source);
		}

		@Override
		public WisataItem[] newArray(int size) {
			return new WisataItem[size];
		}
	};
}