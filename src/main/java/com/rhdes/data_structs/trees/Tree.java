package com.rhdes.data_structs.trees;

import java.util.Iterator;

public abstract class Tree<T> {

	public abstract int numChildren();

	protected Tree(int numChildren, T value) {
		if (numChildren < 1) {
			// raise exception
			throw new TreeException("Number of children not positive.");
		} else {
			this.maxChildren = numChildren;
		}
		this.value = value;
	}

	public int maxChildren() {
		return this.maxChildren;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	protected void setParent(Tree<T> parent) {
		//null checks
		
		if (parent == null || (this.matchesTreeType(parent)
				&& this.maxChildren() == parent.maxChildren())) {
			this.parent = parent;
		} else if (!this.matchesTreeType(parent)) {
			throw new TreeException("Can't set parent:"
									+ "Tree types don't match.");
		} else {
			throw new TreeException("Can't set parent:"
					  				+ "number of max children aren't equal.");
		}
	}

	public Tree<T> getParent() {
		return this.parent;
	}

	protected boolean matchesTreeType(Tree<T> otherTree) {
		return otherTree != null && this.getClass() == otherTree.getClass()
							&& this.maxChildren() == otherTree.maxChildren();
	}

	//public abstract boolean isEqual(Tree<T> otherTree);

	// optimize
	public boolean findDescendant(Tree<T> desc) {
		// rethink?
		if (this == desc) {
			return true;
		}

		Iterator<Tree<T>> children = this.getChildren();
		while (children.hasNext()) {
			// depth first
			Tree<T> child = children.next();
			if (child == desc || child.findDescendant(desc)) {
				return true;
			}
		}
		return false;
	}


	public abstract void deleteChild(Tree<T> child);

	public abstract boolean containsChild(Tree<T> child);

	//public abstract boolean contains(Tree<T> desc);

	public abstract Iterator<Tree<T>> getChildren();

	@Override
	public abstract String toString();

	protected Tree<T> parent;
	protected T value;
	protected final int maxChildren;

}
