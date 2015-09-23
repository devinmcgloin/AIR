package funct;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals("Expression Parsing {height < 2}", true, StrRep.isExpression("height < 2"));
        Assert.assertEquals("Expression Parsing {height <== 2}", false, StrRep.isExpression("height <== 2"));
        Assert.assertEquals("Expression Parsing {height > 0.2}", true, StrRep.isExpression("height > 0.2"));
        Assert.assertEquals("Expression Parsing {length == .2}", false, StrRep.isExpression("length == .2"));
        Assert.assertEquals("Expression Parsing {length == 0.2}", true, StrRep.isExpression("length == 0.2"));

    }

    @Test
    public void testGetCount() throws Exception {

    }

    @Test
    public void testIsCount() throws Exception {
        Assert.assertEquals("Double parsing {123.3}", true, StrRep.isCount("123.3"));
        Assert.assertEquals("Double parsing {.3}", false, StrRep.isCount(".3"));
        Assert.assertEquals("Double Parsing {0.3}", true, StrRep.isCount("0.3"));
        Assert.assertEquals("Double Parsing {0}", true, StrRep.isCount("0"));
    }

    @Test
    public void testGetMeasurement() throws Exception {

    }

    @Test
    public void testIsMeasurement() throws Exception {

    }
}