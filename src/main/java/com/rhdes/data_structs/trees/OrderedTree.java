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
		this.parentIndex = -1;
	}

	@Override
	public int numChildren() {
		// refactor later: (tally instead of array loop)
		// Keeping a tally is much faster than iterating over array
		// return this.numChildren;
		
		
		
		int count = 0;
		for (int i = 0; i < children.length; i += 1) {
			if (children[i] != null) {
				count += 1;
			}
		}
		return count;
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
			this._delChild(index);
		} else {
			throw new TreeException("Invalid index for deleteChild.");
		}
	}
	
	protected void _delChild(int index) {
		if (this.getChild(index) == null) {
			return;
		}
		
		OrderedTree<T> child = (OrderedTree<T>) this.children[index];
		child.parent = null;
		child.parentIndex = -1;
		
		this.children[index] = null;
		this.numChildren -= 1;
		
	}

	public void setChild(int index, Tree<T> newChild) {
		if (!isValidIndex(index)) {
			throw new TreeException("Invalid index for setChild.");
		}
		if (children[index] != null) {
			this._delChild(index);
		}
		if (newChild != null
					&& this.getClass().equals(newChild.getClass())
					&& this.maxChildren == newChild.maxChildren()) {
			children[index] = newChild;
			this.numChildren += 1;
			
			((OrderedTree<T>) newChild).setParent((OrderedTree<T>) this, index);
			
		}
	}
	
	@Override
	public void setParent(Tree<T> newParent) {
		if (newParent != null && !this.matchesTreeType(newParent)) {
			throw new TreeException("Parent and child tree types are different.");
		}
		this.setParent((OrderedTree<T>) newParent, -1);
	}
	
	public void setParent(OrderedTree<T> newParent, int parentIndex) {
		OrderedTree<T> oldParent = (OrderedTree<T>) this.parent;
		
		if (oldParent != null && newParent == null) {
			((OrderedTree<T>) oldParent)._delChild(this.parentIndex);
			this.parent = null;
			this.parentIndex = -1;
			return;
		}
		
		
		if (!this.matchesTreeType(newParent)) {
			throw new TreeException("Parent and child tree types are different.");
		} else if (parentIndex == -1) {
			parentIndex = newParent.firstFree();
		} else if (!isValidIndex(parentIndex)) {
			// make common string for all invalid index errors?
			throw new TreeException("Attempted to set invalid index");
		}
		
		// set child's parent to be PARENT, and have child be at index PARENTINDEX in the parent tree
		if (parentIndex == -1) {
			throw new TreeException("Cannot set parent: parent tree is full.");
		} else if (newParent.getChild(parentIndex) != null
						&& newParent.getChild(parentIndex) != this) {
			throw new TreeException("Cannot set parent: another child already exists at index "
										+ parentIndex + " in parent.");
		}

		if (oldParent != null) {
			((OrderedTree<T>) oldParent)._delChild(this.parentIndex);
		}
		
		newParent.children[parentIndex] = this;
		this.parentIndex = parentIndex;
		this.parent = newParent;

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
	
	
	// remove later; keep track of first free index separately
	// and make it a constant time operation, instead of 
	// looping through the children array each time
	protected int firstFree() {
		for (int i = 0; i < numChildren; i += 1) {
			if (children[i] == null) {
				return i;
			}
		}
		
		return -1;
	}
	
	protected int parentIndex;
	protected int numChildren;
	protected final Object[] children;
}

