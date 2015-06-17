package pa;

import r.R;
import r.TreeNodeBase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by devinmcgloin on 6/3/15.
 * Set logic needs to create sets of like information
 */
public class SetLogic {

    PA pa;



    public SetLogic(PA pa){
        this.pa = pa;
    }

    /**
     * TODO super crude, but gets the idea across
     * Need to take into account all filters.
     *
     *
     * Need to establish search term conventions here.
     * $FROM <BN> $SELECT <fields> $WHERE <condition>
     * @param searchTerms
     * @return
     */
    public ArrayList<PABN> genSet(String searchTerms){
        ArrayList<PABN> nodes;

        //Parse search terms
        String[] terms = searchTerms.split("`");
        if(terms.length == 0) {
            System.out.println("Invalid Search: genSet called with no arguments");
            return null;
        }

        //TODO what happens if there are no additional terms? Need to address.
        String is = terms[0];
        String[] has = terms[1].split(",");
//        String[] conditions = terms[2].split(",");

        nodes = pa.hashSearch(is);

        //Need to use an iterator as its the only safe way to modify an array while iterating over it.
        Iterator<PABN> iterator = nodes.iterator();

        //The great filter
        while (iterator.hasNext()){
            PABN option = iterator.next();

            //Is filter
            if(!option.isFilter(is)) {
                iterator.remove();
                break;
            }

            //Has filter
            for (String determiner : has){
                if(!option.hasFilter(determiner)) {
                    iterator.remove();
                    break;
                }
            }

            //Conditions filter
//            for(String determiner : conditions){
//                if(!option.hasValue(determiner)){
//                    iterator.remove();
//                    continue;
//                }
//            }

        }

        return nodes;
    }

}
