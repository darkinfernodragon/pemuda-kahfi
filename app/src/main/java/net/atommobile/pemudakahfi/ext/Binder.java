package net.atommobile.pemudakahfi.ext;

import android.content.Context;
//import android.databinding.BindingAdapter;
//import android.databinding.ObservableArrayList;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import android.graphics.Bitmap;
//import android.support.v4.app.Fragment;
//import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
//import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
//import android.support.v4.view.ViewPager;
import androidx.fragment.app.Fragment;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.viewpager.widget.ViewPager;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.viewpagerindicator.CirclePageIndicator;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.produk.ProdukSliderAdapter;
import net.atommobile.pemudakahfi.produk.ProdukSliderModel;

/**
 * Created by root on 31/08/16.
 */
public class Binder {

    private static String TAG = Binder.class.getSimpleName();

    @BindingAdapter({"imageURL"})
    public static void downloadImage(ImageView imageView, String url){
        // get Context from view
        Context context = imageView.getContext();

        // configuration Glide
        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);
    }

    @BindingAdapter("RoundimageURL")
    public static void downloadImageAvatar(final ImageView imageView, String url){
        // get Context from view
        final Context context = imageView.getContext();

        // configuration Glide
        Glide
                .with(context)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.ic_placeholder)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @BindingAdapter({"items", "indicator", "fragment"})
    public static void SetImageSliderAdapter(ViewPager viewPager, ObservableArrayList<ProdukSliderModel> images, CirclePageIndicator indicator, Fragment fragment){
        ProdukSliderAdapter slideImage = new ProdukSliderAdapter(viewPager.getContext(), images, fragment);
        viewPager.setAdapter(slideImage);
        indicator.setViewPager(viewPager);
    }

}
