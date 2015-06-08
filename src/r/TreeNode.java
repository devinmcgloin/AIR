package r;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

	public T data;
	public TreeNode<T> parent;
	public List<TreeNode<T>> children;
	private List<TreeNode<T>> elementsIndex;
	TreeHash<T, TreeNode<T>> hash;

	/**
	 * TODO: Initialize hash map for only base nodes.
	 * @param data
	 */
	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
		this.elementsIndex = new LinkedList<TreeNode<T>>();
		this.elementsIndex.add(this);
		if(parent.isRoot()){
			hash = new TreeHash<>();
		}
	}

	public T getData() {
		return data;
	}

	/**
	 * TODO: Update hash keys.
	 * @param data
	 */
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
		return addChild(childNode);
	}

	/**
	 * TODO Add to hash here.
	 * @param childNode
	 * @return
	 */
	public TreeNode<T> addChild(TreeNode<T> childNode){
		childNode.parent = this;
		childNode.elementsIndex = elementsIndex;
		this.children.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}

	/**
	 * TODO: See if this is enough for our searching needs, if not it may be useful for narrowing results.
	 * TODO: Wtf is this doing.
	 * @param cmp
	 * @return
	 */
	public TreeNode<T> findTreeNode(Comparable<T> cmp) {
		for (TreeNode<T> element : this.elementsIndex) {
			T elData = element.data;
			if (cmp.compareTo(elData) == 0)
				return element;
		}

		return null;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	/**
	 *
	 * @param node
	 */
	private void registerChildForSearch(TreeNode<T> node) {
		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node); //TODO: Why do this?
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
	 *
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
	 * TODO: rewrite using hash
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
	 * TODO: rewrite using hash
	 * QA on containsAll
	 * @param node
	 * @return
	 */
	public TreeNode<T> containsAll(T node){
		for(TreeNode<T> child : children){
			if(shallowEquals(child, node))
				return child;
			else if(isLeaf())
				return null;
			containsAll(child.data);
		}
		return null;
	}

	/**
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

	/**
	 * TODO: remove from hash
	 * @param node
	 */
	public void delChild(T node){
		children.remove(contains(node));
	}

	public boolean isBaseNode(){
		return getLevel() == 1;
	}


}
