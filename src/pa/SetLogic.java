package pa;


import java.util.ArrayList;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by devinmcgloin on 6/3/15.
 * Set logic needs to create sets of like information
 * TODO: If we want a list of cities, no need to hash search for them, just go to the city node and get all of its logical children. (Ideally)
 */
public final class SetLogic {

    private SetLogic(){    }

    /**
     * TODO: Value to deal with minimum search, right now that would cause an array out of bounds error on line 46-47.
     * TODO: Implement genset range filtering.
     * TODO: Implement genset or filtering.
     *
     * Need to take into account all filters.
     *
     * Need to establish search term conventions here.
     * "city`population,post_office`length >= 5 meters"
     * @param searchTerms
     * @return
     */
    public static ArrayList<NBN> genSet(String searchTerms){
        ArrayList<NBN> nodes;
        String[] isConditions;
        String[] hasConditions;
        ArrayList<LDATA.Expression> LDATAConditions;

        //Parse search terms
        String[] terms = searchTerms.split("`");

        if(terms.length == 0) {
            System.out.println("Invalid Search: genSet called with no arguments");
            return null;
        }else if(terms.length == 1){
            isConditions = terms[0].split(",");
            hasConditions = null;
            LDATAConditions = null;
        }else if(terms.length == 2){
            isConditions = terms[0].split(",");
            hasConditions = terms[1].split(",");
            LDATAConditions = null;
        }else {
            isConditions = terms[0].split(",");
            hasConditions = terms[1].split(",");
            LDATAConditions = new ArrayList<LDATA.Expression>();
            for (String condition : terms[2].split(",")){
                LDATAConditions.add(Reader.parseExpression(condition));
            }
        }

        nodes = PA.nounHashSearch(terms[0].replace(",", "`"));


        return filter(nodes, isConditions, hasConditions, LDATAConditions);
    }

    public static ArrayList<NBN> filter(ArrayList<NBN> nodes, String[] isConditions, String[] hasConditions, ArrayList<LDATA.Expression> LDATAConditions){
        nodes = isFilter(nodes, isConditions);
        nodes = hasFilter(nodes, hasConditions);
        nodes = LDATAFilter(nodes, LDATAConditions);

        return nodes;
    }


    public static ArrayList<NBN> isFilter(ArrayList<NBN> nodes, String[] isConditions){
        Iterator<NBN> iterator = nodes.iterator();

        while (iterator.hasNext()){
            NBN option = iterator.next();
            //Is filter
            for(String term : isConditions) {
                if (!Noun.isP(option, term.trim())) {
                    iterator.remove();
                    break;
                }
            }
        }
        return nodes;
    }

    public static ArrayList<NBN> hasFilter(ArrayList<NBN> nodes, String[] hasConditions){
        Iterator<NBN> iterator = nodes.iterator();

        while (iterator.hasNext()){
            NBN option = iterator.next();
            //Is filter
            for(String term : hasConditions) {
                if (!Noun.hasP(option, term.trim())) {
                    iterator.remove();
                    break;
                }
            }
        }
        return nodes;
    }

    public static ArrayList<NBN> LDATAFilter(ArrayList<NBN> nodes, ArrayList<LDATA.Expression> LDATAConditions){
        Iterator<NBN> iterator = nodes.iterator();

        while (iterator.hasNext()){
            NBN option = iterator.next();
            //Is filter
            for(LDATA.Expression determiner : LDATAConditions){
                if(!LDATA.validateP(option, determiner)){
                    iterator.remove();
                    break;
                }
            }
        }
        return nodes;
    }

    public static boolean xISyP(NBN x, NBN y){

        if(y == null || x == null)
            return false;

        //Get the logical children of the "parent" node
        ArrayList<String> logicalChildren = Noun.get(y, "^logicalChildren");

        //If there are no logical children, clearly this is false.
        if(logicalChildren == null || logicalChildren.isEmpty()){
            return false;
        }

        //If the title of the logical child is contained in the logicalChildren of the logical parent, everything is fine.
        if( logicalChildren.contains( Noun.getTitle(x) ) ){
            return true;
        }

        //Technically we should also check to make sure that there isn't information written in reverse. In case something wasn't reflexively written in.

        return false;
    }

    /**
     *
     * @param x - the child
     * @param y - the parent
     * @return
     */
    public static NBN xINHERITy(NBN x, NBN y){

        //SR-71 Blackbird   INHERITS        supersonic jet
        x = Noun.add(x, "^logicalParents", Noun.getTitle(y));

        //supersonice jet   gets        SR-71 Blackbird     as a child
        y = Noun.add(y, "^logicalChildren", Noun.getTitle(x));

        //Additional logic:
        //Now the child gets all the keys from the parent, with the exception of the carrot headers. (Even though those should be fine...)
        ArrayList<String> keys = Noun.getKeys(y);
        for(String key: keys){
            x = Noun.add(x, key);
        }

        PA.put(y);

        return x;
    }


}
