package com.lincolnhack;

import com.lincolnhack.interfaces.Socket;

public class AndroidSocket implements Socket {
    @Override
    public void subscribe() {
        new WebsocketConnect().execute();
    }
}
