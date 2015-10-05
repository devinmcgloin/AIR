package funct;

import factory.NodeFactory;
import org.junit.Test;
import pa.Node;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author devinmcgloin
 * @version 10/3/15.
 */
public class FormatterTest {

    @Test
    public void testFormatList() throws Exception {
        String[] array = {"one", "two", "three", "four", "five"};
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(array));
        String output = Formatter.formatList(list);
        assertThat(output, startsWith("["));
        assertThat(output, endsWith("]"));
        assertThat(output, allOf(containsString("one"), containsString("four"), containsString("five")));


    }

    @Test
    public void testFormatList1() throws Exception {
        String output = Formatter.formatList("one", "two", "three", "four", "five");
        assertThat(output, startsWith("["));
        assertThat(output, endsWith("]"));
        assertThat(output, allOf(containsString("one"), containsString("four"), containsString("five")));
    }


    @Test
    public void testViewNode() throws Exception {
        Node n = new NodeFactory().init("test")
                .addName("testing")
                .addLC("firstLC")
                .addLC("secondLC")
                .addLP("firstLP")
                .setFields(new String[]{"key1", "key2"}, new String[]{"val1", "va2"})
                .build();
        String format = Formatter.viewNode(n);
        assertThat(format, allOf(containsString("test"), containsString("testing"), containsString("key1")));
    }

    @Test
    public void testQuickView() throws Exception {
        Node n = new NodeFactory().init("test")
                .addName("testing")
                .addLP("firstLP")
                .build();
        String quickView = Formatter.quickView(n);
        assertThat(quickView, containsString("test"));
        assertThat(quickView, endsWith("N/A"));


        n = new NodeFactory().init("test")
                .addName("testing")
                .addLP("ldata")
                .build();
        quickView = Formatter.quickView(n);
        assertThat(quickView, allOf(containsString("test"), containsString("N/A")));
    }
}