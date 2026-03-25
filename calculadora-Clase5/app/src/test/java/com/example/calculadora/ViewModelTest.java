package com.example.calculadora;

import org.junit.Assert;
import org.junit.Test;

public class ViewModelTest {

    @Test
    public void testMakeOperationSuma(){
        ViewModel viewModel = new ViewModel();
        Operacion operacion = new Operacion(5.0, 5.0, OperationType.ADD);
        Double resultado = viewModel.makeOperation(operacion);
        Assert.assertEquals(10.0, resultado, 0);
    }

    @Test
    public void testMakeOperationMinus(){
        ViewModel viewModel = new ViewModel();
        Operacion operacion = new Operacion(5.0, 5.0, OperationType.SUBSTRAC);
        Double resultado = viewModel.makeOperation(operacion);
        Assert.assertEquals(0.0, resultado, 0);
    }

    @Test
    public void testMakeOperationMulti(){
        ViewModel viewModel = new ViewModel();
        Operacion operacion = new Operacion(5.0, 5.0, OperationType.MULTIP);
        Double resultado = viewModel.makeOperation(operacion);
        Assert.assertEquals(25.0, resultado, 0);
    }

    @Test
    public void testMakeOperationDiv(){
        ViewModel viewModel = new ViewModel();
        Operacion operacion = new Operacion(5.0, 5.0, OperationType.DIV);
        Double resultado = viewModel.makeOperation(operacion);
        //Assert.assertNotEquals(resultado);
        Assert.assertEquals(1.0, resultado, 0);
    }

    @Test
    public void testMakeOperationNegative(){
        ViewModel viewModel = new ViewModel();
        Operacion operacion1 = new Operacion(-5.0, -5.0, OperationType.ADD);
        Operacion operacion = new Operacion(-5.0, -5.0, OperationType.ADD);
        Double resultado = viewModel.makeOperation(new Operacion[]{operacion1, operacion});
        //Assert.assertNotEquals(resultado);
        Assert.assertEquals(-20.0, resultado, 0);
    }

}
