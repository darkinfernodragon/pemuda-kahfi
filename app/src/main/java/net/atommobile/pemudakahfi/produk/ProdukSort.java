package net.atommobile.pemudakahfi.produk;

import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.atommobile.pemudakahfi.R;

import java.util.ArrayList;

/**
 * Created by faizlidwibrido on 9/15/16.
 */
public class ProdukSort extends DialogFragment {

    ArrayList<String> items = new ArrayList<>();

    ListView listView;

    String id_aplikasi;

    //Interface created for communicating this dialog fragment events to called fragment
    public interface SortDialog{
        void setSort(String nama);
    }

    SortDialog sort;

    public void setSortDialog(SortDialog sort){
        this.sort = sort;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_produk_kategori, container);

        listView = (ListView) view.findViewById(R.id.list_kategori);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        id_aplikasi = getString(R.string.app_id);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        items.add("Terbaru");
        items.add("Termurah");
        items.add("Termahal");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();

                sort.setSort(items.get(i));
            }
        });

    }
}
