package com.example.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GraphicObject
{
    protected String name = "Group";

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public GraphicObject(){}

    public String color;

    public List<GraphicObject> children = new ArrayList<>();

    private void print(StringBuilder stringBuilder, int depth)
    {
        stringBuilder.append(String.join("", Collections.nCopies(depth,"-")))
                .append(depth > 0 ? " " : "")
                .append((color == null || color.isEmpty()) ? "" : color + " ")
                .append(getName())
                .append(System.lineSeparator());
        for (GraphicObject child : children)
            child.print(stringBuilder, depth + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        print(sb, 0);
        return sb.toString();
    }
}

class Circle extends GraphicObject
{
    public Circle(String color)
    {
        name = "Circle";
        this.color = color;
    }
}

class Square extends GraphicObject
{
    public Square(String color)
    {
        name = "square";
        this.color = color;
    }
}

class GeometricShapeDemo
{
    public static void main(String[] args) {
        GraphicObject drawing = new GraphicObject();
        drawing.setName("My Drawing");
        GraphicObject group = new GraphicObject();
        group.children.add(new Circle("Blue"));
        group.children.add(new Square("Blue"));
        drawing.children.add(group);
        drawing.children.add(new Square("Red"));
        drawing.children.add(new Circle("yellow"));



        System.out.println(drawing);
    }
}

/**
 * Summary
 * Composite design pattern is used to treat both single(scalar)
 * and composite objects uniformly
 *
 * 复合模式 当年google电面就是考的这道题
 */
