package com.lincolnhack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lincolnhack.data.Device;
import com.lincolnhack.interfaces.InitDevice;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnect implements InitDevice {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void init(Device device) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String url = "https://hockey-hack.herokuapp.com/init";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(device);

        OkHttpClient client = getUnsafeOkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).execute();
    }

    private static OkHttpClient getUnsafeOkHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }
                }
        };

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        return new OkHttpClient().newBuilder()
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }
}
