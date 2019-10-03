package com.imastudio.customerapp.model.modelmakanan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataMakananItem implements Parcelable {

	@SerializedName("user_avatar")
	private String userAvatar;

	@SerializedName("user_status")
	private String userStatus;

	@SerializedName("user_nama")
	private String userNama;

	@SerializedName("user_email")
	private String userEmail;

	@SerializedName("user_password")
	private String userPassword;

	@SerializedName("id_makanan")
	private String idMakanan;

	@SerializedName("id_kategori")
	private String idKategori;

	@SerializedName("foto_makanan")
	private String fotoMakanan;

	@SerializedName("user_hp")
	private String userHp;

	@SerializedName("user_register")
	private String userRegister;

	@SerializedName("insert_time")
	private String insertTime;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("makanan")
	private String makanan;

	@SerializedName("user_level")
	private String userLevel;

	@SerializedName("user_gcm")
	private String userGcm;

	@SerializedName("nama_kategori")
	private String namaKategori;

	public void setUserAvatar(String userAvatar){
		this.userAvatar = userAvatar;
	}

	public String getUserAvatar(){
		return userAvatar;
	}

	public void setUserStatus(String userStatus){
		this.userStatus = userStatus;
	}

	public String getUserStatus(){
		return userStatus;
	}

	public void setUserNama(String userNama){
		this.userNama = userNama;
	}

	public String getUserNama(){
		return userNama;
	}

	public void setUserEmail(String userEmail){
		this.userEmail = userEmail;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setUserPassword(String userPassword){
		this.userPassword = userPassword;
	}

	public String getUserPassword(){
		return userPassword;
	}

	public void setIdMakanan(String idMakanan){
		this.idMakanan = idMakanan;
	}

	public String getIdMakanan(){
		return idMakanan;
	}

	public void setIdKategori(String idKategori){
		this.idKategori = idKategori;
	}

	public String getIdKategori(){
		return idKategori;
	}

	public void setFotoMakanan(String fotoMakanan){
		this.fotoMakanan = fotoMakanan;
	}

	public String getFotoMakanan(){
		return fotoMakanan;
	}

	public void setUserHp(String userHp){
		this.userHp = userHp;
	}

	public String getUserHp(){
		return userHp;
	}

	public void setUserRegister(String userRegister){
		this.userRegister = userRegister;
	}

	public String getUserRegister(){
		return userRegister;
	}

	public void setInsertTime(String insertTime){
		this.insertTime = insertTime;
	}

	public String getInsertTime(){
		return insertTime;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setMakanan(String makanan){
		this.makanan = makanan;
	}

	public String getMakanan(){
		return makanan;
	}

	public void setUserLevel(String userLevel){
		this.userLevel = userLevel;
	}

	public String getUserLevel(){
		return userLevel;
	}

	public void setUserGcm(String userGcm){
		this.userGcm = userGcm;
	}

	public String getUserGcm(){
		return userGcm;
	}

	public void setNamaKategori(String namaKategori){
		this.namaKategori = namaKategori;
	}

	public String getNamaKategori(){
		return namaKategori;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.userAvatar);
		dest.writeString(this.userStatus);
		dest.writeString(this.userNama);
		dest.writeString(this.userEmail);
		dest.writeString(this.userPassword);
		dest.writeString(this.idMakanan);
		dest.writeString(this.idKategori);
		dest.writeString(this.fotoMakanan);
		dest.writeString(this.userHp);
		dest.writeString(this.userRegister);
		dest.writeString(this.insertTime);
		dest.writeString(this.idUser);
		dest.writeString(this.makanan);
		dest.writeString(this.userLevel);
		dest.writeString(this.userGcm);
		dest.writeString(this.namaKategori);
	}

	public DataMakananItem() {
	}

	protected DataMakananItem(Parcel in) {
		this.userAvatar = in.readString();
		this.userStatus = in.readString();
		this.userNama = in.readString();
		this.userEmail = in.readString();
		this.userPassword = in.readString();
		this.idMakanan = in.readString();
		this.idKategori = in.readString();
		this.fotoMakanan = in.readString();
		this.userHp = in.readString();
		this.userRegister = in.readString();
		this.insertTime = in.readString();
		this.idUser = in.readString();
		this.makanan = in.readString();
		this.userLevel = in.readString();
		this.userGcm = in.readString();
		this.namaKategori = in.readString();
	}

	public static final Parcelable.Creator<DataMakananItem> CREATOR = new Parcelable.Creator<DataMakananItem>() {
		@Override
		public DataMakananItem createFromParcel(Parcel source) {
			return new DataMakananItem(source);
		}

		@Override
		public DataMakananItem[] newArray(int size) {
			return new DataMakananItem[size];
		}
	};
}