package com.example.calculadora;

import org.junit.Assert;
import org.junit.Test;

public class CalculadoraUnitTest {

    @Test
    public void testOperacionSuma(){
       ICalculadora calculadora = new Calculadora();
       double result = calculadora.sumar(5, 10);
       Assert.assertEquals(15.0, result, 0);
    }

    @Test
    public void testOperacionResta(){
        ICalculadora calculadora = new Calculadora();
        double resultado = calculadora.resta(5, 5);
        Assert.assertEquals(0, resultado, 0);

    }

    @Test
    public void testOperacionMult(){
        ICalculadora calculadora = new Calculadora();
        double resultado = calculadora.multi(5,10);
        Assert.assertEquals(50, resultado, 0);
    }

    @Test
    public void testCalculadoraDiv(){
        ICalculadora calculadora = new Calculadora();
        double resultado = calculadora.div(10,5);
        Assert.assertEquals(2, resultado, 0);
    }

    @Test
    public void testCalculadoraDivEntreCero(){
        ICalculadora calculadora = new Calculadora();
        double resultado = calculadora.div(5,0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, resultado, 0);

    }




}
