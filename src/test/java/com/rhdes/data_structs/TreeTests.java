package com.rhdes.data_structs;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

import com.rhdes.data_structs.trees.*;

public class TreeTests {


	@Test
	public void orderedTreeTests() {
		OrderedTree<String> root = new OrderedTree<String>(2, "root");
		assertEquals("root", root.getValue());

		// copied from OrderedTreeTests.java
		Iterator<Tree<String>> empty = root.getChildren();

		assertTrue(!empty.hasNext());

		try {
			empty.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {
			assertTrue(true);
		}

		OrderedTree<String> child1 = new OrderedTree<String>(2, "child1");
		OrderedTree<String> child2 = new OrderedTree<String>(2, "child2");
		assertEquals(2, root.maxChildren());

		root.setChild(0, child1);
		root.setChild(1, child2); 

		assertEquals(child1, root.getChild(0));
		assertEquals(child2, root.getChild(1));
		assertEquals(root, child1.getParent());

		assertEquals(2, root.numChildren());
		assertEquals(2, root.maxChildren());

		ArrayList<OrderedTree<String>> childList = new ArrayList<OrderedTree<String>>();
		childList.add(child1);
		childList.add(child2);

		Iterator<Tree<String>> twoChildren = root.getChildren();

		try {
			OrderedTree<String> iter = (OrderedTree<String>) twoChildren.next();	
			assertTrue(childList.contains(iter));
			childList.remove(iter);
			iter = (OrderedTree<String>) twoChildren.next();
			assertTrue(childList.contains(iter));
			childList.remove(iter);
			assertTrue(!twoChildren.hasNext());
		} catch (NoSuchElementException e) {
			assertTrue(false);
		}


		root.deleteChild(0);
		assertEquals(1, root.numChildren());

		assertNull(child1.getParent());
		assertNull(root.getChild(0));

		
		root.deleteChild(child2);

		assertNull(child2.getParent());
		assertNull(root.getChild(1));
		assertEquals(0, root.numChildren());
		
	}

	@Test
	public void matchingTreesOnly() {
		OrderedTree<String> root = new OrderedTree<String>(2, "root");
		OrderedTree<String> child1 = new OrderedTree<String>(3, "child1");
		OrderedTree<String> binTreeChild = new BinTree<String>("child2");

		root.setChild(0, child1);
		root.setChild(1, binTreeChild);
		assertEquals(0, root.numChildren());
	}

	@Test
	public void orderedTreeChildrenCounter() {
		OrderedTree<String> root = new OrderedTree<String>(2, "root");
		OrderedTree<String> child1 = new OrderedTree<String>(2, "child1");

		root.setChild(0, child1);
		assertEquals(1, root.numChildren());

		OrderedTree<String> child2 = new OrderedTree<String>(2, "child2");
		root.setChild(1, child2);
		assertEquals(2, root.numChildren());

		root.deleteChild(child2);
		assertEquals(1, root.numChildren());

		root.deleteChild(child1);
		assertEquals(0, root.numChildren());
	}

	@Test
	public void orderedEqualsTest() {

		OrderedTree<String> root = new OrderedTree<String>(2, "root");
		OrderedTree<String> eqRoot = new OrderedTree<String>(2, "root");

		assertTrue(root.isEqual(eqRoot));
		assertTrue(eqRoot.isEqual(root));

		Tree<String> difChildNum = new OrderedTree<String>(3, "root");
		assertTrue(!root.isEqual(difChildNum)); 

		Tree<String> difTreeType = new BinTree<String>("root");
		assertTrue(!root.isEqual(difTreeType));

		OrderedTree<String> child1 = new OrderedTree<String>(2, "child1");
		root.setChild(0, child1);
		OrderedTree<String> eqChild1 = new OrderedTree<String>(2, "child1");
		eqRoot.setChild(0, eqChild1);
		assertTrue(root.isEqual(eqRoot));

		OrderedTree<String> child2 = new OrderedTree<String>(2, "child2");
		OrderedTree<String> child3 = new OrderedTree<String>(2, "child3");
		OrderedTree<String> child4 = new OrderedTree<String>(2, "child4");
		OrderedTree<String> child5 = new OrderedTree<String>(2, "child5");
		OrderedTree<String> child6 = new OrderedTree<String>(2, "child6");
		root.setChild(1, child2);
		child1.setChild(0, child3);
		child1.setChild(1, child4);
		child2.setChild(0, child5);
		child2.setChild(1, child6);
		
		OrderedTree<String> eqChild2 = new OrderedTree<String>(2, "child2");
		OrderedTree<String> eqChild3 = new OrderedTree<String>(2, "child3");
		OrderedTree<String> eqChild4 = new OrderedTree<String>(2, "child4");
		OrderedTree<String> eqChild5 = new OrderedTree<String>(2, "child5");
		OrderedTree<String> eqChild6 = new OrderedTree<String>(2, "child6");
		eqRoot.setChild(1, eqChild2);
		eqChild1.setChild(0, eqChild3);
		eqChild1.setChild(1, eqChild4);
		eqChild2.setChild(0, eqChild5);
		eqChild2.setChild(1, eqChild6);

		assertTrue(root.isEqual(eqRoot));
		assertTrue(root.findDescendant(child6));
		assertTrue(root.findDescendant(child3));
		assertFalse(root.findDescendant(eqChild6));
		
		root.deleteChild(0);
		eqRoot.deleteChild(0);

		eqChild2.deleteChild(0);
		child2.deleteChild(0);

		assertTrue(root.isEqual(eqRoot));
	}
	
	@Test
	public void orderedSwapChildTest() {
		OrderedTree<Integer> root = new OrderedTree<Integer>(2, 1);
		OrderedTree<Integer> swapChild = new OrderedTree<Integer>(2, 1);
		
		OrderedTree<Integer> child1 = new OrderedTree<Integer>(2, 2);
		OrderedTree<Integer> child2 = new OrderedTree<Integer>(2, 3);
		
		OrderedTree<Integer> eq1 = new OrderedTree<Integer>(2, 2);
		OrderedTree<Integer> eq2 = new OrderedTree<Integer>(2, 3);
		
		root.setChild(0, child1);
		root.setChild(1, child2);
		
		swapChild.setChild(0, eq2);
		swapChild.setChild(1, eq1);
		
		
		assertFalse(root.equals(swapChild));
		
		swapChild.deleteChild(1);
		
		assertFalse(root.equals(swapChild));
		
		swapChild.setChild(1, eq2);
		
		assertFalse(root.equals(swapChild));
		
		return;
	}


	@Test
	public void unorderedTreeTests() {
		UnorderedTree<Integer> root = new UnorderedTree<Integer>(1);
		UnorderedTree<Integer> child2 = new UnorderedTree<Integer>(2);
		UnorderedTree<Integer> child3 = new UnorderedTree<Integer>(3);

		Iterator<Tree<Integer>> empty = root.getChildren();

		assertTrue(!empty.hasNext());

		try {
			empty.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {
			assertTrue(true);
		}

		assertEquals(root.maxChildren(), Integer.MAX_VALUE);
		assertEquals(root.numChildren(), 0);

		root.addChild(child2);
		root.addChild(child3);	
		
		assertEquals(root.numChildren(), 2);
		assertEquals(child2.getParent(), root);
		assertTrue(root.containsChild(child3));

		ArrayList<UnorderedTree<Integer>> childList = new ArrayList<UnorderedTree<Integer>>();
		childList.add(child2);
		childList.add(child3);

		Iterator<Tree<Integer>> twoChildren = root.getChildren();

		try {
			UnorderedTree<Integer> iter = (UnorderedTree<Integer>) twoChildren.next();	
			assertTrue(childList.contains(iter));
			childList.remove(iter);
			iter = (UnorderedTree<Integer>) twoChildren.next();
			assertTrue(childList.contains(iter));
			childList.remove(iter);
			assertTrue(!twoChildren.hasNext());
		} catch (NoSuchElementException e) {
			assertTrue(false);
		}


		root.deleteChild(child2);

		assertNull(child2.getParent());
		assertEquals(root.numChildren(), 1);
		assertTrue(!root.containsChild(child2));

		
	}

	@Test
	public void ordIntTreeTest() {
		TreeUtil.TreeParser<Integer> intTreeParser
				= new TreeUtil.TreeParser<Integer>(Integer.class, OrderedTree.class);
		String ordIntTree = "(1 () ())";
		
		OrderedTree<Integer> parsedIntTree = (OrderedTree<Integer>) intTreeParser.treeFromString(ordIntTree,
															OrderedTree.class, 2);
		OrderedTree<Integer> eqIntTree = new OrderedTree<Integer>(2, 1);
		assertTrue(((OrderedTree<Integer>) intTreeParser.treeFromString(ordIntTree,
											OrderedTree.class, 2)).isEqual(parsedIntTree));

		String simpBinTreeInt = "(1)";
		OrderedTree<Integer> eqBinTree = new BinTree<Integer>(1);
		OrderedTree<Integer> parsedBinTree = (OrderedTree<Integer>) intTreeParser.treeFromString(simpBinTreeInt,
															BinTree.class, 2);
		assertTrue(parsedBinTree.isEqual(eqBinTree));

		String handleWhitespace = "(   1 ( \n) (\t	)		)";
		parsedIntTree = (OrderedTree<Integer>) intTreeParser.treeFromString(handleWhitespace,
															OrderedTree.class, 2);
		eqIntTree = new OrderedTree<Integer>(2, 1);
		assertTrue(eqIntTree.isEqual(parsedIntTree));

		String multilevelTree = "(1 (2) (3))";
		eqBinTree = new BinTree<Integer>(1);
		OrderedTree<Integer> leftChild = new BinTree<Integer>(2);
		OrderedTree<Integer> rightChild = new BinTree<Integer>(3);
		eqBinTree.setChild(BinTree.LEFT, leftChild);
		eqBinTree.setChild(BinTree.RIGHT, rightChild);

		parsedBinTree = (OrderedTree<Integer>) intTreeParser.treeFromString(multilevelTree,
															BinTree.class, 2);

		String unbalancedTree = "(1 () (2 () (3)))";
		parsedBinTree = (OrderedTree<Integer>) intTreeParser.treeFromString(unbalancedTree,
													BinTree.class, 2);
		eqBinTree = new BinTree<Integer>(1);
		OrderedTree<Integer> child1 = new BinTree<Integer>(2);
		OrderedTree<Integer> child2 = new BinTree<Integer>(3);

		eqBinTree.setChild(BinTree.RIGHT, child1);
		child1.setChild(BinTree.RIGHT, child2);


		String largerMultilevelTree = "(1 (2 (4) (5)) (3 (6) (7))) ";
		parsedBinTree = (OrderedTree<Integer>) intTreeParser.treeFromString(largerMultilevelTree,
															BinTree.class, 2);
		// Changing variable name semantics: child2 now refers to node with data = 2


		eqBinTree = new BinTree<Integer>(1);
		child2 = new BinTree<Integer>(2);
		OrderedTree<Integer> child3=  new BinTree<Integer>(3);
		OrderedTree<Integer> child4=  new BinTree<Integer>(4);
		OrderedTree<Integer> child5=  new BinTree<Integer>(5);
		OrderedTree<Integer> child6=  new BinTree<Integer>(6);
		OrderedTree<Integer> child7=  new BinTree<Integer>(7);

		// wire the trees together
		eqBinTree.setChild(BinTree.LEFT, child2);
		eqBinTree.setChild(BinTree.RIGHT, child3);
		child2.setChild(BinTree.LEFT, child4);
		child2.setChild(BinTree.RIGHT, child5);
		child3.setChild(BinTree.LEFT, child6);
		child3.setChild(BinTree.RIGHT, child7);


		assertTrue(eqBinTree.isEqual(parsedBinTree));

		child3.deleteChild(BinTree.LEFT);
		parsedBinTree.getChild(BinTree.RIGHT).deleteChild(BinTree.LEFT);

		assertTrue(eqBinTree.isEqual(parsedBinTree));

		String threeChildren = "(1 (2 (5) (6) (7)) (3 (8) (9) (10)) (4 (11) (12) (13)))";
		parsedIntTree = (OrderedTree<Integer>) intTreeParser.treeFromString(threeChildren, OrderedTree.class,
														3);
		eqIntTree = new OrderedTree<Integer>(3, 1);

		// THERE HAS GOT TO BE A BETTER WAY AKA MAKE ONE
		child2 = new OrderedTree<Integer>(3, 2);
		child3 = new OrderedTree<Integer>(3, 3);
		child4 = new OrderedTree<Integer>(3, 4);
		child5 = new OrderedTree<Integer>(3, 5);
		child6 = new OrderedTree<Integer>(3, 6);
		child7 = new OrderedTree<Integer>(3, 7);
		OrderedTree<Integer> child8 = new OrderedTree<Integer>(3, 8);
		OrderedTree<Integer> child9 = new OrderedTree<Integer>(3, 9);
		OrderedTree<Integer> child10 = new OrderedTree<Integer>(3, 10);
		OrderedTree<Integer> child11 = new OrderedTree<Integer>(3, 11);
		OrderedTree<Integer> child12 = new OrderedTree<Integer>(3, 12);
		OrderedTree<Integer> child13 = new OrderedTree<Integer>(3, 13);

		eqIntTree.setChild(0, child2);
		eqIntTree.setChild(1, child3);
		eqIntTree.setChild(2, child4);

		child2.setChild(0, child5);
		child2.setChild(1, child6);
		child2.setChild(2, child7);

		child3.setChild(0, child8);
		child3.setChild(1, child9);
		child3.setChild(2, child10);

		child4.setChild(0, child11);
		child4.setChild(1, child12);
		child4.setChild(2, child13);

		assertTrue(eqIntTree.isEqual(parsedIntTree));

		String fourLevels = "(1 (2 (3 (4 () ()) (5 () ())) (6 (7 () ()) (8 () ()))) (9 (10 (11 () ()) (12 () ())) (13 (14 () ()) (15 () ()))))";

		parsedIntTree = (OrderedTree<Integer>) intTreeParser.treeFromString(fourLevels, BinTree.class, 2);
		assertTrue(parsedIntTree.getChild(BinTree.LEFT) != null);
		assertTrue(parsedIntTree.getChild(BinTree.LEFT).getChild(BinTree.RIGHT) != null);
		assertTrue(parsedIntTree.getChild(BinTree.LEFT).getChild(BinTree.RIGHT).getValue() == 6);

	}

	
	@Test
	public void unordIntTreeTest() {
		TreeUtil.TreeParser<Integer> unordIntTreeParser = new TreeUtil.TreeParser(Integer.class,
																		UnorderedTree.class);
		String simpleTree = "(1)"; 

		Tree<Integer> parsedTree = unordIntTreeParser.treeFromString(simpleTree,
																	UnorderedTree.class);
		assertEquals(parsedTree.getValue(), new Integer(1));
		assertEquals(parsedTree.numChildren(), 0);

		String simpleWithChildren = "(1 (2) (3))";
		parsedTree = unordIntTreeParser.treeFromString(simpleWithChildren,
														UnorderedTree.class);

		ArrayList<Integer> childVals = new ArrayList<Integer>();
		Iterator<Tree<Integer>> children = parsedTree.getChildren();
		Tree<Integer> childIter;
		while(children.hasNext()) {
			childIter = children.next();
			childVals.add(childIter.getValue());
			assertEquals(UnorderedTree.class, childIter.getClass());
		}
		assertTrue(childVals.contains(2));
		assertTrue(childVals.contains(3));
		assertEquals(childVals.size(), 2);
	}

}
