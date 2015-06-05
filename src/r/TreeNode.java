package r;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

	public T data;
	public TreeNode<T> parent;
	public List<TreeNode<T>> children;
//	private List<TreeNode<T>> elementsIndex;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
//		this.elementsIndex = new LinkedList<TreeNode<T>>();
//		this.elementsIndex.add(this);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public TreeNode<T> getParent() {
		if(parent != null)
			return parent;
		return this;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}


	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public TreeNode<T> addChild(T child) {
		TreeNode<T> childNode = new TreeNode<T>(child);
		childNode.parent = this;
//		childNode.elementsIndex = elementsIndex;
		this.children.add(childNode);
//		this.registerChildForSearch(childNode);
		return childNode;
	}

//	public TreeNode<T> findTreeNode(Comparable<T> cmp) {
//		for (TreeNode<T> element : this.elementsIndex) {
//			T elData = element.data;
//			if (cmp.compareTo(elData) == 0)
//				return element;
//		}
//
//		return null;
//	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	private void registerChildForSearch(TreeNode<T> node) {
//		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node);
	}

	@Override
	public String toString() {
		return data != null ? data.toString() : "[data null]";
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
		return iter;
	}

	/**
	 * TODO: Ensure that the specifiedChild is the child referenced inside current. Will most likely have to be routed through another function.
	 * TODO: Method seems to be replacing all other data with only information about itself.
	 * Makes the current node the specifiedChild.
	 * @param specifiedChild - node to be changed to.
	 * @return boolean
	 */
	public TreeNode<T> getChild(T specifiedChild){
		TreeNode<T> tmp = contains(specifiedChild);

		for(TreeNode<T> child : children){
			if(child.equals(tmp)){
				return child;
			}

		}
		return null;
	}


	/**
	 *
	 * Checks if current node contains specified node inside children.
	 * @param node
	 * @return TreeNode<T>
	 */
	public TreeNode<T> contains(T node){
		for(TreeNode<T> child : children){
			if(shallowEquals(child, node)) {
				return child;
			}
		}
		return null;
	}

	/**
	 *
	 * equals compares data only, and ignores all other attributes.
	 * @param nodeA
	 * @param nodeB
	 * @return
	 */
	public boolean shallowEquals (TreeNode<T> nodeA, T nodeB){
		Boolean result = nodeA.data.equals(nodeB);
//		System.out.println("shallowEquals Result: " + result);
		return result;
	}


	private static String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}


}
