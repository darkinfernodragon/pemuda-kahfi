package net.atommobile.pemudakahfi.menu;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransaction;

import net.atommobile.pemudakahfi.album.AlbumPage;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.contact.ContactPage;
import net.atommobile.pemudakahfi.map.MapPage;
import net.atommobile.pemudakahfi.member.MemberLoginPage;
import net.atommobile.pemudakahfi.news.NewsPage;
import net.atommobile.pemudakahfi.produk.ProdukPage;
import net.atommobile.pemudakahfi.text.TextPage;
import net.atommobile.pemudakahfi.transaksi.TransaksiHistory;
import net.atommobile.pemudakahfi.transaksi.keranjang.KeranjangPage;
import net.atommobile.pemudakahfi.video.VideoPage;

/**
 * Created by root on 19/04/16.
 */
public class MenuAction {

    Fragment fragment;

    public MenuAction(MenuModel item, FragmentManager fragmentManager, Fragment menuFragment){

        super();

        String pageMenu = item.getPage();
        String idMenu = item.getId();
        String captionMenu = item.getCaption();

        Bundle bundle = new Bundle();
        bundle.putString("idMenu", idMenu);
        bundle.putString("captionMenu", captionMenu);
        bundle.putString("awal", "");

        if(pageMenu.equals("hal-text")){
            fragment = new TextPage();
        } else if(pageMenu.equals("hal-video")){
            fragment = new VideoPage();
        } else if(pageMenu.equals("hal-kontak")){
            fragment = new ContactPage();
        } else if(pageMenu.equals("hal-lokasi")){
            fragment = new MapPage();
        } else if(pageMenu.equals("hal-berita")){
            fragment = new NewsPage();
        } else if(pageMenu.equals("hal-album")){
            fragment = new AlbumPage();
        } else if(pageMenu.equals("hal-produk")){
            fragment = new ProdukPage();
        } else if(pageMenu.equals("hal-keranjang")){
            fragment = new KeranjangPage();
        } else if(pageMenu.equals("hal-member")){
            fragment = new MemberLoginPage();
        } else if(pageMenu.equals("hal-transaksi")){
            fragment = new TransaksiHistory();
        }

        fragment.setArguments(bundle);

        if(fragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.hide(menuFragment);

            fragmentTransaction.add(R.id.layout_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

}
