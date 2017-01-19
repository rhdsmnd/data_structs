package com.rhdes.data_structs.trees;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.Comparable;

public class BST<T> extends BinTree<T> implements SearchTree<T> {
	public BST(T value) {
		this(value, null, null);
	}
	
	public BST(T value, T firstLimit) {
		super(value);
		if (!isComparable(value)) {
			throw new TreeException("Binary search tree data does not implement "
									+ "Comparable<T> interface.");
		}
		
		if (firstLimit == null) {
			throw new TreeException("First limit must not be null: use BST(value) "
									+ "if there are no ancestor limits.");
		} else if (value == null) {
			throw new TreeException("Root value in binary search tree cannot be null.");
		}
		
		if (((Comparable) firstLimit).compareTo((Comparable) value) < 0) {
			this.leftLimit = firstLimit;
		} else if (((Comparable) firstLimit).compareTo(((Comparable) value)) > 0) {
			this.rightLimit = firstLimit;
		} else {
			throw new TreeException("Keys cannot be equal in binary search tree.");
		}
	}
	
	public BST(T value, T leftLimit, T rightLimit) {
		super(value);
		if (!isComparable(value)) {
			throw new TreeException("Binary search tree data does not implement "
									+ "Comparable<T> interface.");
		}
		
		if (leftLimit != null && ((Comparable) leftLimit).compareTo((Comparable) value) >= 0) {
			throw new TreeException("Root value cannot be less than or equal to left ancestor.");
		} else if (rightLimit != null
					&& ((Comparable) rightLimit).compareTo((Comparable) value) <= 0) {
			throw new TreeException("Root value cannot be greater than or equal to right ancestor.");
		}
		
		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
	}

	@Override
	public boolean insertValue(T value) {
		// if not root, return false: only operate at root of tree
		
		
		return false;
	}
	
	@Override
	public boolean deleteValue(T value) {
		// if not root, return false: only operate at root of tree
		
		return false;
	}
	
	private boolean isComparable(T value) {
		Type[] genInterfaces = value.getClass().getGenericInterfaces();
		for (int i = 0; i < genInterfaces.length; i += 1) {
            ParameterizedType parType = (ParameterizedType) genInterfaces[i];	
			if (parType.getRawType() == Comparable.class) {
				if (value.getClass().equals(parType.getActualTypeArguments()[0])) {
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	protected T leftLimit;
	protected T rightLimit;
}
