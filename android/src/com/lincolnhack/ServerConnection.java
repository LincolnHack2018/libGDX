package com.lincolnhack;

import android.os.AsyncTask;

import com.lincolnhack.interfaces.Network;

public class ServerConnection implements Network {
    @Override
    public boolean connect() {
        WebsocketConnect websocketConnect = new WebsocketConnect();
        websocketConnect.execute();

        while(websocketConnect.getStatus() == AsyncTask.Status.RUNNING){ }

        return websocketConnect.isResult();
    }
}
