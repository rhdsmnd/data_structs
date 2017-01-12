package com.rhdes.data_structs.graphs;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.LinkedList;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular set of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visiting it, and then adding its untraversed
 *  successors to the fringe.  The client can dictate an ordering on
 *  the fringe, determining which item is next removed, by which kind
 *  of traversal is requested.
 *     + A depth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at one end.  It also revisits the node
 *       itself after traversing all successors by calling the
 *       postVisit method on it.
 *     + A breadth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at different ends.  It also revisits the node
 *       itself after traversing all successors as for depth-first
 *       traversals.
 *     + A general traversal treats the fringe as an ordered set, as
 *       determined by a Comparator argument.  There is no postVisit
 *       for this type of traversal.
 *  As vertices are added to the fringe, the traversal calls a
 *  preVisit method on the vertex.
 *
 *  Generally, the client will extend Traversal, overriding the visit,
 *  preVisit, and postVisit methods, as desired (by default, they do nothing).
 *  Any of these methods may throw StopException to halt the traversal
 *  (temporarily, if desired).  The preVisit method may throw a
 *  RejectException to prevent a vertex from being added to the
 *  fringe, and the visit method may throw a RejectException to
 *  prevent its successors from being added to the fringe.
 *  @author Ron Desmond
 */
public class Traversal<VLabel, ELabel> {

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        _graph = G;
        _finalVertex = null;
        _marked = new HashSet<Graph<VLabel, ELabel>.Vertex>();
        final Comparator<VLabel> finalOrder = order;
        _comparator = new Comparator<Graph<VLabel, ELabel>.Vertex>() {
            public int compare(Graph<VLabel, ELabel>.Vertex v1,
                               Graph<VLabel, ELabel>.Vertex v2) {
                return finalOrder.compare(v1.getLabel(), v2.getLabel());
            }
        };
        _curTraversal = "gt";
        traverseHelper(v);
    }

    /** Takes in V and traverses from there. */
    private void traverseHelper(Graph<VLabel, ELabel>.Vertex v) {
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> fringe =
            new PriorityQueue<Graph<VLabel,
            ELabel>.Vertex>(_graph.vertexSize(), _comparator);
        fringe.add(v);
        while (fringe.size() > 0) {
            Graph<VLabel, ELabel>.Vertex curV = fringe.poll();
            try {
                if (!_marked.contains(curV)) {
                    _marked.add(curV);
                    visit(curV);
                    for (Graph<VLabel, ELabel>.Edge e: _graph.outEdges(curV)) {
                        if (!_marked.contains(e.getV(curV))) {
                            preVisit(e, curV);
                            fringe.add(e.getV(curV));
                        }
                    }
                }
            } catch (StopException e) {
                _finalVertex = curV;
                return;
            }
        }
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        _graph = G;
        _marked = new HashSet<Graph<VLabel, ELabel>.Vertex>();
        _curTraversal = "dft";
        _finalVertex = null;
        try {
            depthHelper(G, v, _marked, false);
        } catch (StopException e) {
            return;
        }
        _curTraversal = "";
    }
    /** Recursively navigates graph G from vertex V
     *  with a hashmap of MARKED vertices.  STOPPED
     *  indicates if a later node called a stop exception.
     */
    private void depthHelper(Graph<VLabel, ELabel> G,
                             Graph<VLabel, ELabel>.Vertex v,
                             HashSet<Graph<VLabel, ELabel>.Vertex> marked,
                             Boolean stopped) {
        try {
            if (marked.contains(v)) {
                return;
            } else {
                marked.add(v);
                try {
                    visit(v);
                } catch (RejectException rExc) {
                    postVisit(v);
                    return;
                }
                HashSet<Graph<VLabel, ELabel>.Vertex> reject =
                    new HashSet<Graph<VLabel, ELabel>.Vertex>();
                for (Graph<VLabel, ELabel>.Edge e: G.outEdges(v)) {
                    try {
                        if (!marked.contains(e.getV(v))) {
                            preVisit(e, v);
                        }
                    } catch (RejectException rExc) {
                        reject.add(e.getV(v));
                    }
                }
                for (Graph<VLabel, ELabel>.Edge e: G.outEdges(v)) {
                    if (!reject.contains(e.getV(v))) {
                        depthHelper(G, e.getV(v), _marked, false);
                    }
                }
                try {
                    postVisit(v);
                } catch (RejectException rExc) {
                    int emptyCatchStylecheck = 0;
                }
            }
        } catch (StopException exc) {
            if (!stopped) {
                _finalVertex = v;
                stopped = true;
            }
            throw new StopException();
        }
    }

    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
        _graph = G;
        _finalVertex = null;
        _curTraversal = "bft";
        _marked = new HashSet<Graph<VLabel, ELabel>.Vertex>();
        breadthHelper(v);
    }

    /** Traverses V using a breadth-first traversal. */
    private void breadthHelper(Graph<VLabel, ELabel>.Vertex v) {
        LinkedList<Graph<VLabel, ELabel>.Vertex> fringe =
            new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        fringe.add(v);
        while (fringe.size() > 0) {
            Graph<VLabel, ELabel>.Vertex curV = fringe.poll();
            try {
                if (!_marked.contains(curV)) {
                    _marked.add(curV);
                    try {
                        visit(curV);
                    } catch (RejectException rExc) {
                        fringe.add(curV);
                        continue;
                    }
                    for (Graph<VLabel, ELabel>.Edge e: _graph.outEdges(curV)) {
                        Graph<VLabel, ELabel>.Vertex child = e.getV(curV);
                        if (!_marked.contains(child)) {
                            try {
                                preVisit(e, curV);
                                fringe.add(child);
                            } catch (RejectException rExc) {
                                int unused = 0;
                            }
                        }
                    }
                    fringe.add(curV);
                } else {
                    postVisit(curV);
                    while (fringe.remove(curV)) {
                        continue;
                    }
                }
            } catch (StopException sExc) {
                _finalVertex = curV;
                return;
            }
        }
    }

    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices or edges that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        if (_curTraversal.equals("dfs")) {
            try {
                depthHelper(_graph, v, _marked, false);
            } catch (StopException e) {
                return;
            }
            _finalVertex = null;
        } else if (_curTraversal.equals("gt")) {
            traverseHelper(v);
        } else if (_curTraversal.equals("bft")) {
            breadthHelper(v);
        }
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit that caused a Visit routine to return false.  Otherwise,
     *  returns null. */
    public Graph<VLabel, ELabel>.Vertex finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit that caused a Visit routine to return false. If it was not
     *  an edge that caused termination, returns null. */
    public Graph<VLabel, ELabel>.Edge finalEdge() {
        return _finalEdge;
    }

    /** Returns the graph currently being traversed.  Undefined if no traversal
     *  is in progress. */
    protected Graph<VLabel, ELabel> theGraph() {
        return _graph;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** The Vertex (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Vertex _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Edge _finalEdge;
    /** The graph currently being traversed. */
    protected Graph<VLabel, ELabel> _graph;

    /** Keeps track of marked vertices in the graph for continueTraversal. */
    private HashSet<Graph<VLabel, ELabel>.Vertex> _marked;
    /** The comparator the current traversal uses. */
    private Comparator<Graph<VLabel, ELabel>.Vertex> _comparator;
    /** Indicates what traversal is currently in progress. */
    private String _curTraversal;
}
