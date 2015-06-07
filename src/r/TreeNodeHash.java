package r;

import java.util.HashMap;

/**
 * Created by devinmcgloin on 6/5/15.
 * TODO: Consider the following:
 * What are the keys? - have to be unique.
 * Values are TreeNodes?
 *
 */
public class TreeNodeHash<T> {
    HashMap<String, T > map;

    public TreeNodeHash(){
        map = new HashMap<>(100);
    }

}
