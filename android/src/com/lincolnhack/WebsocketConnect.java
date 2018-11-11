package com.lincolnhack;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lincolnhack.data.Response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.lincolnhack.LibGDX.id;
import static java.util.Arrays.stream;

public class WebsocketConnect extends AsyncTask<String, Void, String> {
    private boolean result;

    @Override
    protected String doInBackground(String... strings) {
        websocket();
        return null;
    }

    private void websocket() {
        Stomp s = new Stomp("ws://hockey-hack.herokuapp.com/socket", Collections.emptyMap(), state -> {
        });

        try {
            s.connect();
        } catch (Exception e) {
            result = false;
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        s.subscribe(new Subscription("/init", (headers, body) -> {
            System.out.println(body);
            List<Response> responses = stream(objectMapper.readValue(body, Response[].class))
                    .filter(r -> r.getId().equals(id))
                    .collect(Collectors.toList());

            LibGDX.setResponses(responses);
            System.out.println(responses);
        }));

        result = true;
    }

    public boolean isResult() {
        return result;
    }
}
