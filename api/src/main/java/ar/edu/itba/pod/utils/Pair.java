package ar.edu.itba.pod.utils;


import lombok.Getter;

public class Pair<T, U> {
    @Getter
    private final T first;
    @Getter
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
}
