package com.rhdes.data_structs.graphs;

import java.util.List;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Comparator;

/** Assorted graph algorithms.
 *  @author Ron Desmond
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 final Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h,
                 final Weighter<? super VLabel> vweighter,
                 final Weighting<? super ELabel> eweighter) {
        HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> parents = new
            HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge>();
        for (Graph<VLabel, ELabel>.Vertex v: G.vertices()) {
            vweighter.setWeight(v.getLabel(), Double.POSITIVE_INFINITY);
        }
        vweighter.setWeight(V0.getLabel(), 0);
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> fringe
            = new PriorityQueue<Graph<VLabel, ELabel>
            .Vertex>(G.vertexSize(), new Comparator<Graph<VLabel,
                     ELabel>.Vertex>() {
                    public int compare(Graph<VLabel, ELabel>.Vertex v1,
                                            Graph<VLabel, ELabel>.Vertex v2) {
                        return Double.compare(vweighter
                                                   .weight(v1.getLabel())
                                                   + h.dist(v1.getLabel(),
                                                            V1.getLabel()),
                                                   vweighter
                                                   .weight(v2.getLabel())
                                                   + h.dist(v2.getLabel(),
                                                            V1.getLabel()));
                    }
                });
        traverse(G, V0, V1, h, vweighter, eweighter, fringe, parents);
        if (vweighter.weight(V1.getLabel()) == Double.POSITIVE_INFINITY) {
            return null;
        } else {
            return getPath(parents, V0, V1);
        }
    }
    /** Traverses G finding a path from V0 to V1, updating it to a
     *  hashmap PARENTS, using H, VWEIGHTER, and EWEIGHTER to compare
     *  vertices according to their type VLABEL and ELABEL, then
     *  putting them into the FRINGE. */
    private static <VLabel, ELabel> void traverse(Graph<VLabel, ELabel> G,
                                 Graph<VLabel, ELabel>.Vertex V0,
                                 Graph<VLabel, ELabel>.Vertex V1,
                                 final Distancer<? super VLabel> h,
                                 final Weighter<? super VLabel> vweighter,
                                 final Weighting<? super ELabel> eweighter,
                                 PriorityQueue<Graph<VLabel,
                                 ELabel>.Vertex> fringe,
                                 HashMap<Graph<VLabel, ELabel>.Vertex,
                                 Graph<VLabel, ELabel>.Edge> parents) {
        for (Graph<VLabel, ELabel>.Vertex v: G.vertices()) {
            fringe.add(v);
        }
        while (fringe.size() > 0) {
            Graph<VLabel, ELabel>.Vertex v = fringe.poll();
            if (v == V1) {
                break;
            } else {
                for (Graph<VLabel, ELabel>.Edge e
                         : G.outEdges(v)) {
                    double pathWeight = vweighter.weight(v.getLabel())
                        + eweighter.weight(e.getLabel());
                    Graph<VLabel, ELabel>.Vertex child = e.getV(v);
                    if (vweighter.weight(child.getLabel())
                        > pathWeight) {
                        boolean isIn = false;
                        if (fringe.contains(child)) {
                            fringe.remove(child);
                            isIn = true;
                        }
                        vweighter.setWeight(child.getLabel(), pathWeight);
                        parents.put(child, e);
                        fringe.add(child);
                    }
                }
            }
        }
    }


    /** Returns the path from CURRENT navigating iteratively with PARENTS
     *  using types VLABEL and ELABEL beginning from START to END. */
    private static
        <VLabel, ELabel>
    List<Graph<VLabel, ELabel>.Edge>
    getPath(HashMap
            <Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> parents,
            Graph<VLabel, ELabel>.Vertex start,
            Graph<VLabel, ELabel>.Vertex end) {
        LinkedList<Graph<VLabel, ELabel>.Edge> ret =
            new LinkedList<Graph<VLabel, ELabel>.Edge>();
        Graph<VLabel, ELabel>.Vertex current = end;
        while (parents.get(current).getV(current) != start) {
            ret.addFirst(parents.get(current));
            current = parents.get(current).getV(current);
        }
        ret.addFirst(parents.get(current));
        return ret;
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h) {
        return shortestPath(G, V0, V1, h, new Weighter<VLabel>() {
                public void setWeight(VLabel l, double v) {
                    l.setWeight(v);
                }
                public double weight(VLabel l) {
                    return l.weight();
                }
            }, new Weighter<ELabel>() {
                public double weight(ELabel l) {
                    return l.weight();
                }
                public void setWeight(ELabel l, double v) {
                    return;
                }
            });
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };
}
