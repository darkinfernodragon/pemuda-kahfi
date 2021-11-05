package net.atommobile.pemudakahfi.produk;

//import android.databinding.ObservableArrayList;
//import android.support.v4.app.Fragment;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by root on 31/08/16.
 */
public class ProdukItemModel {

    private ObservableArrayList<ProdukSliderModel> images = new ObservableArrayList<>();
    private CirclePageIndicator indicator;

    private String id;
    private String harga;
    private String title;
    private String image;

    private String kategori;
    private String deskripsi;

    private Fragment fragment;

    public ProdukItemModel(String id, String title, String harga, String image){
        this.id = id;

        this.harga = String.format("%,d", Integer.parseInt(harga)).replace(",", ".");

        this.title = title;
        this.image = image;
    }

    public ProdukItemModel(){
    }

    public void AddSlide(String id, String image){
        images.add(new ProdukSliderModel(id, image));
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setHarga(String harga) {
        this.harga = String.format("%,d", Integer.parseInt(harga)).replace(",", ".");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getHarga() {
        return harga;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public CirclePageIndicator getIndicator() {
        return indicator;
    }

    public ObservableArrayList<ProdukSliderModel> getImages() {
        return images;
    }

    public void setImages(ObservableArrayList<ProdukSliderModel> images) {
        this.images = images;
    }

    public void setIndicator(CirclePageIndicator indicator) {
        this.indicator = indicator;
    }
}
