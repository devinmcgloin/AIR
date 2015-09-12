package funct;

import pa.Node;

/**
 * Created by devinmcgloin on 8/25/15.
 * TODO for translating string representations from strings into nodes and back again.
 */
public class StringRepresentation {


    /**
     * This is far easier for Devin than the previous idea:
     * isKeyStringRepresentatble(Node key)     here you would submit "height" as a node to see that it takes a string rep.
     * Wait no i still need that function done.
     * @param s
     * @return
     */
    public static boolean isStringRepresentation(String s){

        return false;
    }

    /**
     * If I pass in the "Height" node from RN i need to know the values that go under it will be string representable.
     * This is important in the construction of the ghost tree (which is responsible for searches and adding and deleting.
     *
     * I need it because if I get to a Key and I see it has no Value I need to handle creating either a GhostValue (a CI on a range
     * of height) or if I need to create a GhostOF node (which is a LC of the Key). Those two are handled entirely differently.
     *
     * @param key
     * @return
     */
    public static boolean isKeyStringRepresentable(Node key){

        return false;
    }

}
