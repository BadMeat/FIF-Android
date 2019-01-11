package com.dolan.user.fifandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Badmeat on 16/04/2018.
 */

public class CardViewLay extends Fragment {
    private String judul;
    private String isi;

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getJudul() {
        return judul;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_card_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView judul = view.findViewById(R.id.text_judul);
        TextView isi = view.findViewById(R.id.text_isi);
        judul.setText(getJudul());
        isi.setText(getIsi());
        super.onViewCreated(view, savedInstanceState);
    }
}
