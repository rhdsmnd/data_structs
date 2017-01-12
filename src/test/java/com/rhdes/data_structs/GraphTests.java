package com.rhdes.data_structs;

import org.junit.Test;

import com.rhdes.data_structs.graphs.DirectedGraph;
import com.rhdes.data_structs.graphs.Distancer;
import com.rhdes.data_structs.graphs.Graph;
import com.rhdes.data_structs.graphs.Graphs;
import com.rhdes.data_structs.graphs.Traversal;
import com.rhdes.data_structs.graphs.UndirectedGraph;
import com.rhdes.data_structs.graphs.Weighter;
import com.rhdes.data_structs.graphs.Weighting;
import com.rhdes.data_structs.graphs.Graph.Edge;
import com.rhdes.data_structs.graphs.Graph.Vertex;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Comparator;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package. */
public class GraphTests {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void sizeTest() {
        UndirectedGraph<String, Integer> g =
            new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex v1 = g.add("v1");
        Graph<String, Integer>.Vertex v2 = g.add("v1");
        Graph<String, Integer>.Vertex v3 = g.add("v1");
        Graph<String, Integer>.Vertex v4 = g.add("v1");
        Graph<String, Integer>.Edge e1 = g.add(v1, v2);
        Graph<String, Integer>.Edge e2 = g.add(v2, v3);
        Graph<String, Integer>.Edge e3 = g.add(v3, v4);
        Graph<String, Integer>.Edge e4 = g.add(v4, v1);
        Graph<String, Integer>.Edge e5 = g.add(v1, v3);
        assertEquals("Vertices does not equal 4",
                     4, g.vertexSize());
        assertEquals("Edges does not equal 5", 5, g.edgeSize());
    }

    @Test
    public void degreeTest() {
        UndirectedGraph<Integer, Integer> g =
            new UndirectedGraph<Integer, Integer>();
        Graph<Integer, Integer>.Vertex v1 = g.add(5);
        Graph<Integer, Integer>.Vertex v2 = g.add(0);
        Graph<Integer, Integer>.Vertex v3 = g.add(10);
        Graph<Integer, Integer>.Vertex v4 = g.add(12);
        Graph<Integer, Integer>.Vertex v5 = g.add(3);
        Graph<Integer, Integer>.Vertex v6 = g.add(4);
        Graph<Integer, Integer>.Edge e1 = g.add(v1, v2);
        Graph<Integer, Integer>.Edge e2 = g.add(v2, v1);
        Graph<Integer, Integer>.Edge e3 = g.add(v1, v2);
        Graph<Integer, Integer>.Edge e4 = g.add(v3, v2);
        Graph<Integer, Integer>.Edge e5 = g.add(v2, v2);
        Graph<Integer, Integer>.Edge e6 = g.add(v1, v3);
        assertEquals("Degree for v1 doesn't equal 4.", 4, g.degree(v1));
        assertEquals("Degree for v2 doesn't equal 6.", 6, g.degree(v2));
        assertEquals("Degree doesn't equal 0.", 0, g.degree(v6));
    }

    /** Creates a testTraversal. */
    private class PrintTraversal<VLabel, ELabel> extends
                                                     Traversal<VLabel, ELabel> {
        /** Creates a traversal from graph G, starting from V, in ORDER
         *  according to the comparator. */
        public void traverse(Graph<VLabel, ELabel> G,
                             Graph<VLabel, ELabel>.Vertex v,
                             Comparator<VLabel> order) {
            testString = "";
            super.traverse(G, v, order);
            testString = testString.trim();
        }

        /** Traverse the graph G from vertex V. */
        public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
            testString = "";
            super.breadthFirstTraverse(G, v);
            testString = testString.trim();
        }

        /** Traverse the graph G from vertex V. */
        public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
            testString = "";
            super.depthFirstTraverse(G, v);
            testString = testString.trim();
        }

        @Override
        protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                                Graph<VLabel, ELabel>.Vertex v0) {
            testString += "pr" + e.getV(v0)
                + " ";
        }
        @Override
        protected void visit(Graph<VLabel, ELabel>.Vertex v) {
            testString += "v" + v + " ";
        }
        @Override
        protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
            testString += "pv" + v + " ";
        }
        /** Returns the representation of this traversal. */
        String getString() {
            return testString;
        }
        /** The string that traversals build. */
        String testString = "";
    }

    @Test
    public void testTraversal() {
        UndirectedGraph<String, Integer> g =
            new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex v1 = g.add("A");
        Graph<String, Integer>.Vertex v2 = g.add("B");
        Graph<String, Integer>.Vertex v3 = g.add("C");
        Graph<String, Integer>.Vertex v4 = g.add("D");
        Graph<String, Integer>.Vertex v5 = g.add("E");
        Graph<String, Integer>.Vertex v6 = g.add("F");
        Graph<String, Integer>.Edge e1 = g.add(v1, v2);
        Graph<String, Integer>.Edge e2 = g.add(v1, v3);
        Graph<String, Integer>.Edge e3 = g.add(v1, v4);
        Graph<String, Integer>.Edge e4 = g.add(v4, v5);
        Graph<String, Integer>.Edge e5 = g.add(v4, v6);
        PrintTraversal<String, Integer> t =
            new PrintTraversal<String, Integer>();
        t.depthFirstTraverse(g, v1);
        assert (t.getString().equals("vA prB prC prD vB pvB vC pvC vD"
                                    + " prE prF vE pvE vF pvF pvD pvA"));
        t.breadthFirstTraverse(g, v1);
        assert (t.getString().equals("vA prB prC prD vB vC vD prE prF"
                                    + " pvA pvB pvC vE vF pvD pvE pvF"));
        t.traverse(g, v1, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    return ((Character) s1.charAt(0))
                        .compareTo(((Character) s2.charAt(0)));
                }
            });
        assert (t.getString().equals("vA prB prC prD vB vC vD prE prF"
                                    + " vE vF"));
    }
    @Test
    public void testShortestPath() {
        UndirectedGraph<String, Integer> g =
            new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex v1 = g.add("D");
        Graph<String, Integer>.Vertex v2 = g.add("C");
        Graph<String, Integer>.Vertex v3 = g.add("B");
        Graph<String, Integer>.Vertex v4 = g.add("A");
        Graph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        Graph<String, Integer>.Edge e2 = g.add(v1, v3, 1);
        Graph<String, Integer>.Edge e3 = g.add(v2, v4, 1);
        Graph<String, Integer>.Edge e4 = g.add(v3, v4, 2);
        Distancer<String> heur = new Distancer<String>() {
            public double dist(String s1, String s2) {
                return (double) (s1.charAt(0) - s2.charAt(0));
            }
        };
        Weighter<String> vweighter = new Weighter
            <String>() {
            public void setWeight(String v, double d) {
                if (_weights.containsKey(v)) {
                    _weights.remove(v);
                }
                _weights.put(v, d);
            }
            public double weight(String v) {
                return _weights.get(v);
            }
            private HashMap<String, Double> _weights =
                new HashMap<String, Double>();
        };
        Weighting<Integer> eweighter = new Weighting<Integer>() {
            public double weight(Integer x) {
                return (double) x;
            }
        };
        List<Graph<String, Integer>.Edge> path =
            Graphs.shortestPath(g, v1, v4, heur, vweighter, eweighter);
        String tester = "";
        tester += path.get(0).getV0() + " ";
        for (Graph<String, Integer>.Edge e: path) {
            tester += e.getV1() + " ";
        }
        assertEquals(tester, "D C A ");
    }

}
