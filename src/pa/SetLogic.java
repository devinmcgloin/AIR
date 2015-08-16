package pa;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by devinmcgloin on 6/3/15.
 * Set logic needs to create sets of like information
 * TODO: If we want a list of cities, no need to hash search for them, just go to the city node and get all of its logical children. (Ideally)
 * TODO: Implement or filtering.
 */
public final class SetLogic {

    private SetLogic(){}


    public static ArrayList<NBN> filter(ArrayList<NBN> nodes, ArrayList<String> isConditions, ArrayList<String> hasConditions, ArrayList<LDATA.Expression> LDATAConditions){
        nodes = isFilter(nodes, isConditions);
        nodes = hasFilter(nodes, hasConditions);
        nodes = LDATAFilter(nodes, LDATAConditions);
        return nodes;
    }


    public static ArrayList<NBN> isFilter(ArrayList<NBN> nodes,ArrayList<String> isConditions){
        //Is filter
        for(String term : isConditions) {
            nodes = isFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<NBN> isFilter(ArrayList<NBN> nodes, String isCondition){
        Iterator<NBN> iterator = nodes.iterator();

        while (iterator.hasNext()){
            NBN option = iterator.next();
            //Is filter

            if (!Noun.isP(option, isCondition.trim())) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }

    public static ArrayList<NBN> hasFilter(ArrayList<NBN> nodes, ArrayList<String> hasConditions){
        //Is filter
        for(String term : hasConditions) {
            nodes = hasFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<NBN> hasFilter(ArrayList<NBN> nodes, String hasCondition){
        Iterator<NBN> iterator = nodes.iterator();

        while (iterator.hasNext()){
            NBN option = iterator.next();
            //Is filter

            if (!Noun.hasP(option, hasCondition.trim())) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }

    public static ArrayList<NBN> LDATAFilter(ArrayList<NBN> nodes, ArrayList<LDATA.Expression> LDATAConditions){
        //Is filter
        for(LDATA.Expression term : LDATAConditions) {
            nodes = LDATAFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<NBN> LDATAFilter(ArrayList<NBN> nodes, LDATA.Expression LDATACondition){
        Iterator<NBN> iterator = nodes.iterator();

        while (iterator.hasNext()){
            NBN option = iterator.next();
            //Is filter
            if(!LDATA.validateP(option, LDATACondition)){
                iterator.remove();
                break;
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
