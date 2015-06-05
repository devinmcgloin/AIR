package r;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

	public T data;
	public TreeNode<T> parent;
	public List<TreeNode<T>> children;
	private List<TreeNode<T>> elementsIndex;

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
		this.elementsIndex = new LinkedList<TreeNode<T>>();
		this.elementsIndex.add(this);
	}

	public TreeNode<T> addChild(T child) {
		TreeNode<T> childNode = new TreeNode<T>(child);
		childNode.parent = this;
		this.children.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	private void registerChildForSearch(TreeNode<T> node) {
		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node);
	}

	public TreeNode<T> findTreeNode(Comparable<T> cmp) {
		for (TreeNode<T> element : this.elementsIndex) {
			T elData = element.data;
			if (cmp.compareTo(elData) == 0)
				return element;
		}

		return null;
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
	 * @return
	 */
	public TreeNode<T> toParent(){
		if(!isRoot()) {
			data = parent.data;
			children = parent.children;
			elementsIndex = parent.elementsIndex;
			parent = parent.parent;
		}
		return this;
	}

	/**
	 * TODO: Ensure that the specifiedChild is the child referenced inside current. Will most likely have to be routed through another function.
	 * TODO: Method seems to be replacing all other data with only information about itself.
	 * Makes the current node the specifiedChild.
	 * @param specifiedChild - node to be changed to.
	 * @return boolean
	 */
	public boolean toChild(TreeNode<T> specifiedChild){
		TreeNode<T> tmp = contains(specifiedChild);

		for(TreeNode<T> child : children){
			if(child.equals(tmp)){
				data = child.data;
				children = child.children;
				elementsIndex = child.elementsIndex;
				parent = child.parent;
				return true;
			}

		}
		return false;
	}

	/**
	 *
	 */
	public void toRoot(){
		while(!isRoot()){
			toParent();
		}
	}

	/**
	 * TODO: Test after fixing toChild and toParent
	 * Ensure at root, the proceed through the absolute path.
	 * NO RELATIVE PATHS EVER.
	 * @param path
	 * @return
	 */
	public TreeNode<T> changeDir(TreeNode<T>[] path){
		toRoot();
		for(TreeNode<T>child : path){
			toChild(child);
		}
		return this;
	}

	/**
	 *
	 * Checks if current node contains specified node inside children.
	 * @param node
	 * @return TreeNode<T>
	 */
	public TreeNode<T> contains(TreeNode<T> node){
		for(TreeNode<T> child : children){
			if(shallowEquals(child, node))
				System.out.println("CONTAINS FUNCTION SUCCESFUL\n");
				return child;
		}
		System.out.println("CONTAINS FUNCTION UNSUCCESFUL\n");
		return null;
	}

	/**
	 *
	 * equals compares data only, and ignores all other attributes.
	 * @param node
	 * @return
	 */
	public boolean shallowEquals (TreeNode<T> nodeA, TreeNode<T> nodeB){
		return nodeA.data.equals(nodeB.data);
	}


	private static String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}

	/**
	 * TESTING ONLY
	 */
	public void printChildren(){
		for(TreeNode<T> child : children){
			System.out.println(child);
		}
	}

	public static void main(String[] args){
		System.out.println("INSTANTIATION TEST: ");
		TreeNode<String> current = new TreeNode<String>("R");
		System.out.println("R" == current.toString());

		System.out.println("\nCHILDREN TEST: ");
		current.addChild("chair");
		current.addChild("desk");
		current.addChild("stool");
		current.printChildren();

		System.out.println("\ntoChild TEST: ");
		current.toChild(new TreeNode<String>("stool"));
		System.out.println("stool" == current.toString());

//		System.out.println("\ncontains TEST: ");
//		current.addChild("has");
//		System.out.println(current.contains(new TreeNode<String>("has")));
//
//		System.out.println("\ntoParent TEST: ");
//		current.toParent();
//		System.out.println("R" == current.toString());


	}
}
