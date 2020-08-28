package com.finch.app;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ShapeValue extends Value {
    public List<List<Double>> points;
    public List<List<Integer>> lines;
    public List<List<Integer>> faces;
    public List<String> curves;
    public List<String> planes;
    public List<Integer> curveAlign;
    public List<Integer> planeAlign;
    //The align variables are used to map particular curves/planes to particular lines/faces

    ShapeValue() {
        this.points = new ArrayList<List<Double>>();
        this.lines = new ArrayList<List<Integer>>();
        this.faces = new ArrayList<List<Integer>>();
        this.curves = new ArrayList<String>();
        this.planes = new ArrayList<String>();
        this.curveAlign = new ArrayList<Integer>();
        this.planeAlign = new ArrayList<Integer>();
    }

    public ShapeValue addPoint(double x, double y, double z) {
        List<Double> temp = new ArrayList<Double>();
        temp.add(x);
        temp.add(y);
        temp.add(z);
        this.points.add(temp);
        return this;
    }

    public ShapeValue addLine(int a, int b) {
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(a);
        temp.add(b);
        this.lines.add(temp);
        return this;
    }

    public ShapeValue addFace(List<Integer> face) {
        this.faces.add(face);
        return this;
    }

    public ShapeValue addCurve(String formula, int align) {
        this.curves.add(formula);
        this.curveAlign.add(align);
        return this;
    }

    public ShapeValue addPlane(String formula, int align) {
        this.planes.add(formula);
        this.planeAlign.add(align);
        return this;
    }
}