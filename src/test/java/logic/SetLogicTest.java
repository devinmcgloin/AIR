package logic;

import memory.Notepad;
import org.junit.Assert;
import org.junit.Test;
import pa.Node;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
        ArrayList<Node> logicalParents = SetLogic.getLogicalParents(number);
        Assert.assertThat(logicalParents, hasItem(Notepad.searchByTitle("ldata")));

        Node boat = Notepad.searchByTitle("boat");
        logicalParents = SetLogic.getLogicalParents(boat);
        Assert.assertThat(logicalParents, not(hasItem(Notepad.searchByTitle("ldata"))));
        Assert.assertThat(logicalParents, hasItem(Notepad.searchByTitle("transport")));
    }

    @Test
    public void testGetLogicalParent() throws Exception {

    }

    @Test
    public void testGetLogicalChildren() throws Exception {
        Node number = Notepad.searchByTitle("ldata");
        ArrayList<Node> logicalParents = SetLogic.getLogicalChildren(number);
        Assert.assertThat(logicalParents, hasItem(Notepad.searchByTitle("number")));
        Assert.assertThat(logicalParents, hasItem(Notepad.searchByTitle("measurement")));
        Assert.assertThat(logicalParents, not(hasItem(Notepad.searchByTitle("boat"))));
        Assert.assertThat(logicalParents, not(hasItem(Notepad.searchByTitle("node"))));
        Assert.assertThat(logicalParents, not(hasItem(new Node("ksjd"))));


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
        String[] setAArray = {"ldata", "bmw", "facebook"};
        ArrayList<Node> setA = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setAArray)));

        String[] setBArray = {"ldata", "audi", "facebook", ""};
        ArrayList<Node> setB = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setBArray)));

        ArrayList<Node> intersection = SetLogic.intersection(setA, setB);


        assertThat(intersection, hasItem(Notepad.searchByTitle("ldata")));
        assertThat(intersection, not(hasItem(Notepad.searchByTitle("bmw"))));
        assertThat(intersection, not(hasItem(Notepad.searchByTitle("facebook"))));


    }

    @Test
    public void testDifference() throws Exception {
        String[] setAArray = {"ldata", "bmw", "facebook"};
        ArrayList<Node> setA = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setAArray)));


        String[] setBArray = {"ldata", "audi", "facebook", ""};
        ArrayList<Node> setB = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setBArray)));


        ArrayList<Node> difference = SetLogic.difference(setA, setB);


        assertThat(difference, not(hasItem(Notepad.searchByTitle("ldata"))));
        assertThat(difference, hasItem(Notepad.searchByTitle("bmw")));
        assertThat(difference, hasItem(Notepad.searchByTitle("audi")));
        assertThat(difference, not(hasItem(Notepad.searchByTitle("facebook"))));
    }

    @Test
    public void testUnion() throws Exception {
        String[] setAArray = {"ldata", "bmw", "facebook"};
        ArrayList<Node> setA = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setAArray)));

        String[] setBArray = {"ldata", "audi", "facebook", ""};
        ArrayList<Node> setB = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setBArray)));

        ArrayList<Node> difference = SetLogic.union(setA, setB);

    }

    @Test
    public void testSupersetP() throws Exception {
        String[] setAArray = {"ldata", "bmw", "facebook"};
        ArrayList<Node> setA = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setAArray)));


        String[] setBArray = {"ldata", "facebook", ""};
        ArrayList<Node> setB = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setBArray)));


        assertThat(SetLogic.supersetP(setA, setB), is(true));
    }

    @Test
    public void testSubsetP() throws Exception {
        String[] setAArray = {"ldata", "bmw", "facebook"};
        ArrayList<Node> setA = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setAArray)));


        String[] setBArray = {"ldata", "facebook", ""};
        ArrayList<Node> setB = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setBArray)));


        assertThat(SetLogic.subsetP(setB, setA), is(true));
    }

    @Test
    public void testMemberP() throws Exception {
        String[] setAArray = {"ldata", "bmw", "facebook"};
        ArrayList<Node> setA = Notepad.searchByTitles(
                new ArrayList<String>(Arrays.asList(setAArray)));

        assertThat(SetLogic.memberP(setA, Notepad.searchByTitle("ldata")), is(true));
        assertThat(SetLogic.memberP(setA, Notepad.searchByTitle("bmw")), is(true));
        assertThat(SetLogic.memberP(setA, Notepad.searchByTitle("alks")), not(is(true)));


    }
}