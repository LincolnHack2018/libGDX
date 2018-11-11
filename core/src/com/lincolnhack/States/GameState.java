package com.lincolnhack.States;

import lombok.Data;

@Data
public class GameState {

    private int Score;
    private int playerCount;

    public void startNewGame(){

        Score = 0;
        playerCount = 0;

    }

}
