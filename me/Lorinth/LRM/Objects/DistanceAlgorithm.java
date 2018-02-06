package me.Lorinth.LRM.Objects;

/**
 * Circle - Uses circular more costly functions to calculate levels
 * Diamond - Uses raw x/z calculations leading to less accurate (diamond shapes)
 * Square - Uses zone-esque calculations checking if the spot is within x/z distance
 */
public enum DistanceAlgorithm {
    Circle, Diamond, Square
}
