package com.adopet;

public interface Mapper<S, T> {

    T map(S source);
}
