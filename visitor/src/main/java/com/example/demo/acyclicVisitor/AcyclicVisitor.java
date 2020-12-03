package com.example.demo.acyclicVisitor;

// really creepy implementation of acyclic visitor
import java.util.ArrayList;
import java.util.List;

interface Visitor{}

interface ExpressionVisitor extends Visitor{
    void visit(Expression obj);
}

interface DoubleExpressionVisitor extends  Visitor{
    void visit(DoubleExpression obj);
}

interface  AdditionExpressionVisitor extends Visitor{
    void visit(AdditionExpression obj);
}

abstract class Expression
{
    // optional
    public void accept(Visitor visitor)
    {
        if( visitor instanceof ExpressionVisitor){
            ((ExpressionVisitor) visitor).visit(this);
        }
    }
}

class DoubleExpression extends Expression
{
    public double value;

    public DoubleExpression(double value){
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor){
        if (visitor instanceof DoubleExpressionVisitor){
            ((DoubleExpressionVisitor) visitor).visit(this);
        }
    }
}

class AdditionExpression extends Expression
{
    public Expression left, right;

    public AdditionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof AdditionExpressionVisitor) {
            ((AdditionExpressionVisitor) visitor).visit(this);
        }
    }
}

class ExpressionPrinter implements DoubleExpressionVisitor, AdditionExpressionVisitor
{
    private StringBuilder sb = new StringBuilder();

    @Override
    public void visit(DoubleExpression obj){
        sb.append(obj.value);
    }

    @Override
    public void visit(AdditionExpression obj) {
        sb.append('(');
        obj.left.accept(this);
        sb.append('+');
        obj.right.accept(this);
        sb.append(')');
    }

    @Override
    public String toString(){
        return sb.toString();
    }
}

public class AcyclicVisitor {
    public static void main(String[] args) {
        AdditionExpression e = new AdditionExpression(
                new DoubleExpression(1),
                new AdditionExpression(
                        new DoubleExpression(2),
                        new DoubleExpression(3)
                )
        );

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);
        System.out.println(ep.toString());
    }
}

/**
 * 大致思路是每个特定expression都有一个特定的visitor，如果不是特定visitor就不执行 visitor.visit(this)，所以先看一下expression里面有多少个
 * 实现类，然后Visitor继承相应实现类对应的visitor接口，override所有visit方法 就能遍历这个expression中所有的expression的子类了
 */
