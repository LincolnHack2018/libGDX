package com.lincolnhack;

import java.io.IOException;
import java.util.Map;

public interface ListenerSubscription {
    public void onMessage(Map<String, String> headers, String body) throws IOException;
}
