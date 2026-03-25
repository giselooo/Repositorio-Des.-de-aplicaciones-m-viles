package com.example.calculadora;

import org.junit.Assert;
import org.junit.Test;

public class CalculadoraBuilderTest {

    @Test
    public void testBuilder(){

       CalculadoraBuilder builder = new CalculadoraBuilder();

       builder.addOperationInitial(

               new Operacion(5.0, 5.0, OperationType.ADD)

       );

       builder.addOperation(

         new OperacionMin(OperationType.MULTIP, 5.0)

       );

       builder.addOperation(

               new OperacionMin(OperationType.SUBSTRAC, 30.0)

       );

       CalculadoraInput input = builder.build();
        Assert.assertEquals(2, input.operaciones.size());
        Assert.assertEquals(5.0, input.operaciones.get(0).getX(), 0.0);
        Assert.assertEquals(OperationType.MULTIP, input.operaciones.get(0).getOperationType());
        Assert.assertEquals(30.0, input.operaciones.get(1).getX(), 0.0);
        Assert.assertEquals(OperationType.SUBSTRAC, input.operaciones.get(1).getOperationType());
        Assert.assertNotNull(input.operacionInicial);


    }


}
