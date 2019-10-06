package me.Lorinth.LRM.Objects;

public class RandomNullCollection<T> extends RandomCollection<T>
{
    public T next()
    {
        if(random.nextDouble() * 100.0 <= total){
            return super.next();
        }

        return null;
    }
}