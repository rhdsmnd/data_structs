package com.rhdes.data_structs.graphs;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make methods in Graph abstract, if you want
 * different implementations in DirectedGraph and UndirectedGraph.  You may
 * add bodies to abstract methods, modify existing bodies, or override
 * inherited methods. */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 1;2c*  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Ron Desmond
 */
public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The label of the vertex. */
        private final VLabel _label;
    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v.equals(_v0)) {
                return _v1;
            } else if (v.equals(_v1)) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return _verts.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return _edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        return _outEdges.get(v).size();
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        return _inEdges.get(v).size();
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        for (Edge e: _outEdges.get(u)) {
            if (e.getV1() == v) {
                return true;
            }
        }
        return false;
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        for (Edge e: _outEdges.get(u)) {
            if (e.getV1() == v && label == e.getLabel()) {
                return true;
            }
        }
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex ret = new Vertex(label);
        _verts.add(ret);
        _outEdges.put(ret, new LinkedList<Edge>());
        _inEdges.put(ret, new LinkedList<Edge>());
        return ret;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        Edge ret = new Edge(from, to, label);
        _edges.add(ret);
        _outEdges.get(from).add(ret);
        _inEdges.get(to).add(ret);
        return ret;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        return add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        LinkedList<Edge> iter
            = new LinkedList<Edge>(_outEdges.get(v));
        iter.addAll(_inEdges.get(v));
        while (iter.size() > 0) {
            remove(iter.get(0));
            iter.remove();
        }
        _outEdges.remove(v);
        _inEdges.remove(v);
        _verts.remove(v);
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        try {
            _edges.remove(e);
            _outEdges.get(e.getV0()).remove(e);
            _inEdges.get(e.getV1()).remove(e);
        } catch (IllegalArgumentException exc) {
            int unused = 0;
        }
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        for (Edge e: _outEdges.get(v1)) {
            if (e.getV1() == v2) {
                try {
                    remove(e);
                } catch (IllegalArgumentException exc) {
                    continue;
                }
            }
        }
        for (Edge e: _outEdges.get(v2)) {
            if (e.getV1() == v1) {
                remove(e);
            }
        }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        return Iteration.iteration(_verts);
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        LinkedList<Vertex> getVerts = new LinkedList<Vertex>();
        for (Edge e: _outEdges.get(v)) {
            getVerts.add(e.getV(v));
        }
        return Iteration.iteration(getVerts);
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        LinkedList<Vertex> predecessorsSet = new LinkedList<Vertex>();
        for (Edge e: _inEdges.get(v)) {
            predecessorsSet.add(e.getV(v));
        }
        return Iteration.iteration(predecessorsSet);
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return Iteration.iteration(_edges);
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        return Iteration.iteration(_outEdges.get(v));
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        return Iteration.iteration(_inEdges.get(v));
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if stringComp = Graph.<Integer>naturalOrder(), then
     *  stringComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    /** Cause subsequent traversals and calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        final Comparator<ELabel> comp = comparator;
        Collections.sort(_edges, new Comparator<Edge>() {
                public int compare(Edge e1, Edge e2) {
                    return comp.compare(e1.getLabel(), e2.getLabel());
                }
            });
        for (LinkedList<Edge> outEdges: _outEdges.values()) {
            Collections.sort(outEdges, new Comparator<Edge>() {
                public int compare(Edge e1, Edge e2) {
                    return comp.compare(e1.getLabel(), e2.getLabel());
                }
            });
        }
        for (LinkedList<Edge> inEdges: _inEdges.values()) {
            Collections.sort(inEdges, new Comparator<Edge>() {
                public int compare(Edge e1, Edge e2) {
                    return comp.compare(e1.getLabel(), e2.getLabel());
                }
            });
        }
    }

    /** Holds all the vertices in this graph. */
    private HashSet<Vertex> _verts = new HashSet<Vertex>();
    /** Maps every vertex to a list of outEdges. */
    private HashMap<Vertex, LinkedList<Edge>> _outEdges =
        new HashMap<Vertex, LinkedList<Edge>>();
    /** Maps every vertex to a list of inEdges. */
    private HashMap<Vertex, LinkedList<Edge>> _inEdges =
        new HashMap<Vertex, LinkedList<Edge>>();
    /** Holds all the edges in this graph. */
    private LinkedList<Edge> _edges = new LinkedList<Edge>();
}
