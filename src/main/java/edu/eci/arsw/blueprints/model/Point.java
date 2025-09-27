/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.model;

/**
 *
 * @author hcadavid
 */
public class Point {
    private int x, y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){ return x; }

    public int getY(){ return y; }

    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
}
