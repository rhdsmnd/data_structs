package com.rhdes.data_structs.trees;

public class TreeUtil {

	public static class TreeParser<T> {
		interface ParseDataObj<T> {
			T parseAndDiscard(StringBuffer strBuf);
		}

		public TreeParser(Class<T> dataType,
							Class<? extends Tree> treeType) {
			this.treeType = treeType;
			if (dataType == String.class) {
				this.dataParser = ((StringBuffer strBuf) -> {
					// search for ", continue if instance = \" // 
					int ind = 0;
					// null check for strBuf? Error case for empty stringbuffer?
					if (strBuf.length() == 0 || strBuf.charAt(ind) != '"') {
						throw new TreeParserException("Not valid string");
					} else {
						ind += 1;
					}
					
					while ((ind < strBuf.length()) && (strBuf.charAt(ind) != '\"' ||
							strBuf.charAt(ind - 1) == '\\')) {
						ind += 1;
					}
					String ret = strBuf.substring(1, ind);

					// get rid of quote character
					if (ind != strBuf.length()) {
						ind += 1;
					}

					strBuf.delete(0, ind);
					return ret;
				});
			} else if (dataType == Integer.class) {
				this.dataParser = ((StringBuffer strBuf) -> {
					int ind = 0;
			
					while (ind < strBuf.length()
							&& Character.isDigit(strBuf.charAt(ind))) {
						ind += 1;
					}
					String charsRead = strBuf.substring(0, ind);
					strBuf.delete(0, ind);

					try {
						return Integer.parseInt(charsRead);
					} catch (NumberFormatException e) {
						// raise exception
						throw new TreeParserException("Couldn't parse string \""
											+ charsRead + "\" to integer.");
					}
				});
			} else {
				throw new TreeParserException("Class does not have a builtin data parser:"
											+ " call constructor TreeParser(dataType, tree, func).");
			}
		}

		public TreeParser(Class<T> dataType, Class<? extends Tree> treeType,
							ParseDataObj func) {
			this.treeType = treeType;
			this.dataParser = func;
		}

		Class<? extends Tree> treeType;
		ParseDataObj dataParser;

		public Tree<T> treeFromString(String str, Class<? extends Tree> treeType,
										int numChildren) {
			// make sure numChildren > 0
			return treeFromString(new StringBuffer(str), treeType, numChildren);
		}

		public Tree<T> treeFromString(String str, Class<? extends Tree> treeType) {
			return treeFromString(new StringBuffer(str), treeType, Integer.MAX_VALUE);
		}


		// syntax (1 () ()))
		public Tree<T> treeFromString(StringBuffer strBuf,
					Class<? extends Tree> treeType, int numChildren) {
			// consider making helper function
			// null checks for strBuf
			nextToken(strBuf);

			if (strBuf.charAt(0) != '(') {
				// raise exception
				System.out.println("Error with tree format");
				return null;
			}

			// outline

			strBuf.deleteCharAt(0);
			if (nextToken(strBuf) == ')') {
				strBuf.deleteCharAt(0);
				return null;
			}

			Object dataObj = this.dataParser.parseAndDiscard(strBuf);
			Tree<T> root = this.createTree(treeType, numChildren, dataObj);

			int childInd = 0;
			Tree<T> child;

			while (nextToken(strBuf) != null && nextToken(strBuf) == '(') {
				child =	this.treeFromString(strBuf, treeType, numChildren);
				setChild(root, child, root.numChildren());
			}

			if (nextToken(strBuf) != null && nextToken(strBuf) == ')') {
				strBuf.deleteCharAt(0);
				return root;
			} else {
				throw new TreeException("Error parsing tree: final token should be"
						+ "')', but is: '" + nextToken(strBuf) + "'.");
			}

		}

		protected void setChild(Tree<T> root, Tree<T> child, int index) {
			if ((OrderedTree.class).isAssignableFrom(root.getClass())) {
				((OrderedTree<T>) root).setChild(index, child);
			} else if ((UnorderedTree.class).isAssignableFrom(root.getClass())
						&& child != null) {
				((UnorderedTree<T>) root).addChild(child);
			}
		}

		private Character nextToken(StringBuffer strBuf) {
			while (strBuf.length() > 0
					&& Character.isWhitespace(strBuf.charAt(0))) {
				strBuf.deleteCharAt(0);
			}
			if (strBuf.length() > 0) {
				return strBuf.charAt(0);
			} else {
				return null;
			}
		}

		public Tree<T> createTree(Class<?> treeType, int numChildren, Object dataObj) {
			if (treeType == OrderedTree.class) {
				return new OrderedTree<T>(numChildren, (T) dataObj);
			} else if (treeType == BinTree.class) {
				return new BinTree<T>((T) dataObj);
			} else if (treeType == UnorderedTree.class) {
				return new UnorderedTree<T>((T) dataObj);
			} else {
				throw new TreeParserException("TreeType \"" + treeType
													+ "\" not supported.");
			}
		}
	}

/**
	public static void inorderTraversal(BinTree<T> t) {
		Stack<BinTree<T>> inorderStack = new Stack<BinTree<T>>();
		inorderAddToStack(t, inorderStack);

		//have visit take in lambda function
		//visit(inorderStack);
	}

	public static void inorderIterTraversal(BinTree<T> t) {
		Stack<BinTree<T>> inorderStack = new Stack<BinTree<T>>();
		HashSet<BinTree<T>> visited = new HashSet<BinTree<T>>();
		
		BinTree<T> iter = t;
		while (iter != null) {
			BinTree<T> left = t.getChild(BinTree.LEFT);
			
			// need to visit left
			if (left != null && !visited.contains(left)) {
				inorderStack.push(iter);
				inorderStack.push(left);
				continue;
			}

			//visit(iter);

			inorderStack.push(t.getChild(BinTree.RIGHT));
		}
	}

	private void inorderAddToStack(BinTree<T> t, Stack<BinTree<T>> treeStack) {
		if (t == null) {
			return;
		} else {
			inorderAddToStack(t.getChild(BinTree.RIGHT), treeStack);
			treeStack.push(t);
			inorderAddToStack(t.getChild(BinTree.LEFT), treeStack);
		}
	}
*/
}
