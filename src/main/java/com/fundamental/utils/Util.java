/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

/**
 *
 * @author Mery
 */
public class Util {
    public static void main(String ars[]){
        double valor1 = 100.5665888;
        double d = 100;
        System.out.println("formateado = "+Math.ceil(valor1));
        double valor2 = valor1*d;
        System.out.println("formateado = "+valor2);
        System.out.println("formateado = "+Math.ceil(valor2));
        System.out.println("formateado = "+(Math.ceil(valor2))/d);
        
    }
    public static double toDoubleFormat(double n){
            double newD=0;
             double valor1 = 100.5665888;
            double d = 100;
//            System.out.println("formateado = "+Math.ceil(valor1));
            double valor2 = valor1*d;
//            System.out.println("formateado = "+valor2);
//            System.out.println("formateado = "+Math.ceil(valor2));
//            System.out.println("formateado = "+(Math.ceil(valor2))/d);
            valor2 = (Math.ceil(valor2))/d;
            return valor2;
    }
    
}
