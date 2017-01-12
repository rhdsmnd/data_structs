package com.rhdes.data_structs.graphs;

/** An object representing a function that computes the distance between
 *  two objects of type TYPE.
 *  @author Ron Desmond
 */
public interface Distancer<Type> {

    /** Returns the distance from V0 to V1. */
    double dist(Type v0, Type v1);

}
