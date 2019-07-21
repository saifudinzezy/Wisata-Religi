package com.example.wisatareligi.model.acara;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AcaraItem implements Parcelable {

	@SerializedName("pembicara")
	private String pembicara;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_acara")
	private String idAcara;

	@SerializedName("desk")
	private String desk;

	@SerializedName("waktu")
	private String waktu;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("duror")
	private String duror;

	@SerializedName("alamat")
	private String alamat;

	public void setPembicara(String pembicara){
		this.pembicara = pembicara;
	}

	public String getPembicara(){
		return pembicara;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setIdAcara(String idAcara){
		this.idAcara = idAcara;
	}

	public String getIdAcara(){
		return idAcara;
	}

	public void setDesk(String desk){
		this.desk = desk;
	}

	public String getDesk(){
		return desk;
	}

	public void setWaktu(String waktu){
		this.waktu = waktu;
	}

	public String getWaktu(){
		return waktu;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	public void setDuror(String duror){
		this.duror = duror;
	}

	public String getDuror(){
		return duror;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	@Override
 	public String toString(){
		return 
			"AcaraItem{" + 
			"pembicara = '" + pembicara + '\'' + 
			",nama = '" + nama + '\'' + 
			",id_acara = '" + idAcara + '\'' + 
			",desk = '" + desk + '\'' + 
			",waktu = '" + waktu + '\'' + 
			",tanggal = '" + tanggal + '\'' + 
			",duror = '" + duror + '\'' + 
			",alamat = '" + alamat + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.pembicara);
		dest.writeString(this.nama);
		dest.writeString(this.idAcara);
		dest.writeString(this.desk);
		dest.writeString(this.waktu);
		dest.writeString(this.tanggal);
		dest.writeString(this.duror);
		dest.writeString(this.alamat);
	}

	public AcaraItem() {
	}

	protected AcaraItem(Parcel in) {
		this.pembicara = in.readString();
		this.nama = in.readString();
		this.idAcara = in.readString();
		this.desk = in.readString();
		this.waktu = in.readString();
		this.tanggal = in.readString();
		this.duror = in.readString();
		this.alamat = in.readString();
	}

	public static final Parcelable.Creator<AcaraItem> CREATOR = new Parcelable.Creator<AcaraItem>() {
		@Override
		public AcaraItem createFromParcel(Parcel source) {
			return new AcaraItem(source);
		}

		@Override
		public AcaraItem[] newArray(int size) {
			return new AcaraItem[size];
		}
	};
}