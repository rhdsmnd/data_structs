package com.rhdes.data_structs.trees;

public interface SearchTree<T> {

	public abstract boolean insertValue(T t);
	
	public abstract boolean deleteValue(T t);
}
