package funct;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author devinmcgloin
 * @version 9/23/15
 */
public class StrRepTest {

    @Test
    public void testGetStringRep() throws Exception {

    }

    @Test
    public void testIsStringRepresentation() throws Exception {
        Assert.assertEquals("1: Improper String Rep Parsing", true, StrRep.isStringRepresentation("123.3"));
        Assert.assertEquals("2: Improper String Rep Parsing", false, StrRep.isStringRepresentation(".3"));
        Assert.assertEquals("3: Improper String Rep Parsing", true, StrRep.isStringRepresentation("0.3"));
        Assert.assertEquals("4: Improper String Rep Parsing", true, StrRep.isStringRepresentation("height < 2"));
        Assert.assertEquals("5: Improper String Rep Parsing", false, StrRep.isStringRepresentation("height <== 2"));
        Assert.assertEquals("6: Improper String Rep Parsing", true, StrRep.isStringRepresentation("height > 0.2"));
    }

    @Test
    public void testIsKeyStringRepresentable() throws Exception {

    }

    @Test
    public void testGetExpression() throws Exception {

    }

    @Test
    public void testIsExpression() throws Exception {

    }

    @Test
    public void testGetCount() throws Exception {

    }

    @Test
    public void testIsCount() throws Exception {

    }

    @Test
    public void testGetMeasurement() throws Exception {

    }

    @Test
    public void testIsMeasurement() throws Exception {

    }
}