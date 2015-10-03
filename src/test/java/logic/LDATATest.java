package logic;

import funct.Predicate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
        //count tests
        assertThat("height < 13", Predicate.isExpression("height < 13"), is(true));
        assertThat("height > 1.3", Predicate.isExpression("height > 1.3"), is(true));
        assertThat("height < 13v", Predicate.isExpression("height < 13v"), not(is(true)));
        assertThat("height < .13", Predicate.isExpression("height < .13"), not(is(true)));
        assertThat("height <- 13", Predicate.isExpression("height <- 13"), not(is(true)));
        assertThat("height =< 13v", Predicate.isExpression("height =< 13v"), not(is(true)));
        assertThat("height =< 13", Predicate.isExpression("height =< 13"), not(is(true)));
        assertThat("height <. 13v", Predicate.isExpression("height <. 13v"), not(is(true)));
        assertThat("height <= 13.0", Predicate.isExpression("height <= 13.0"), is(true));

        //todo measurment tests cant complete now due to lack of unit types in the db
    }

    @Test
    public void testIsUnit() throws Exception {

    }

    @Test
    public void testIsUnitsEqual() throws Exception {

    }

    @Test
    public void testIsNumeric() throws Exception {
        Assert.assertTrue("0.901", Predicate.isNumeric("0.901"));
        Assert.assertTrue("12.901", Predicate.isNumeric("12.901"));
        Assert.assertTrue("0.90100", Predicate.isNumeric("0.90100"));
        Assert.assertTrue("0", Predicate.isNumeric("0"));
        Assert.assertTrue("901", Predicate.isNumeric("901"));
        Assert.assertTrue("901231231", Predicate.isNumeric("901231231"));

        Assert.assertFalse(".123", Predicate.isNumeric(".123"));
        Assert.assertFalse(".12.3", Predicate.isNumeric(".12.3"));
        Assert.assertFalse("v.123", Predicate.isNumeric("v.123"));
        Assert.assertFalse("v3", Predicate.isNumeric("v3"));
        Assert.assertFalse("vaskj", Predicate.isNumeric("vakjsa"));

        Random r = new Random();
        String baseString = "";

        for (int i = 0; i < 50000; i++) {
            String testString = baseString
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT));

            Assert.assertFalse(testString, Predicate.isNumeric(testString));
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