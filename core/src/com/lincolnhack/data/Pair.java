package com.lincolnhack.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T> implements Serializable{

    T first;
    T second;

    Pair(){

    }
}
