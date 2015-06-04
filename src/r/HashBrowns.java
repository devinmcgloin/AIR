package r;

import java.util.ArrayList;
import java.util.HashMap;


public class HashBrowns {

    TreeNode node;
    ArrayList<String> addresses;
    HashMap<String, ArrayList<String> > map;
    String name = "";
    String address = "";

    HashBrowns(){
        map = new HashMap<String, ArrayList<String> >();
        addresses = new ArrayList<String>();
    }

    public ArrayList<String> search(String key){
        //Get the list of addresses related to this node.
        addresses = map.get(key);
        if(addresses == null){
            return null;
        }
        return addresses;
    }

    //TODO: will eventually need a customized "black list" of words to NOT save the addresses of.
    //i.e. has, is, parent (literally any header).
    public boolean add(TreeNode n){
        name = n.getName();
        address = n.getAddress();

        //List of current values to the name key
        if( map.get(name) != null){
            addresses = map.get(name);
        }
        else{
            addresses = new ArrayList<String>();
        }

        //Then it'll have to check if that add entry already exits. Although it never should.
        if(addresses.contains(name) ){
            System.out.println("HashBrowns: addresses already contained: "+ name);
            //TODO: See like ^ should that be an error. There should be one larger error log for AIR.
            return false;
        }

        //Then it adds this new address entry. Order of addresses doesn't matter for the time being.
        addresses.add(address);

        //Replace the old hash key (name) with the update list of addresses
        map.put(name, addresses);

        return true;
    }

    public boolean del(String name, String address){

        //Gets a key and address. Address is inside of arrayList of addresses.
        //Remove the address from the arrayList of addresses and add the updated list.

        if( map.get(name) != null){
            addresses = map.get(name);
        }
        System.out.println("Trying hash del for first time! Only on failed search.");
        addresses.remove(address);

        //Replace the old hash key (name) with the update list of addresses
        map.put(name, addresses);

        return false;
    }


}