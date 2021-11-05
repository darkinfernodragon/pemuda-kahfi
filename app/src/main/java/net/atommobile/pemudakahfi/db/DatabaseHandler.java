package net.atommobile.pemudakahfi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.atommobile.pemudakahfi.member.MemberModel;
import net.atommobile.pemudakahfi.transaksi.keranjang.KeranjangItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faizlidwibrido on 9/6/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "atomdb";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE token (id INTEGER PRIMARY KEY, token TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CreateKeranjang = "CREATE TABLE keranjang (" +
                "id INTEGER PRIMARY KEY, " +
                "id_produk INTEGER, " +
                "id_attribut INTEGER, " +
                "id_ukuran INTEGER, " +
                "nama TEXT, " +
                "ukuran TEXT, " +
                "qty INTEGER, " +
                "stok INTEGER, " +
                "berat INTEGER, " +
                "harga INTEGER, " +
                "gambar TEXT)";
        db.execSQL(CreateKeranjang);

        String CreateMember = "CREATE TABLE member (" +
                "id_member INTEGER PRIMARY KEY, " +
                "email TEXT, " +
                "password TEXT, " +
                "nama TEXT, " +
                "telepon TEXT, " +
                "alamat TEXT, " +
                "id_provinsi INTEGER, " +
                "provinsi TEXT, " +
                "id_kota INTEGER, " +
                "kota TEXT, " +
                "kodepos TEXT, " +
                "gender TEXT, " +
                "tgllahir TEXT)";
        db.execSQL(CreateMember);

        String CreateTransaksi = "CREATE TABLE transaksi (" +
                "id INTEGER PRIMARY KEY, " +
                "id_transaksi TEXT)";
        db.execSQL(CreateTransaksi);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS token");
        db.execSQL("DROP TABLE IF EXISTS keranjang");
        db.execSQL("DROP TABLE IF EXISTS member");
        db.execSQL("DROP TABLE IF EXISTS transaksi");
        onCreate(db);
    }

    //TOKEN ----------------------------------------------------------------------------

    public void addToken(String token) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("token", token);

        // Inserting Row
        db.insert("token", null, values);
        db.close(); // Closing database connection
    }

    // Deleting single contact
    public void deleteToken() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("token", null, null);
        db.close();
    }

    // Getting contacts Count
    public int getTokenAvailable() {
        String countQuery = "SELECT  * FROM token";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    //-----------------------------------------------------------------------------------------------

    //KERANJANG ----------------------------------------------------------------------------

    public void addKeranjang(KeranjangItemModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_produk", model.getId_produk());
        values.put("id_attribut", model.getId_attribut());
        values.put("id_ukuran", model.getId_ukuran());
        values.put("nama", model.getNama());
        values.put("ukuran", model.getUkuran());
        values.put("qty", model.getQty());
        values.put("stok", model.getStok());
        values.put("berat", model.getBerat());
        values.put("harga", model.getHarga().replace(".", ""));
        values.put("gambar", model.getGambar());

        db.insert("keranjang", null, values);
        db.close();
    }

    public List<KeranjangItemModel> getAllKeranjang() {
        List<KeranjangItemModel> keranjangList = new ArrayList<KeranjangItemModel>();

        String selectQuery = "SELECT  * FROM keranjang";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                keranjangList.add(new KeranjangItemModel(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        return keranjangList;
    }

    public int cekProdukKeranjang(String id_ukuran) {

        String countQuery = "SELECT  * FROM keranjang WHERE id_ukuran = '" + id_ukuran + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int countAll = cursor.getCount();
        cursor.close();

        // return count
        return countAll;
    }

    public int updateKeranjang(KeranjangItemModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_produk", model.getId_produk());
        values.put("id_attribut", model.getId_attribut());
        values.put("id_ukuran", model.getId_ukuran());
        values.put("nama", model.getNama());
        values.put("ukuran", model.getUkuran());
        values.put("qty", model.getQty());
        values.put("stok", model.getStok());
        values.put("berat", model.getBerat());
        values.put("harga", model.getHarga().replace(".", ""));
        values.put("gambar", model.getGambar());

        return db.update("keranjang", values, "id = ?", new String[]{String.valueOf(model.getId())});
    }

    public void deleteKeranjang(KeranjangItemModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("keranjang", "id = ?",
                new String[]{String.valueOf(model.getId())});
        db.close();
    }

    public int getKeranjangCount() {
        String countQuery = "SELECT  * FROM keranjang";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int countAll = cursor.getCount();
        cursor.close();

        // return count
        return countAll;
    }

    public void clearKeranjang() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("keranjang", null, null);
        db.close();
    }


    //-----------------------------------------------------------------------------------------------

    //MEMBER ----------------------------------------------------------------------------


    public void addMember(MemberModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_member", model.getId_member());
        values.put("email", model.getEmail());
        values.put("password", model.getPassword());
        values.put("nama", model.getNama());
        values.put("telepon", model.getTelepon());
        values.put("alamat", model.getAlamat());
        values.put("id_provinsi", model.getId_provinsi());
        values.put("provinsi", model.getProvinsi());
        values.put("id_kota", model.getId_kota());
        values.put("kota", model.getKota());
        values.put("kodepos", model.getKodepos());
        values.put("gender", model.getGender());
        values.put("tgllahir", model.getTgllahir());

        db.insert("member", null, values);
        db.close();
    }

    public MemberModel getMember() {

        MemberModel member = null;
        String selectQuery = "SELECT  * FROM member";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            member = new MemberModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12));
        }

        return member;
    }

    public int updateMember(MemberModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_member", model.getId_member());
        values.put("email", model.getEmail());
        values.put("password", model.getPassword());
        values.put("nama", model.getNama());
        values.put("telepon", model.getTelepon());
        values.put("alamat", model.getAlamat());
        values.put("id_provinsi", model.getId_provinsi());
        values.put("provinsi", model.getProvinsi());
        values.put("id_kota", model.getId_kota());
        values.put("kota", model.getKota());
        values.put("kodepos", model.getKodepos());
        values.put("gender", model.getGender());
        values.put("tgllahir", model.getTgllahir());

        // updating row
        return db.update("member", values, "id_member = ?", new String[]{String.valueOf(model.getId_member())});
    }

    public boolean cekLogin() {
        String countQuery = "SELECT  * FROM member";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int countAll = cursor.getCount();
        cursor.close();

        if(countAll == 0){
            return false;
        } else {
            return true;
        }
    }

    public void Logout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("member", null, null);
        db.close();
    }

    //-----------------------------------------------------------------------------------------------

    //MEMBER ----------------------------------------------------------------------------


    public void setTransaksi(String id_transaksi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_transaksi", id_transaksi);

        db.insert("transaksi", null, values);
        db.close();
    }

    public String getTransaksi() {

        String id_transaksi = "";
        String selectQuery = "SELECT  * FROM transaksi";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            id_transaksi = cursor.getString(1);
        }

        return id_transaksi;
    }

    public void deleteTransaksi(String id_transaksi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("keranjang", "id = ?",
                new String[]{String.valueOf(id_transaksi)});
        db.close();
    }

    //-----------------------------------------------------------------------------------------------

}
