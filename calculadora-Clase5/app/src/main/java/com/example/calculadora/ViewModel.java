package com.example.calculadora;
import java.util.ArrayList;
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



    public String processStringInput(String input) {
        if (input == null || input.isEmpty()) return "0";


        if (input.contains("0/0")) return "Indefinido";

        try {

            String[] tokens = input.split("(?<=[-+X/])|(?=[-+X/])");

            ArrayList<Double> nums = new ArrayList<>();
            ArrayList<String> ops = new ArrayList<>();

            for (String t : tokens) {
                if (t.matches("[0-9.]+")) nums.add(Double.parseDouble(t));
                else ops.add(t);
            }


            for (int i = 0; i < ops.size(); i++) {
                String op = ops.get(i);
                if (op.equals("X") || op.equals("/")) {
                    double r = op.equals("X")
                            ? calculadora.multi(nums.get(i), nums.get(i+1))
                            : calculadora.div(nums.get(i), nums.get(i+1));

                    nums.set(i, r);
                    nums.remove(i + 1);
                    ops.remove(i);
                    i--;
                }
            }


            double total = nums.get(0);
            for (int i = 0; i < ops.size(); i++) {
                if (ops.get(i).equals("+")) total = calculadora.sumar(total, nums.get(i+1));
                if (ops.get(i).equals("-")) total = calculadora.resta(total, nums.get(i+1));
            }

            return String.valueOf(total);

        } catch (Exception e) {
            return "...";
        }
    }


}
