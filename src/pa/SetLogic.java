package pa;

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
     * TODO: Value to deal with minimum search, right now that would cause an array out of bounds error on line 46-47.
     * Need to take into account all filters.
     *
     *
     * Need to establish search term conventions here.
     * $FROM <BN> $SELECT <fields> $WHERE <condition>
     * @param searchTerms
     * @return
     */
    public ArrayList<NBN> genSet(String searchTerms){
        ArrayList<NBN> nodes;

        //Parse search terms
        String[] terms = searchTerms.split("`");
        if(terms.length == 0) {
            System.out.println("Invalid Search: genSet called with no arguments");
            return null;
        }

        //TODO what happens if there are no additional terms? Need to address.
        String is = terms[0];
        String[] has = terms[1].split(",");
        String[] conditions = terms[2].split(",");

        nodes = pa.hashSearch(is);

        //Need to use an iterator as its the only safe way to modify an array while iterating over it.
        Iterator<NBN> iterator = nodes.iterator();

        //The great filter
        while (iterator.hasNext()){
            Boolean removed = false;
            NBN option = iterator.next();

            //Is filter
            if(!option.isFilter(is)) {
                iterator.remove();
                removed = true;
                break;
            }

            //Has filter
            if(!removed) {
                for (String determiner : has) {
                    if (!option.hasFilter(determiner)) {
                        iterator.remove();
                        removed = true;
                        break;
                    }
                }
            }

            //Conditions filter
            if(!removed){
                for(String determiner : conditions){
                    if(!option.hasValue(determiner)){
                        iterator.remove();
                        removed = true;
                        break;
                    }
                }
            }

        }

        return nodes;
    }

}
