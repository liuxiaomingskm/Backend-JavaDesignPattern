package com.example.demo;

import java.io.ObjectInputStream;
import java.util.*;
import java.util.function.Consumer;

class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString()
    {
        return "Point{" +
                "first=" + x +
                ", second=" + y +
                '}';
    }
}

class Line {
    public Point start, end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (!start.equals(line.start)) return false;
        return end.equals(line.end);
    }

    @Override
    public int hashCode()
    {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }
}

class VectorObject extends ArrayList<Line> {}

class VectorRectangle extends VectorObject
{
    public VectorRectangle(int x, int y, int width, int height)
    {
        add(new Line(new Point(x,y), new Point(x+width, y) ));
        add(new Line(new Point(x+width,y), new Point(x+width, y+height) ));
        add(new Line(new Point(x,y), new Point(x, y+height) ));
        add(new Line(new Point(x,y+height), new Point(x+width, y+height) ));
    }
}

class LineToPointAdapter implements Iterable<Point>
{
    private static int count = 0;
    private static Map<Integer, List<Point>> cache = new HashMap<>();
    private int hash;

    public LineToPointerAdapter(Line line)
    {
        hash = line.hashCode();
        if (cache.get(hash) != null) return; // we already draw it and have it.

        System.out.println(
                String.format("%d: Generating points for line [%d,%d]-[%d,%d] (no caching)",
                        ++count, line.start.x, line.start.y, line.end.x, line.end.y));

        ArrayList<Point> points = new ArrayList<>();

        int left = Math.min(line.start.x, line.end.x);
        int right = Math.max(line.start.x, line.end.x);
        int top = Math.min(line.start.y, line.end.y);
        int bottom = Math.max(line.start.y, line.end.y);
        int dx = right - left;
        int dy = line.end.y - line.start.y;

        if (dx == 0)
        {
            for (int y = top; y <= bottom; ++y)
            {
                points.add(new Point(left, y));
            }
        }
        else if (dy == 0)
        {
            for (int x = left; x <= right; ++x)
            {
                points.add(new Point(x, top));
            }
        }

        cache.put(hash, points);
    }

    @Override
    public Iterator<Point> iterator()
    {
        return cache.get(hash).iterator();
    }

    @Override
    public void forEach(Consumer<? super Point> action)
    {
        cache.get(hash).forEach(action);
    }

    @Override
    public Spliterator<Point> spliterator()
    {
        return cache.get(hash).spliterator();
    }
    }

    class AdapterDemo
    {
        private static final List<VectorObject> vectorObjects =
                new ArrayList<>(Arrays.asList(
                        new VectorRectangle(1,1,10,10),
                        new VectorRectangle(3,3,6,6)
                ));

        public static void drawPoint(Point p)
        {
            System.out.print(".");
        }

        private static void draw()
        {
            for (VectorObject vo : vectorObjects)
            {
                for (Line line : vo)
                {
                    LineToPointAdapter adapter = new LineToPointAdapter(line);
                    adapter.forEach(point -> drawPoint(point));
                }
            }
        }

        public static void main(String[] args)
        {
            draw();
            draw();
        }
    }

/**
 * Summary:
 * 学习了使用cache来暂时保存已经使用过的数据，从而避免画相同的线段，学习了如何实现iterable接口，通常实现iterable接口还需要实现iterator接口
 * 但是这里因为容器是List<Point> 已经实现了iterator接口 所以不用再次实现
 * 正常自定义iterable和iterator接口参考代码如下：
 *
 * class ReverseList<T> implements Iterable<T> {
 *     private List<T> list = new ArrayList<>();
 *
 *     public void add(T t) {
 *         list.add(t);
 *     }
 *
 * @Override
 * public Iterator<T> iterator(){
 *     return new ReverseIterator(list.size());
 * }
 *
 * class ReverseIterator implements Iterator<T> {
 *     int index;
 *
 *     ReverseIterator(int index) this.index = index;
 *
 * @Override
 * public boolean hasNext(){ return index > 0 }
 *
 * @Override
 * public T next(){
 *     index--;
 *     return ReverseList.this.list.get(index);  // 又是一个值得学习的写法
 * }
 * }
 * }
 */
