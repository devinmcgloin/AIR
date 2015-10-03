package logic;

import funct.Core;
import funct.Formatter;
import memory.Notepad;
import org.junit.Test;
import pa.Node;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 10/2/15.
 */
public class SetLogicTest {

    @Test
    public void testIsValid() throws Exception {

    }

    @Test
    public void testFilter() throws Exception {

    }

    @Test
    public void testNameFilter() throws Exception {

    }

    @Test
    public void testIsFilter() throws Exception {

    }

    @Test
    public void testIsFilter1() throws Exception {

    }

    @Test
    public void testHasFilter() throws Exception {

    }

    @Test
    public void testHasFilter1() throws Exception {

    }

    @Test
    public void testLDATAFilter() throws Exception {

    }

    @Test
    public void testLDATAFilter1() throws Exception {

    }

    @Test
    public void testGetLogicalParents() throws Exception {
        Node number = Notepad.searchByTitle("number");
        Core.println(Formatter.viewNode(number));
        ArrayList<Node> logicalParents = SetLogic.getLogicalParents(number);
        Core.println(Formatter.formatList(logicalParents));
//        Assert.assertThat(logicalParents, hasItem(Notepad.searchByTitle("ldata")));
    }

    @Test
    public void testGetLogicalParent() throws Exception {

    }

    @Test
    public void testGetLogicalChildren() throws Exception {

    }

    @Test
    public void testXISyP() throws Exception {

    }

    @Test
    public void testXINHERITy() throws Exception {

    }

    @Test
    public void testXLikey() throws Exception {

    }

    @Test
    public void testHasP() throws Exception {

    }

    @Test
    public void testIntersection() throws Exception {

    }

    @Test
    public void testDifference() throws Exception {

    }

    @Test
    public void testUnion() throws Exception {

    }

    @Test
    public void testSupersetP() throws Exception {

    }

    @Test
    public void testSubsetP() throws Exception {

    }

    @Test
    public void testMemberP() throws Exception {

    }
}