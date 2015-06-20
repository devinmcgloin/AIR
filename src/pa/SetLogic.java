package pa;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by devinmcgloin on 6/3/15.
 * Set logic needs to create sets of like information
 * TODO: If we want a list of cities, no need to hash search for them, just go to the city node and get all of its logical children.
 */
public class SetLogic {

    PA pa;



    public SetLogic(PA pa){
        this.pa = pa;
    }

    /**
     * TODO super crude, but gets the idea across
     * TODO: Value to deal with minimum search, right now that would cause an array out of bounds error on line 46-47.
     * TODO: make it possible to generate a set with multiple is statements.
     *
     *
     * Need to take into account all filters.
     *
     * Need to establish search term conventions here.
     * "city`population,post_office`length >= 5 meters"
     * @param searchTerms
     * @return
     */
    public ArrayList<NBN> genSet(String searchTerms){
        ArrayList<NBN> nodes;
        String[] isConditions;
        String[] has;
        String[] conditions;

        //Parse search terms
        String[] terms = searchTerms.split("`");
        if(terms.length == 0) {
            System.out.println("Invalid Search: genSet called with no arguments");
            return null;
        }else if(terms.length == 1){
            isConditions = terms[0].split(",");
            has = null;
            conditions = null;
        }else if(terms.length == 2){
            isConditions = terms[0].split(",");
            has = terms[1].split(",");
            conditions = null;
        }else {
            isConditions = terms[0].split(",");
            has = terms[1].split(",");
            conditions = terms[2].split(",");
        }

        nodes = pa.nounHashSearch(terms[0].replace(",", "`"));

        //Need to use an iterator as its the only safe way to modify an array while iterating over it.
        Iterator<NBN> iterator = nodes.iterator();

        //The great filter
        while (iterator.hasNext()){
            Boolean removed = false;
            NBN option = iterator.next();

            //Is filter
            for(String term : isConditions) {
                if (!option.isFilter(term.trim())) {
                    iterator.remove();
                    removed = true;
                    break;
                }
            }

            //Has filter
            if(!removed && has != null) {
                for (String determiner : has) {
                    if (!option.hasFilter(determiner.trim())) {
                        iterator.remove();
                        removed = true;
                        break;
                    }
                }
            }

            //Conditions filter
            if(!removed && conditions != null){
                for(String determiner : conditions){
                    if(!option.hasValue(determiner.trim())){
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
