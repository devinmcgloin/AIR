package util;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/15/15.
 */
public final class keyValPair {
    public final String key;
    public final Object val;

    public String getKey() {
        return key;
    }

    public Object getVal() {
        return val;
    }

    public keyValPair(String key, Object val) {
        this.key = key;
        this.val = val;
    }

    public static Object getVal(ArrayList<keyValPair> pairs, String key){
        for(keyValPair pair : pairs){
            if(pair.getKey().equals(key))
                return pair.getVal();
        }
        return null;
    }
}
