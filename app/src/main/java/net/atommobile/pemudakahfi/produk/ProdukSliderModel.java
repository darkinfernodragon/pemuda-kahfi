package net.atommobile.pemudakahfi.produk;

/**
 * Created by faizlidwibrido on 8/31/16.
 */
public class ProdukSliderModel {

    private String id;
    private String image;

    public ProdukSliderModel(String id, String image){
        this.id = id;
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

}
