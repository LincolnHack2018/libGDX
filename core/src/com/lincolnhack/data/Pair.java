package com.lincolnhack.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T> {

    T first;
    T second;
}
