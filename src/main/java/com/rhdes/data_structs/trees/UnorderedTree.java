package com.rhdes.data_structs.trees;

import java.util.Iterator;
import java.util.HashSet;

public class UnorderedTree<T> extends Tree<T> {

	public UnorderedTree(T value) {
		super(Integer.MAX_VALUE, value);
		children = new HashSet<Tree<T>>();
	}


	public int numChildren() {
		return children.size();
	}

	public void addChild(Tree<T> child) {
		if (this.matchesTreeType(child)) {
			children.add(child);
			child.setParent(this);
		} else {
			throw new TreeException("Can't add child: tree types don't match");
		}
	}

	@Override
	public Iterator<Tree<T>> getChildren() {
		return children.iterator();
	}

	public void deleteChild(Tree<T> child) {
		if (children.contains(child)) {
			children.remove(child);
			child.setParent(null);
		}
	}

	@Override
	public boolean containsChild(Tree<T> child) {
		return children.contains(child);
	}

	@Override
	public String toString() {
		String ret = "";
		ret += "(" + this.getValue();
		for (Object child: children) {
			ret += " " + child.toString();
		}
		ret += ")";
		return ret;
	}

	private HashSet<Tree<T>> children;	
}
