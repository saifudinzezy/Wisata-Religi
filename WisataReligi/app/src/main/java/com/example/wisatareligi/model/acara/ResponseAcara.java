package com.example.wisatareligi.model.acara;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAcara implements Parcelable {

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("response")
	private String response;

	@SerializedName("acara")
	private List<AcaraItem> acara;

	public void setPesan(String pesan){
		this.pesan = pesan;
	}

	public String getPesan(){
		return pesan;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setAcara(List<AcaraItem> acara){
		this.acara = acara;
	}

	public List<AcaraItem> getAcara(){
		return acara;
	}

	@Override
 	public String toString(){
		return 
			"ResponseAcara{" + 
			"pesan = '" + pesan + '\'' + 
			",response = '" + response + '\'' + 
			",acara = '" + acara + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.pesan);
		dest.writeString(this.response);
		dest.writeTypedList(this.acara);
	}

	public ResponseAcara() {
	}

	protected ResponseAcara(Parcel in) {
		this.pesan = in.readString();
		this.response = in.readString();
		this.acara = in.createTypedArrayList(AcaraItem.CREATOR);
	}

	public static final Parcelable.Creator<ResponseAcara> CREATOR = new Parcelable.Creator<ResponseAcara>() {
		@Override
		public ResponseAcara createFromParcel(Parcel source) {
			return new ResponseAcara(source);
		}

		@Override
		public ResponseAcara[] newArray(int size) {
			return new ResponseAcara[size];
		}
	};
}