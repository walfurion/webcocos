/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.model;

/**
 *
 * @author Alfredo
 */
public class Header {
    String text;
    int start_Cell;
    int end_Cell;

    public Header(String text, int start_Cell, int end_Cell) {
        this.text = text;
        this.start_Cell = start_Cell;
        this.end_Cell = end_Cell;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStart_Cell() {
        return start_Cell;
    }

    public void setStart_Cell(int start_Cell) {
        this.start_Cell = start_Cell;
    }

    public int getEnd_Cell() {
        return end_Cell;
    }

    public void setEnd_Cell(int end_Cell) {
        this.end_Cell = end_Cell;
    }
    
    
}
