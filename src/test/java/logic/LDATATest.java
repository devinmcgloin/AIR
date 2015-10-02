package logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * @author devinmcgloin
 * @version 9/27/15
 */
public class LDATATest {

    @Test
    public void testExpressionIsValid() throws Exception {

    }

    @Test
    public void testIsValid() throws Exception {

    }

    @Test
    public void testConvert() throws Exception {

    }

    @Test
    public void testGetType() throws Exception {

    }

    @Test
    public void testGetType1() throws Exception {

    }

    @Test
    public void testIsLdata() throws Exception {

    }

    @Test
    public void testIsExpression() throws Exception {

    }

    @Test
    public void testIsUnit() throws Exception {

    }

    @Test
    public void testIsUnitsEqual() throws Exception {

    }

    @Test
    public void testIsNumeric() throws Exception {
        Assert.assertTrue("0.901", LDATA.isNumeric("0.901"));
        Assert.assertTrue("12.901", LDATA.isNumeric("12.901"));
        Assert.assertTrue("0.90100", LDATA.isNumeric("0.90100"));
        Assert.assertTrue("0", LDATA.isNumeric("0"));
        Assert.assertTrue("901", LDATA.isNumeric("901"));
        Assert.assertTrue("901231231", LDATA.isNumeric("901231231"));

        Assert.assertFalse(".123", LDATA.isNumeric(".123"));
        Assert.assertFalse(".12.3", LDATA.isNumeric(".12.3"));
        Assert.assertFalse("v.123", LDATA.isNumeric("v.123"));
        Assert.assertFalse("v3", LDATA.isNumeric("v3"));
        Assert.assertFalse("vaskj", LDATA.isNumeric("vakjsa"));

        Random r = new Random();
        String baseString = "";

        for (int i = 0; i < 50000; i++) {
            String testString = baseString
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT));

            Assert.assertFalse(testString, LDATA.isNumeric(testString));
        }


    }

    @Test
    public void testGetCastConvert() throws Exception {

    }

    @Test
    public void testGetCast() throws Exception {

    }

    @Test
    public void testAddConversion() throws Exception {

    }

    @Test
    public void testAddValRange() throws Exception {

    }

    @Test
    public void testGetValRange() throws Exception {

    }

    @Test
    public void testAddUnit() throws Exception {

    }

    @Test
    public void testGetUnits() throws Exception {

    }

    @Test
    public void testAddValue() throws Exception {

    }

    @Test
    public void testAddValue1() throws Exception {

    }
}