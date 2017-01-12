package com.rhdes.data_structs.graphs;

import java.util.LinkedList;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** An undirected graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Ron Desmond
 */
public class UndirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public UndirectedGraph() {
    }

    @Override
    public boolean isDirected() {
        return false;
    }
    @Override
    public int outDegree(Vertex v) {
        return super.outDegree(v) + super.inDegree(v);
    }
    @Override
    public boolean contains(Vertex u, Vertex v) {
        return super.contains(u, v)
            || super.contains(v, u);
    }
    @Override
    public boolean contains(Vertex u, Vertex v, ELabel label) {
        return super.contains(u, v, label)
            || super.contains(v, u, label);
    }

    @Override
    public void remove(Vertex v) {
        LinkedList<Edge> iter
            = new LinkedList<Edge>();
        for (Edge e: outEdges(v)) {
            iter.add(e);
        }
        while (iter.size() > 0) {
            remove(iter.get(0));
            iter.remove();
        }
        super.remove(v);
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        LinkedList<Vertex> succs = new LinkedList<Vertex>();
        for (Vertex buildSucc: super.successors(v)) {
            succs.add(buildSucc);
        }
        for (Vertex buildPred: super.predecessors(v)) {
            succs.add(buildPred);
        }
        return Iteration.iteration(succs);
    }

    @Override
    public Iteration<Vertex> predecessors(Vertex v) {
        return successors(v);
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        LinkedList<Edge> out = new LinkedList<Edge>();
        for (Edge e: super.outEdges(v)) {
            out.add(e);
        }
        for (Edge e: super.inEdges(v)) {
            out.add(e);
        }
        return Iteration.iteration(out);
    }
    @Override
    public Iteration<Edge> inEdges(Vertex v) {
        return outEdges(v);
    }
}
