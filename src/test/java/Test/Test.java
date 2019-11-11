/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class Test {

    public static void main(String[] args) {
        List<String> cols = new ArrayList<String>();
        cols.add("HOLA1");
        cols.add("HOLA2");
        cols.add("HOLA3");
        cols.add("HOLA4");
        cols.add("HOLA5");
        cols.add("HOLA6");
        
        System.out.println(cols.toArray(new String[0]));
    }

}
