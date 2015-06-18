package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/3/15.
 * Inheritance is going to be needed to create new nodes that pull in the HAS qualities only.
 */
public class Inheritance {
    PA pa;


    public Inheritance(PA pa){
        this.pa = pa;
    }
    /**
     *
     * @param from - Node the current node is getting its has attributes from.
     */
    public void inherit(NBN to, NBN from) {
        ArrayList<String> children;

        if (from.getOrigin().getChild("^has") != null && to.getOrigin().getChild("^has") != null) {
            children = from.getOrigin().getChild("^has").getChildrenString();
            for (String child : children) {
                //Check if "to" node (ferrari) already ^has those qualities. (Optimization).
                if (to.getOrigin().getChild("^has").contains(child)) {
                    continue;
                }
                pa.add(child, to.getOrigin().getAddress() + "^has/"); //brilliant, Devin

            }
        } else{
            pa.add("^has", to.getOrigin().getAddress());
            pa.add("^has", from.getOrigin().getAddress());
            inherit(to, from); //even more brilliant
            return;
        }

        //ADDED BY BLAZE (^is)
        //Adding is that now the "to" BN "^is" of the type "from" BN
        if (to.getOrigin().getChild("^is") != null) {
            pa.add(from.getOrigin().getName(), to.getOrigin().getAddress() + "^is");
        } else {
            pa.add("^is", to.getOrigin().getAddress());
            pa.add(from.getOrigin().getName(), to.getOrigin().getAddress() + "^is");
        }

        //First check if it already has the ^is qualification.
        if( to.getOrigin().getChild("^is").contains(from.getOrigin().getName()) ){

        } else{
            if(to.getOrigin().getChild("^is") != null  ){
                pa.add( from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
            }else{
                pa.add("^is", to.getOrigin().getAddress());
                pa.add( from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
            }
        }


        //ADDED BY BLAZE 2.0 (^logicalchild)
        //Reverse adding to the "from" node (car; car --> ferrari) that ferrari is its ^logicalchild
        //First check if that information already exists.
        //TODO Throws null pointer
        if( from.getOrigin().getChild("^logicalchild").contains(to.getOrigin().getName()) ){

        }else{
            if(from.getOrigin().getChild("^logicalchild") != null  ){
                pa.add(to.getOrigin().getName(), from.getOrigin().getAddress() + "^logicalchild");
            }else{
                pa.add("^logicalchild", from.getOrigin().getAddress());
                pa.add(to.getOrigin().getName(), from.getOrigin().getAddress() + "^logicalchild");
            }
        }



        //pa.add( from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
    }

    /**
     *            EX1           EX2
     * @param x - los_angeles   ferrari
     * @param y - city          car
     */
    public void xISy(String x, String y){
        //Check if those names exist in RNouns
        if(!pa.getNoun("R/noun").getOrigin().contains(x) || !pa.getNoun("R/noun").getOrigin().contains(y) ){
            System.out.println("Names supplied do not exist in R Nouns.");
        }

        NBN BNx = pa.getNoun(("R/noun/" + x));
        NBN BNy = pa.getNoun(("R/noun/" + y));

        inherit(BNx, BNy); //Devin's method. Just wrapped a bit for ease.

        //System.out.println("BN: "+ BNx.getOrigin().getName());


    }

}
