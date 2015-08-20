package util;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/15/15.
 */
public final class keyVal {
    public final String type;
    public final String key;
    public final Object val;

    public String getKey() {
        return key;
    }

    public Object getVal() {
        return val;
    }

    public keyVal(String key, Object val) {
        type = null;
        this.key = key;
        this.val = val;
    }

    public keyVal(String type, String key, Object val) {
        this.type = type;
        this.key = key;
        this.val = val;
    }

    public static Object getVal(ArrayList<keyVal> pairs, String key){
        for(keyVal pair : pairs){
            if(pair.getKey().equals(key))
                return pair.getVal();
        }
        return null;
    }
}
