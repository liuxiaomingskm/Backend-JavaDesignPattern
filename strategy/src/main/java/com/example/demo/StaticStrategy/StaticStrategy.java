package com.example.demo.StaticStrategy;

import org.w3c.dom.Text;

import java.util.List;
import java.util.function.Supplier;

interface ListStrategy
{
    default void start(StringBuilder sb){}

    void addListItem(StringBuilder stringBuilder,String item);

    default void end(StringBuilder sb){}
}

class MarkdownListStrategy implements ListStrategy
{
    @Override
    public void addListItem(StringBuilder stringBuilder, String item)
    {
        stringBuilder.append(" * ").append(item).append(System.lineSeparator());
    }
}

class HtmlListStrategy implements ListStrategy
{
    @Override
    public void addListItem(StringBuilder stringBuilder, String item) {
        stringBuilder.append(" <li>")
                .append(item)
                .append("</li>")
                .append(System.lineSeparator());
    }

    @Override
    public void start(StringBuilder sb) {
        sb.append("<ul>").append(System.lineSeparator());
    }

    @Override
    public void end(StringBuilder sb) {
        sb.append("</ul>").append(System.lineSeparator());
    }
}

class TextProcessor<LS extends ListStrategy>
{
    private StringBuilder sb = new StringBuilder();
    // cannot do this
    // private LS listStrategy = new LS(); // because of Java type erasion, we cannot create instance via new keyword.
    private LS listStrategy;

    public TextProcessor(Supplier<? extends LS> ctor)
    {
        listStrategy = ctor.get();
    }

    // the skeleton algorithm is here
    public void appendList(List<String> items)
    {
        listStrategy.start(sb);
        for (String item : items)
            listStrategy.addListItem(sb, item);
        listStrategy.end(sb);
    }
    public void clear()
    {
        sb.setLength(0);
    }

    @Override
    public String toString()
    {
        return sb.toString();
    }
}

 class DynamicStrategyDemo {
     public static void main(String[] args) {
         TextProcessor<MarkdownListStrategy> tp = new TextProcessor<>(
                 MarkdownListStrategy::new);
         tp.appendList(List.of("liberte", "egalite", "fraternite"));
         System.out.println(tp);

         TextProcessor<HtmlListStrategy> tp2 = new TextProcessor<>(HtmlListStrategy::new);
         tp2.appendList(List.of("inheritance", "encapsulation", "polymorphism"));
         System.out.println(tp2);
     }

}
/**
 * Summary:
 * A strategy pattern essentially allows you to define the different parts of an algorithm that you can call. For example,
 * you have three parts, start, addItems, end and subsequently what you can do is you can have different strategies that plug
 * into a particular object and the base class of the strategy is going to be used regardless so we can replace the interface with
 * any implementation we want.
 *
 *Steps:
 * Define an algorithm at a high level
 * Define the interface you expect each strategy to follow
 * Provide for either dynamic or static composition of strategy in the
 * overall algorithm
 */
