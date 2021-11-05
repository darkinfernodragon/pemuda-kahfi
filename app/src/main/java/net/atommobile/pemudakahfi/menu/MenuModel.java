package net.atommobile.pemudakahfi.menu;

/**
 * Created by root on 18/04/16.
 */
public class MenuModel {

    String id;
    String page;
    String caption;
    int image;

    public MenuModel(String id, String page, String caption, int image){
        super();
        this.id = id;
        this.page = page;
        this.caption = caption;
        this.image = image;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getCaption() {
        return caption;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getPage() {
        return page;
    }
}
