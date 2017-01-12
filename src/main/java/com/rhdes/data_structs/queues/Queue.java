package com.rhdes.data_structs.queues;

import java.util.Arrays;

public class Queue<T> {

	private static final int DEFAULT_SIZE = 8;
	private static final int DEALLOC_THRESHOLD = 128;

	private static final int RESIZE_RATIO = 2;

	private enum Resize {
		LARGER, SMALLER
	}

	public Queue() {
		this(Integer.MAX_VALUE);
	}

	public Queue(int maxSize) {
		if (maxSize <= 0) {
			// exception
			this.maxSize = 0;
		} else {
			this.maxSize = maxSize;
		}
		
		if (maxSize < 16) {
			values = new Object[maxSize];
		} else {
			values = new Object[16];
		}

		full = false;

		head = 0;
		tail = 0;
	}

	public T dequeue() {
		if (head != tail || full) {
			T ret = (T) values[tail];
			tail = advance(tail);
			if (shouldResize(Resize.SMALLER)) {
				resize(Resize.SMALLER);
			}
			full = false;
			return ret;
		} else {
			// queue empty
			return null;
		}
	}

	public boolean enqueue(T value) {
		if (shouldResize(Resize.LARGER)) {
			resize(Resize.LARGER);
		} else if (full) {
			return false;
		}
		
		values[head] = value;
		head = advance(head);
		full = head == tail;

		return true;
	}

	private boolean shouldResize(Resize r) {
		if (r == Resize.SMALLER && values.length > DEALLOC_THRESHOLD)	 {
			return numValues() * 2 < values.length;
		} else {
			return full && values.length < maxSize;
		}
	}

	private void resize(Resize r) {
		Object[] newArr;
		
		if (r == Resize.SMALLER) {
			newArr = new Object[values.length / 2];
		} else {
			newArr = new Object[Math.min(values.length * 2, maxSize)];
		}
		head = copyElems(values, newArr);
		tail = 0;
		values = newArr;
		// should always be false, maybe revisit
		full = head == tail;
	}

	private int copyElems(Object[] fromArr, Object[] toArr) {
		int counter = 0;
		while (tail != head || full) {
			if (tail == head) {
				full = false;
			}
			toArr[counter] = fromArr[tail];
			tail = advance(tail);
			counter += 1;
		}
		return counter;
	}

	public int numValues() {
		if (head > tail) {
			return head - tail;	
		} else if (head < tail) {
			// separate arithmetic operations to prevent possible overflow?
			int tailToEnd = values.length - tail;
			return head + tailToEnd;
		} else if (full) {
			return values.length;
		} else {
			return 0;
		}
	}

	private int advance(int index) {
		if (index == values.length - 1) {
			return 0;
		} else {
			return index + 1;
		}
	}

	private T prev(int index) {
		if (index < 0 || values.length == 0) {
			// throw exception
			return null;	
		} else if (index == 0)  {
			return (T) values[values.length - 1];
		} else {
			return (T) values[index - 1];
		}
	}

	private boolean full;

	// different from 'head' in Stack; corresponds to next empty slot
	// not index of data in queue
	private int head;
	private int tail;
	private int maxSize;
	private Object[] values;
}
