package com.example.intentos;

import java.math.BigInteger;

public class Fibonacci {

    public String calcular(int n) {
        if (n < 0) return "0";
        if (n == 0) return "0";
        if (n == 1) return "1";

        BigInteger a = BigInteger.ZERO; // F(0)
        BigInteger b = BigInteger.ONE;  // F(1)

        for (int i = 2; i <= n; i++) {
            BigInteger temp = a.add(b);
            a = b;
            b = temp;
        }
        return b.toString();
    }
}