/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.sample.benchmark.josephus;

/**
 */
public class Chain
{
    private Person first = null;
    static long _lc = 0;

    public Chain(int size)
    {
        Person last = null;
        Person current = null;
        //for (int i = 0 ; i < size ; i++)
        int i = 0;
        while( i < size )
        {
            current = new Person(i);
            if (first == null) first = current;
            if (last != null)
            {
                last.setNext(current);
                current.setPrev(last);
            }
            last = current;
            i++;
        }
        first.setPrev(last);
        last.setNext(first);
    }

    public Person kill(int nth)
    {
        Person current = first;
        int shout = 1;
        while( (current.getNext() == current) == false )
        {
            shout = current.shout(shout, nth);
            current = current.getNext();
            _lc++;
        }
        first = current;
        return current;
    }

    public Person getFirst()
    {
        return first;
    }
    public static void main(String[] args)
    {
        int ITER = 100000;
        long start = System.nanoTime();
        int i = 0;
        while(i < ITER)
        {
            Chain chain = new Chain(40);
            chain.kill(3);
            i++;
        }
        System.out.println( _lc );
        long end = System.nanoTime();
        System.out.println("Time per iteration = " + ((end - start) / (ITER )) + " nanoseconds.");
    }
}