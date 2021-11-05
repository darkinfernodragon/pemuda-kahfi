package net.atommobile.pemudakahfi.member;

import android.content.DialogInterface;
//import android.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AlertDialog;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageMemberProfileBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class MemberProfilePage extends Fragment {

    PageMemberProfileBinding binding;
    MemberModel model;

    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_member_profile, container, false);
        View v = binding.getRoot();

        db = new DatabaseHandler(getActivity());
        model = db.getMember();

        if (model.getTelepon().toString().equals("0")) model.setTelepon("-");
        if (model.getAlamat().toString().equals("0")) model.setAlamat("-");
        if (model.getProvinsi().toString().equals("null")) model.setProvinsi("-");
        if (model.getKota().toString().equals("null")) model.setKota("-");
        if (model.getKodepos().toString().equals("0")) model.setKodepos("-");
        if (model.getGender().toString().equals("0")) model.setGender("-");
        if (model.getTgllahir().toString().equals("0000-00-00")) model.setTgllahir("-");

        binding.setModel(model);

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Log out")
                        .setMessage("Anda yakin log out?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                db.Logout();
                                Fragment fragment = new MemberLoginPage();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).commit();

                            }})

                        .setNegativeButton("Tidak", null).show();

            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new MemberProfileEdit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

            }
        });

        return v;

    }

}
