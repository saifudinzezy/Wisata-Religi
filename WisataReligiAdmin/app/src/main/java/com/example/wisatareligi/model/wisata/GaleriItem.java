package com.example.wisatareligi.model.wisata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GaleriItem implements Parcelable {

	@SerializedName("desk_foto")
	private String deskFoto;

	@SerializedName("nama_foto")
	private String namaFoto;

	public void setDeskFoto(String deskFoto){
		this.deskFoto = deskFoto;
	}

	public String getDeskFoto(){
		return deskFoto;
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
			",nama_foto = '" + namaFoto + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.deskFoto);
		dest.writeString(this.namaFoto);
	}

	public GaleriItem() {
	}

	protected GaleriItem(Parcel in) {
		this.deskFoto = in.readString();
		this.namaFoto = in.readString();
	}

	public static final Parcelable.Creator<GaleriItem> CREATOR = new Parcelable.Creator<GaleriItem>() {
		@Override
		public GaleriItem createFromParcel(Parcel source) {
			return new GaleriItem(source);
		}

		@Override
		public GaleriItem[] newArray(int size) {
			return new GaleriItem[size];
		}
	};
}