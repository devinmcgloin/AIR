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

        if (from.checkFirstDimension("^has") && to.checkFirstDimension("^has")) {
            children = from.getChildrenOfDimension("^has");
            for (String child : children) {
                //Check if "to" node (ferrari) already ^has those qualities. (Optimization).
                if (to.getChildrenOfDimension("^has").contains(child)) {
                    continue;
                }
                pa.add("noun", child, to.getOrigin().getAddress() + "^has/"); //brilliant, Devin

            }
        } else{
            pa.add("noun", "^has", to.getOrigin().getAddress());
            pa.add("noun", "^has", from.getOrigin().getAddress());
            inherit(to, from); //even more brilliant
            return;
        }

        //ADDED BY BLAZE (^is)
        //Adding is that now the "to" BN "^is" of the type "from" BN
        if (to.checkFirstDimension("^is")) {
            pa.add("noun",from.getOrigin().getName(), to.getOrigin().getAddress() + "^is");
        } else {
            pa.add("noun","^is", to.getOrigin().getAddress());
            pa.add("noun",from.getOrigin().getName(), to.getOrigin().getAddress() + "^is");
        }

        //First check if it already has the ^is qualification.
        //Check if null pointer.
        if(to.checkFirstDimension("^is")){
            pa.add("noun","^is", to.getOrigin().getAddress());         //add carrot header
            pa.add("noun", from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is"); //add info
        } else{
            if(  !to.getOrigin().getChild("^is").contains(from.getOrigin().getName())){
                pa.add("noun", from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
            }
        }


        //ADDED BY BLAZE 2.0 (^logicalchild)
        //Reverse adding to the "from" node (car; car --> ferrari) that ferrari is its ^logicalchild
        //First check Null pointer
        if(to.checkFirstDimension("^logicalchild")){
            pa.add("noun","^logicalchild", from.getOrigin().getAddress()); //add carrot header
            pa.add("noun",to.getOrigin().getName(), from.getOrigin().getAddress() + "^logicalchild"); //add info
        }else{
            //Check if contains information already..
            if( !from.getChildrenOfDimension("^logicalchild").contains(to.getOrigin().getName())  ){
                pa.add("noun",to.getOrigin().getName(), from.getOrigin().getAddress() + "^logicalchild");
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
        if(!pa.isNounBase(x) || !pa.isNounBase(y) ){
            System.out.println("Names supplied do not exist in R Nouns.");
        }else {

            NBN BNx = pa.getNoun(x);
            NBN BNy = pa.getNoun(y);

            inherit(BNx, BNy); //Devin's method. Just wrapped a bit for ease.
        }
        //System.out.println("BN: "+ BNx.getOrigin().getName());


    }

}
