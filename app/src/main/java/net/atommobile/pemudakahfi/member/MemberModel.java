package net.atommobile.pemudakahfi.member;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class MemberModel {

    private String id_member;
    private String email;
    private String password;
    private String nama;
    private String telepon;
    private String alamat;
    private String id_provinsi;
    private String provinsi;
    private String id_kota;
    private String kota;
    private String kodepos;
    private String gender;
    private String tgllahir;

    public MemberModel(){}

    public MemberModel(String id_member, String email, String password, String nama, String telepon, String alamat, String id_provinsi, String provinsi, String id_kota, String kota, String kodepos, String gender, String tgllahir){
        this.id_member = id_member;
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
        this.id_provinsi = id_provinsi;
        this.provinsi = provinsi;
        this.id_kota = id_kota;
        this.kota = kota;
        this.kodepos = kodepos;
        this.gender = gender;
        this.tgllahir = tgllahir;
    }

    public String getId_kota() {
        return id_kota;
    }

    public String getId_member() {
        return id_member;
    }

    public String getId_provinsi() {
        return id_provinsi;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getKodepos() {
        return kodepos;
    }

    public String getKota() {
        return kota;
    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return password;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getTgllahir() {
        return tgllahir;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public void setTgllahir(String tgllahir) {
        this.tgllahir = tgllahir;
    }

    public void setId_kota(String id_kota) {
        this.id_kota = id_kota;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public void setId_provinsi(String id_provinsi) {
        this.id_provinsi = id_provinsi;
    }
}
