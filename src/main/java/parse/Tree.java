package parse;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class Tree<T> {
    private static Logger logger = Logger.getLogger(Tree.class);
    final Node<T> root;

    public Tree(T root){
        this.root = new Node<T>(root);
    }

    public T getRoot(){
        return root.data;
    }

    public void walk(Node<T> element, List<Node<T>> list) {
        list.add(element);
        for (Node<T> data : element.children) {
            walk(data, list);
        }
    }

    public void removeValue(T element){
        removeValue(root, element);
    }

    private void removeValue(Node<T> node, T element){
        if(element.equals(node.data))
            logger.warn("Cannot remove the root");
        for(Node<T> data : node.children){
            if(data.data.equals(element))
                node.children.remove(element);
            else
                removeValue(data, element);
        }
    }

    public void addValue(T element, T itemToAdd){
        addValue(root, element, itemToAdd);
    }

    private void addValue(Node<T> node, T element, T itemToAdd){
        for(Node<T> data : node.children){
            if(data.data.equals(element)) {
                data.children.add(new Node<T>(itemToAdd));
                return;
            }
            addValue(data, element, itemToAdd);
        }
        if(node.children.isEmpty())
            node.children.add(new Node<T>(itemToAdd));
    }

    public String toString(){
        List<Node<T>> list = new ArrayList<>();
        walk(root,list );
        return list.toString();
    }

    private StringBuffer toString(Node<T> node){
        StringBuffer buffer = new StringBuffer();
        for(Node<T> child : node.children){
            buffer.append(child).append(",");
            buffer.append(toString(child));
        }
        return buffer;
    }

    class Node<E>{
        public Node(E data){
            this.data = data;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            final Node<?> node = (Node<?>) o;

            return new EqualsBuilder()
                    .append(data, node.data)
                    .isEquals();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(data)
                    .toHashCode();
        }

        E data;
        ArrayList<Node<E>> children = new ArrayList<Node<E>>();
    }
}
