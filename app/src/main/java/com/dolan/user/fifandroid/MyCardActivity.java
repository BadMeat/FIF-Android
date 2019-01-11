package com.dolan.user.fifandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyCardActivity extends AppCompatActivity {

    String judul[] = {"MAKAN","PUDING"};
    String isi[] = {"SAMA AKU","NANANA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

        CardViewLay cardViewLay;
        for(int a=0;a<judul.length;a++){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            cardViewLay = new CardViewLay();
            cardViewLay.setJudul(judul[a]);
            cardViewLay.setIsi(isi[a]);
            fragmentTransaction.add(R.id.parent_list,cardViewLay);
            fragmentTransaction.commit();
        }

        WebView webView = findViewById(R.id.jadwal_sholat);
        webView.loadUrl("http://m.jadwalsholat.org");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
    }
}
