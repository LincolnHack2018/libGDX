package com.lincolnhack;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lincolnhack.data.Device;
import com.lincolnhack.interfaces.InitDevice;

import java.io.UnsupportedEncodingException;

public class HttpConnect implements InitDevice {
    private Context context;

    public HttpConnect(Context context) {
        this.context = context;
    }

    public void init(Device device) throws Exception {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://hockey-hack.herokuapp.com/init";

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(device);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {}, error -> {}) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return body.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }
}
