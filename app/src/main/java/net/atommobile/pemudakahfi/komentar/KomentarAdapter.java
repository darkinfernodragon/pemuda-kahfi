package net.atommobile.pemudakahfi.komentar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import net.atommobile.pemudakahfi.R;

/**
 * Created by root on 30/05/16.
 */
public class KomentarAdapter extends ArrayAdapter<KomentarModel> {

    Context context;
    int layoutResourceId;
    ArrayList<KomentarModel> data = new ArrayList<KomentarModel>();

    public KomentarAdapter(Context context, int layoutResourceId, ArrayList<KomentarModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();

            holder.lblNama = (TextView) row.findViewById(R.id.lbl_nama);
            holder.lblKomentar = (TextView) row.findViewById(R.id.lbl_komentar);
            holder.lblWaktu = (TextView) row.findViewById(R.id.lbl_waktu);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        KomentarModel item = data.get(position);

        holder.lblNama.setText(item.getNama());
        holder.lblKomentar.setText(item.getKomentar());
        holder.lblWaktu.setText(item.getWaktu());

        return row;

    }

    static class RecordHolder {
        TextView lblNama;
        TextView lblWaktu;
        TextView lblKomentar;
    }

}
