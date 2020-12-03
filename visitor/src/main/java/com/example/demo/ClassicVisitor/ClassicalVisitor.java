package com.example.demo.ClassicVisitor;

interface ExpressionVisitor
{
    void visit(DoubleExpression e);
    void visit(AdditionExpression e);
}

abstract class Expression
{
    public abstract void accept(ExpressionVisitor visitor);
}

class DoubleExpression extends Expression
{
    public double value;

    public DoubleExpression(double value)
    {
        this.value = value;
    }

    @Override
    public void accept(ExpressionVisitor visitor)
    {
        visitor.visit(this);
    }
}

class AdditionExpression extends Expression
{
    public Expression left, right;

    public AdditionExpression(Expression left, Expression right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(ExpressionVisitor visitor)
    {
        visitor.visit(this);
    }
}

class ExpressionPrinter implements ExpressionVisitor
{
    private StringBuilder sb = new StringBuilder();

    @Override
    public void visit(DoubleExpression e)
    {
        sb.append(e.value);
    }

    @Override
    public void visit(AdditionExpression e)
    {
        sb.append("(");
        e.left.accept(this);
        sb.append("+");
        e.right.accept(this);
        sb.append(")");
    }

    @Override
    public String toString()
    {
        return sb.toString();
    }
}

class ExpressionCalculator implements ExpressionVisitor
{
    public double result;

    @Override
    public void visit(DoubleExpression e)
    {
        result = e.value;
    }

    @Override
    public void visit(AdditionExpression e)
    {
        e.left.accept(this);
        double a = result;
        e.right.accept(this);
        double b = result;
        result = a + b;
    }
}

public class ClassicalVisitor {
    public static void main(String[] args) {
        AdditionExpression e = new AdditionExpression(
                new DoubleExpression(1),
                new AdditionExpression(
                        new DoubleExpression(2),
                        new DoubleExpression(3)
                ));

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);
        System.out.println(ep);

        ExpressionCalculator calc = new ExpressionCalculator();
        calc.visit(e);
        System.out.println(ep + " =" + calc.result);
    }

}
