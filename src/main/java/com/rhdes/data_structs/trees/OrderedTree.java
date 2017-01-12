package com.rhdes.data_structs.trees;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedTree<T> extends Tree<T> {

	public OrderedTree(int numChildren, T value) {
		super(numChildren, value);

		children = new Object[numChildren];
		for (int i = 0; i < children.length; i += 1) {
			children[i] = null;
		}
		this.numChildren = 0;
	}

	@Override
	public int numChildren() {
		// Keeping a tally is much faster than iterating over array
		return this.numChildren;
	}

	// move code later to after Tree methods
	public OrderedTree<T> getChild(int index) {
		if (isValidIndex(index)) {
			return (OrderedTree<T>) children[index];
		} else {
			throw new TreeException("Not a valid tree index.");
		}
	}

	@Override
	public void deleteChild(Tree<T> child) {
		if (child == null) {
			throw new TreeException("Can't delete null child.");
		}
		
		// consider adding a hashmap mapping reference to index in children for ~constant time deletion & search
		for (int i = 0; i < children.length; i += 1) {
			OrderedTree<T> elem = (OrderedTree<T>) children[i];
			if (child == elem) {
				children[i] = null;
				child.setParent(null);
				this.numChildren -= 1;
			}
		}
	}	

	public void deleteChild(int index) {
		if (isValidIndex(index) && children[index] != null) {
			((OrderedTree<T>) children[index]).setParent(null);
			children[index] = null;
			this.numChildren -= 1;
		} else {
			throw new TreeException("Invalid index for deleteChild.");
		}
		
		
	}

	public void setChild(int index, Tree<T> newChild) {
		if (!isValidIndex(index)) {
			throw new TreeException("Invalid index for setChild.");
		}
		if (children[index] != null) {
			Tree<T> curChild = (Tree<T>) children[index];
			curChild.setParent(null);
			children[index] = null;
		}
		if (newChild != null
					&& this.getClass().equals(newChild.getClass())
					&& this.maxChildren == newChild.maxChildren()) {
			
			children[index] = newChild;
			this.numChildren += 1;
			newChild.setParent(this);
		}
	}
	
	public void setParent(Tree<T> parent, int parentIndex) {
		if (parent == null && this.parent != null) {
			this.parent.deleteChild(this);
			return;
		}
		if (!isValidIndex(parentIndex) && parentIndex != -1) {
			// make common string for all invalid index errors?
			throw new TreeException("Attempted to set invalid index");
		} else if (!this.matchesTreeType(parent)) {
			throw new TreeException("Parent and child tree types are different.");
		}
		
		// set child's parent to be PARENT, and have child be at index PARENTINDEX in the parent tree
		this.parent = parent;
	}

	//@Override
	public boolean isEqual(Tree<T> otherTree) {
		if (otherTree == null || this.getClass() != otherTree.getClass()
				|| this.maxChildren() != otherTree.maxChildren()) {
			return false;
		}


		T thisData = this.getValue();
		T otherData = otherTree.getValue();

		boolean equalValues;
		if (thisData == null && otherData == null) {
			equalValues = true;
		} else if (thisData == null || otherData == null) {
			equalValues = false;
		} else {
			equalValues = thisData.equals(otherData);
		}

		if (!equalValues) {
			return false;
		}

		OrderedTree<T> thisChild;
		OrderedTree<T> otherChild;
		for (int i = 0; i < this.maxChildren(); i += 1) {
			thisChild = this.getChild(i);
			otherChild = ((OrderedTree<T>) otherTree).getChild(i);

			if (thisChild == null && otherChild == null) {
				continue;
			} else if (thisChild == null || otherChild == null
					|| !(thisChild.isEqual(otherChild))) {
				return false;
			}
		}
		return true;
	}


	@Override
	public Iterator<Tree<T>> getChildren() {
		return new Iterator<Tree<T>>() {
			@Override
			public boolean hasNext() {
				while (i < children.length && children[i] == null) {
					i += 1;
				}
				if (i >= children.length) {
					return false;
				} else {
					return true;
				}
			}

			@Override
			public Tree<T> next() throws NoSuchElementException {
				while (i < children.length && children[i] == null) {
					i += 1;
				}
				if (i >= children.length) {
					throw new NoSuchElementException();
				} else {
					Tree<T> ret = (Tree<T>) children[i];
					i += 1;
					return ret;
				}
			}

			private int i = 0;
		};
	}

	// move later to match class order in Tree.java
	@Override
	public boolean containsChild(Tree<T> desc) {
		for (int i = 0; i < children.length; i += 1) {
			if (desc == children[i]) {
				return true;
			}
		}
		return false;
	}

	// TEST TEST TEST
	@Override
	public String toString() {
		String ret = "";
		ret += "(" + this.getValue();
		for (int i = 0; i < children.length; i += 1) {
			if (children[i] == null) {
				ret += " ()";
			} else { 
				ret += " " + children[i].toString();
			}
		}
		ret += ")";
		return ret;
	}

	private boolean isValidIndex(int index) {
		return index >= 0 && index < this.maxChildren();
	}
	
	protected int numChildren;
	protected final Object[] children;
}

