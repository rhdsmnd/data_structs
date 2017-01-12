package com.rhdes.data_structs.trees;

public class BinTree<T> extends
						OrderedTree<T> {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
    
	public BinTree(T value) {
		super(2, value);
	}
 
}
