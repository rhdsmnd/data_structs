package com.rhdes.data_structs;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import com.rhdes.data_structs.stacks.*;

public class StackTests {
	private Stack<Integer> testStack;

	@Before
	public void before() {
		testStack = new Stack<Integer>();
	}

	@Test
	public void simplePopPush() {
		assertTrue(testStack.push(1));
		assertEquals(testStack.pop(), new Integer(1));
	}

	@Test
	public void multiplePopPush() {
		testStack.push(1);
		testStack.push(-1);

		assertEquals(testStack.pop(), new Integer(-1));

		testStack.push(3);
		assertEquals(testStack.pop(), new Integer(3));

		assertEquals(testStack.pop(), new Integer(1));
	}

	@Test
	public void testResize() {
		for (int i = 0; i < 129; i += 1) {
			testStack.push(i);
		}
		assertEquals(testStack.pop(), new Integer(128));
		for (int i = 0; i < 127; i += 1) {
			testStack.pop();
		}
		assertEquals(testStack.pop(), new Integer(0));
	}
}
