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

    public static void main(String ars[]) {
        double valor1 = 100.5665888;
        double d = 100;
        System.out.println("dd " + isDoublePositive("0.0"));
//        System.out.println("formateado = "+Math.ceil(valor1));
//        double valor2 = valor1*d;
//        System.out.println("formateado = "+valor2);
//        System.out.println("formateado = "+Math.ceil(valor2));
//        System.out.println("formateado = "+(Math.ceil(valor2))/d);

    }

    public static double toDoubleFormat(double n) {
        double newD = 0;
        double valor1 = 100.5665888;
        double d = 100;
//            System.out.println("formateado = "+Math.ceil(valor1));
        double valor2 = valor1 * d;
//            System.out.println("formateado = "+valor2);
//            System.out.println("formateado = "+Math.ceil(valor2));
//            System.out.println("formateado = "+(Math.ceil(valor2))/d);
        valor2 = (Math.ceil(valor2)) / d;
        return valor2;
    }
    
     public static boolean isNumberRegex(String s) {
        
        if (s.matches("^-?\\d*\\.{0,1}\\d+$")) {
            return true;
        }
        return false;
    }

    public static double isNumber(String s) {
        double newD = 0;
        if (s.matches("\\d+")) {
            newD = Double.parseDouble(s);
        }
        return newD;
    }
    
     public static boolean isPositiveRegex(String s) {
        try {
            if (s.matches("^\\d*\\.?\\d+$")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isDoublePositive(String s) {
        try {
            double n = Double.parseDouble(s);
            if (n > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPositive(double n) {
        return n > 0 ? true : false;
    }

    public static double isNumberEst(String s) {
        double newD = -1;
        if (s.matches("\\d+")) {
            newD = Double.parseDouble(s);
        }
        return newD;
    }
}
