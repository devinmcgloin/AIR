package funct;

import org.junit.Assert;
import org.junit.Test;
import parse.Parser;

/**
 * @author devinmcgloin
 * @version 9/23/15
 */
public class StrRepTest {


    @Test
    public void testIsKeyStringRepresentable() throws Exception {

    }

    @Test
    public void testGetExpression() throws Exception {

    }

    @Test
    public void testIsExpression() throws Exception {
        Assert.assertEquals("Expression Parsing {height < 2}", true, Parser.isExpression.apply("height < 2"));
        Assert.assertEquals("Expression Parsing {height <== 2}", false, Parser.isExpression.apply("height <== 2"));
        Assert.assertEquals("Expression Parsing {height > 0.2}", true, Parser.isExpression.apply("height > 0.2"));
        Assert.assertEquals("Expression Parsing {length == .2}", false, Parser.isExpression.apply("length == .2"));
        Assert.assertEquals("Expression Parsing {length == 0.2}", true, Parser.isExpression.apply("length == 0.2"));

    }

    @Test
    public void testGetCount() throws Exception {

    }

    @Test
    public void testIsCount() throws Exception {
        Assert.assertEquals("Double parsing {123.3}", true, Parser.isCount.apply("123.3"));
        Assert.assertEquals("Double parsing {.3}", false, Parser.isCount.apply(".3"));
        Assert.assertEquals("Double Parsing {0.3}", true, Parser.isCount.apply("0.3"));
        Assert.assertEquals("Double Parsing {0}", true, Parser.isCount.apply("0"));
    }

    @Test
    public void testGetMeasurement() throws Exception {

    }

    @Test
    public void testIsMeasurement() throws Exception {

    }
}