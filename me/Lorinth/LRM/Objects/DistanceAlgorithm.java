package me.Lorinth.LRM.Objects;

/**
 * Accurate - Uses circular more costly functions to calculate levels
 * Optimized - Uses raw x/z calculations leading to less accurate (diamond shapes)
 */
public enum DistanceAlgorithm {
    Accurate, Optimized
}
