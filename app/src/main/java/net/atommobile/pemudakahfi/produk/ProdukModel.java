package net.atommobile.pemudakahfi.produk;

//import android.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayList;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by root on 31/08/16.
 */
public class ProdukModel {

    private ObservableArrayList<ProdukItemModel> list = new ObservableArrayList<>();

    private ObservableArrayList<ProdukSliderModel> images = new ObservableArrayList<>();
    private CirclePageIndicator indicator;

    public void AddData(String id, String title, String harga, String image){
        list.add(new ProdukItemModel(id, title, harga, image));
    }

    public void AddSlide(String id, String image){
        images.add(new ProdukSliderModel(id, image));
    }

    public CirclePageIndicator getIndicator() {
        return indicator;
    }

    public void setImages(ObservableArrayList<ProdukSliderModel> images) {
        this.images = images;
    }

    public void setIndicator(CirclePageIndicator indicator) {
        this.indicator = indicator;
    }

    public void setList(ObservableArrayList<ProdukItemModel> list) {
        this.list = list;
    }

    public ObservableArrayList<ProdukSliderModel> getImages() {
        return images;
    }

    public ObservableArrayList<ProdukItemModel> getList() {
        return list;
    }
}
