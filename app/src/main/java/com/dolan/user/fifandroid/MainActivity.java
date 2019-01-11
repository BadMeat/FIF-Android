package com.dolan.user.fifandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText user = findViewById(R.id.user);
        final EditText pass = findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getText().toString().equals("admin") && pass.getText().toString().equals("admin")) {
                    GettingToken();
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button pindah = findViewById(R.id.pindah);
        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyCardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GettingToken() {
        SharedPreferences preferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.43.54:8011/auth/realms/fifgroup/protocol/openid-connect/token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                try {
                    JSONObject token = new JSONObject(response);
                    String getToken = token.getString("access_token");
                    editor.putString("token", getToken);
                    editor.apply();
                    Log.e("Token Ku", getToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", "fifapps");
                params.put("client_id", "fifgroup-token");
                params.put("grant_type", "password");
                params.put("client_secret", "a008b5a5-fe9c-486a-a4d5-8f0e2521ca56");
                params.put("username", "fifapps");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 400) {
                    Log.e("Nggonku", "" + response.statusCode);
                    finish();
                }
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(stringRequest);
    }
}
