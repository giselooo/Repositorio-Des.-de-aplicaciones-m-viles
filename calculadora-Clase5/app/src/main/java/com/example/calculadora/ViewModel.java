package com.example.calculadora;

public class ViewModel {

    ICalculadora calculadora = new Calculadora();

    ViewModel(){


    }

    Double makeOperation(Operacion[] operacions){

        Double cache = 0.0;

        for(Operacion operacion: operacions){

           cache += makeOperation(operacion);

        }

        return cache;
    }

    Double makeOperation(Operacion operacion){

        switch (operacion.getOperationType()){

            //ADD, SUBSTRAC, MULTIP, DIV

            case ADD:
                return calculadora.sumar(operacion.x, operacion.y);

            case SUBSTRAC:
                return  calculadora.resta(operacion.x, operacion.y);

            case MULTIP:
                return  calculadora.multi(operacion.x, operacion.y);

            case DIV:
                return calculadora.div(operacion.x, operacion.y);

            default:
                return 0.0;

        }

    }


}
