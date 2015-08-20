package pa;

import org.apache.log4j.Logger;
import r.TreeNode;
import util.Record;

import java.util.ArrayList;

/**
 * This class is never to be used outside of the static Noun class.
 *
 */
public final class NBN{

    private final BaseNode TN;

    static Logger logger = Logger.getLogger(NBN.class);


    public NBN(TreeNode TN) {
        //Do a full copy on instantiation.
        this.TN = new BaseNode(TN);
    }

    public NBN(BaseNode node){
        this.TN = node;
    }

    //Used in adding more shit it needs to change.
    public NBN(TreeNode TN, ArrayList<Record> record){
        this.TN = new BaseNode(TN, record);
    }

    /**
     * For making a new node to think about and put back later.
     *
     * @param title
     */
    public NBN(String title) {
        TN = new BaseNode(title);
    }

    public BaseNode getBaseNode(){
        return TN;
    }

    public String getTitle(){
        return TN.getTitle();
    }

    public String toString(){
        return TN.toString();
    }

    public ArrayList<String> getKeys(){
        return TN.getKeys();
    }

    public ArrayList<String> get(String key){
        return TN.get(key);
    }

    public ArrayList<String> get(String firstKey, String secondKey){
        return TN.get(firstKey, secondKey);
    }

    public ArrayList<Record> getRecord(){
        return TN.getRecord();
    }

    public NBN add(String key){
        return new NBN(TN.add(key));
    }

    public NBN add(String key, String val){
        return new NBN(TN.add(key, val));
    }

    public NBN rm(String key){
        return new NBN(TN.rm(key));
    }

    public NBN rm(String key, String val) {
        return new NBN(TN.rm(key, val));
    }

    public NBN update(String key, String oldVal, String newVal){
        return new NBN(TN.update(key, oldVal, newVal));
    }

}

