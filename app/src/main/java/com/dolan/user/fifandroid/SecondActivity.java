package com.dolan.user.fifandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Badmeat on 05/04/2018.
 */

public class SecondActivity extends AppCompatActivity {

    ProgressBar pBar;
    EditText nama, alamat, agama, jk, tgllahir, nokk, niku;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        agama = findViewById(R.id.agama);
        jk = findViewById(R.id.jeniskelamin);
        tgllahir = findViewById(R.id.tgllahir);
        nokk = findViewById(R.id.nokk);
        niku = findViewById(R.id.niku);
        Button cari = findViewById(R.id.cari);
        pBar = findViewById(R.id.progress);
        pBar.setVisibility(View.GONE);
        Button logout = findViewById(R.id.logout);
        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(SecondActivity.this);
                GetingNIP();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("Token", loadToken());
    }

    private String loadToken() {
        String token;
        SharedPreferences preferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "data not found");
        return token;
    }

    private void GetingNIP() {
        pBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.43.54:8181/api-gateway-ws/apigateway/getDataWni";
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nik", niku.getText());
            jsonBody.put("fif_branch_code", "80900");
            jsonBody.put("fif_app_code", "FIFAPP01");
            jsonBody.put("fif_app_ip_address", "10.172.69.900");
            jsonBody.put("fif_app_admin_user_id", "FIFAppAdmin02");
            jsonBody.put("fif_app_admin_user_pwd", "sdfg45he");
            jsonBody.put("fif_app_user_id_login", "MorisSoft");
            jsonBody.put("fif_app_kios_pos_code", "70310");
            jsonBody.put("fif_app_no_permohonan", "70310");
            Log.e("PARAM PAK", jsonBody.toString());
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    try {
                        Log.e("Responnya", "Responnya : " + response.toString());
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("resp_code");
                        if (status.equalsIgnoreCase("6")) {
                            JSONArray dataMap = object.getJSONArray("DATA_MAPPING");
                            for (int a = 0; a < dataMap.length(); a++) {
                                JSONObject objenya = dataMap.getJSONObject(a);
                                nama.setText(objenya.getString("NAMA_LGKP"));
                                alamat.setText(objenya.getString("TMPT_LHR"));
                                agama.setText(objenya.getString("AGAMA"));
                                tgllahir.setText(objenya.getString("TGL_LHR"));
                                jk.setText(objenya.getString("JENIS_KLMIN"));
                                nokk.setText(objenya.getString("NO_KK"));
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                pBar.setVisibility(View.GONE);
                            }
                        } else {
                            ErrorResponse();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
                    alertDialog.setTitle("DATA");
                    alertDialog.setMessage(error.toString());
                    alertDialog.show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    pBar.setVisibility(View.GONE);
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    Log.e("STATUS CODE", response.statusCode + "");
                    if (response.statusCode == 400) {
                        Log.e("Nggonku", "" + response.statusCode);
                        finish();
                    }
                    return super.parseNetworkResponse(response);
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Bearer " + loadToken());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            Log.e("Error Pak", e.getMessage());
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void ErrorResponse() {
        nama.setText("");
        alamat.setText("");
        agama.setText("");
        tgllahir.setText("");
        jk.setText("");
        nokk.setText("");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
        alertDialog.setTitle("DATA");
        alertDialog.setMessage("DATA " + niku.getText().toString() + " TIDAK DITEMUKAN");
        alertDialog.show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pBar.setVisibility(View.GONE);
    }
}