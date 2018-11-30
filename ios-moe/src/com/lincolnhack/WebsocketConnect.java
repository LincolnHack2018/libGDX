package com.lincolnhack;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lincolnhack.data.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lincolnhack.states.ScreenSetup.id;

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

            List<Response> responses = new ArrayList<>();
            Response[] r = objectMapper.readValue(body, Response[].class);

            for (Response resp : r) {
                if (resp.getId().equals(id)) {
                    responses.add(resp);
                }
            }


            LibGDX.setResponses(responses);
            System.out.println(responses);
        }));

        result = true;
    }

    public boolean isResult() {
        return result;
    }
}
