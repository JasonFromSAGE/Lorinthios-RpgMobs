package me.Lorinth.LRM.Objects;

import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<T>
{
    private final NavigableMap<Double, T> map = new TreeMap();
    private final HashMap<T, Double> chances = new HashMap<>();
    protected final Random random;
    protected double total = 0.0D;

    public RandomCollection()
    {
        this(new Random());
    }

    public RandomCollection(Random random)
    {
        this.random = random;
    }

    public void add(double weight, T result)
    {
        if (weight <= 0.0D) {
            return;
        }
        this.total += weight;
        this.map.put(this.total, result);
        this.chances.put(result, weight);
    }

    public T next()
    {
        double value = this.random.nextDouble() * this.total;
        return this.map.ceilingEntry(value).getValue();
    }

    public HashMap<T, Double> getChances(){
        return chances;
    }

    public double getTotal(){
        return total;
    }
}