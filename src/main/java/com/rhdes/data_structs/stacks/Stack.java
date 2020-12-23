package com.rhdes.data_structs.stacks;

import java.util.Arrays;

public class Stack<T> {

	private static final int DEFAULT_SIZE = 16;
	private static final int DEALLOC_THRESHOLD = 128;

	private static final int RESIZE_RATIO = 2;

	public Stack() {
		this(Integer.MAX_VALUE);
	}

	public Stack(int maxSize) {
		if (maxSize < 1) {
			throw new IllegalArgumentException("Maximum size must be a positive number");
		} else {
			this.maxSize = maxSize;
		}
		values = new Object[DEFAULT_SIZE];
		head = -1;
	}

	public T pop() {
		if (head >= 0) {
			T ret = (T) values[head];
			head -= 1;
			if (values.length >= DEALLOC_THRESHOLD
							&& head < values.length / (2 * RESIZE_RATIO)) {
				values = Arrays.copyOf(values, values.length / RESIZE_RATIO);
			}
			return ret;
		} else {
			return null;
		}
	}

	private void resizeSmaller() {

	}

	public boolean push(T value) {
		if (head == maxSize - 1) {
			// throw exception (stack is full)
			return false;
		} else if (head == values.length - 1) {
			values = Arrays.copyOf(values, values.length * RESIZE_RATIO);
		}
		head += 1;
		values[head] = value;
		return true;
	}

	public boolean empty() {
		return head == -1;
	}

	private int head;
	private int maxSize;
	private Object[] values;
}
