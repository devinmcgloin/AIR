package parse;

import funct.Core;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class TreeTest {

    @Test
    public void testGetRoot() throws Exception {
        Tree<String> tree = new Tree<>("root");
        Core.println(tree.toString());
        tree.addValue("root", "branch1");
        tree.addValue("root", "branch2");
        Core.println(tree.toString());
        tree.addValue("branch1", "leaf1");
        tree.addValue("branch1", "leaf2");
        Core.println(tree.toString());

    }

    @Test
    public void testRemoveValue() throws Exception {

    }

    @Test
    public void testAddValue() throws Exception {

    }
}