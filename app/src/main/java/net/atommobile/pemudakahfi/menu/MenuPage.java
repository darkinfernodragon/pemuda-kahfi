package net.atommobile.pemudakahfi.menu;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;

/**
 * Created by root on 18/04/16.
 */
public class MenuPage extends Fragment {

    GridView gridMenu;
    ArrayList<MenuModel> gridArray = new ArrayList<MenuModel>();
    MenuAdapter menuAdapter;

    Boolean gridViewResized = false;
    int numItem;

    Fragment menuFragment = this;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View v = inflater.inflate(R.layout.page_menu,container,false);
        final View v;
        try {
            v = inflater.inflate(R.layout.page_menu,container, false);
            // ... rest of body of onCreateView() ...
        } catch (Exception e) {
            Log.e(TAG, "onCreateView", e);
            throw e;
        }

        setHasOptionsMenu(true);

        LinearLayout layout_menu = (LinearLayout) v.findViewById(R.id.layout_menu);
        ((MainActivity)getActivity()).setBackground(v, layout_menu, getResources());

        String[] idMenu = getResources().getStringArray(R.array.menu_id);
        String[] pageMenu = getResources().getStringArray(R.array.menu_page);
        String[] captionMenu = getResources().getStringArray(R.array.menu_caption);
        TypedArray imageMenu = getResources().obtainTypedArray(R.array.menu_image);

        numItem = idMenu.length;

        for (int i = 0; i < numItem; i++) {
            gridArray.add(new MenuModel(idMenu[i], pageMenu[i], captionMenu[i], imageMenu.getResourceId(i, 0)));
        }

        gridMenu = (GridView) v.findViewById(R.id.grid_menu);

        menuAdapter = new MenuAdapter(getActivity(), R.layout.page_menu_item, gridArray);
        gridMenu.setAdapter(menuAdapter);

        gridMenu.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!gridViewResized) {
                    gridViewResized = true;
                    resizeGridView(gridMenu, numItem, 2);
                }
            }
        });

        gridMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridView lv = (GridView) v.findViewById(R.id.grid_menu);
                final MenuModel item = (MenuModel) lv.getAdapter().getItem(position);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                new MenuAction(item, fragmentManager, menuFragment);

                ((MainActivity) getActivity()).setTitle(item.getCaption());

            }
        });

        gridMenu.setFocusable(false);

        return v;
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        // TODO Add your menu entries here
//        inflater.inflate(R.menu.menu_home, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.exit:
//                System.exit(1);
//                break;
//
//            case R.id.refresh:
//                //webView.reload();
//                break;
//        }
//        return true;
//
//    }

    private void resizeGridView(GridView gridView, int items, int columns) {
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        int oneRowHeight = gridView.getHeight();
        int rows = (int) (items / columns);
        params.height = oneRowHeight * rows;
        gridView.setLayoutParams(params);
    }


}
