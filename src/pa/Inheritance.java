package pa;

import r.R;
import r.TreeNode;
import r.TreeNodeBase;

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
    public void inherit(PABN to, PABN from){
        ArrayList<String> children;

        if(from.getOrigin().getChild("^has") != null && to.getOrigin().getChild("^has") != null){
            children = from.getOrigin().getChild("^has").getChildrenString();
            for (String child : children)
                pa.add(child, to.getOrigin().getAddress() + "^has/"); //brilliant, Devin
        }else{
            pa.add("^has", to.getOrigin().getAddress());
            pa.add("^has", from.getOrigin().getAddress());
            inherit(to, from); //even more brilliant
            return;
        }

        //ADDED BY BLAZE
        //Adding is that now the "to" BN "^is" of the type "from" BN
        if(to.getOrigin().getChild("^is") != null){
            pa.add( from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
        }else{
            pa.add("^is", to.getOrigin().getAddress());
            pa.add( from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
        }

        //pa.add( from.getOrigin().getName() ,to.getOrigin().getAddress() + "^is");
    }

    /**
     *            EX1           EX2
     * @param x - los_angeles   ferrari
     * @param y - city          car
     */
    public void xISy(String x, String y){
        PABN BNx = pa.get(("R/noun/" + x));
        PABN BNy = pa.get(("R/noun/" + y));

        inherit(BNx, BNy); //Devin's method. Just wrapped a bit for ease.

        //System.out.println("BN: "+ BNx.getOrigin().getName());


    }

}
