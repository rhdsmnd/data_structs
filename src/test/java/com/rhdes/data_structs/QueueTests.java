package com.rhdes.data_structs;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import com.rhdes.data_structs.queues.*;

public class QueueTests {
	private Queue<Integer> testQueue;

	@Before
	public void before() {
		testQueue = new Queue<Integer>();
	}

	@Test
	public void simpleEnqDeq() {
		assertTrue(testQueue.enqueue(1));
		assertEquals(testQueue.dequeue(), new Integer(1));
	}

	@Test
	public void multipleEnqDeq() {
		testQueue.enqueue(1);
		testQueue.enqueue(2);
		assertEquals(testQueue.dequeue(), new Integer(1));

		testQueue.enqueue(3);
		assertEquals(testQueue.dequeue(), new Integer(2));
		assertEquals(testQueue.dequeue(), new Integer(3));
	}

	@Test
	public void testResize() {
		for (int i = 0; i < 129; i += 1) {
			testQueue.enqueue(i);
		}
		assertEquals(testQueue.dequeue(), new Integer(0));
		for (int i = 0; i < 127; i += 1) {
			testQueue.dequeue();
		}
		assertEquals(testQueue.dequeue(), new Integer(128));
	}
}
