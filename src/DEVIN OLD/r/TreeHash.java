package r;

import java.util.HashMap;

/**
 * Created by devinmcgloin on 6/7/15.
 *
 * TODO: Methods Needed:
 * Put values
 * Remove values
 * rename key
 * replace values
 * get values
 * contains
 */
public class TreeHash<T, T1>{
    public HashMap<T, T1> map;
    T1 tmp;

    public TreeHash(){
        map = new HashMap<>();
    }

    public T1 put(T key, T1 val){
        return map.put(key, val);
    }

    public T1 remove(T key){
        return map.remove(key);
    }

    public void replaceKey(T oldKey, T newKey){
        tmp = map.remove(oldKey);
        map.put(newKey, tmp);
    }

    public void replaceVal(T key, T1 val){
        map.replace(key, val);
    }

    public T1 get(T key){
        return map.get(key);
    }

    public boolean containsKey(T key){
        return map.containsKey(key);
    }

    public boolean containsVal(T val){
        return map.containsValue(val);
    }



}
