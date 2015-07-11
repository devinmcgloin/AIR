package r;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class HashBrowns {


    protected ArrayList<String> addresses;
    protected HashMap<String, ArrayList<String> > map;
    protected String name = "";
    protected String address = "";

    HashBrowns(){
        map = new HashMap<String, ArrayList<String> >();
        addresses = new ArrayList<String>();
    }

    protected ArrayList<String> search(String key){
        //Get the list of addresses related to this node.
        addresses = map.get(key);
        if(addresses == null){
            return null;
        }
        return addresses;
    }

    protected ArrayList<TreeNodeBase> fullHashSearch(String input, GeneralTree GenTree){
        String address = "";
        ArrayList<TreeNode> hits = new ArrayList<TreeNode>();
        ArrayList<String> addresses = new ArrayList<String>();
        ArrayList<String> allAddresses = new ArrayList<String>();
        ArrayList<TreeNode> basePrep = new ArrayList<TreeNode>(); //helps eliminate dupes of Bases.
        ArrayList<TreeNodeBase> allBaseNodes = new ArrayList<TreeNodeBase>(); //just a wrapper for holding TreeNode to sort.

        String terms[] = input.split("`");

        //Should loop through all words.
        for(int i = 0; i<terms.length; i++){
            terms[i] = terms[i].trim();

            //HashSearch returns addresses related to one node name.
            addresses = GenTree.hashSearch(terms[i]);
            if(addresses == null){
                continue;
            }

            //Add those addresses to all addresses
            for(int j = 0; j<addresses.size(); j++){
                allAddresses.add(addresses.get(j));
            }
        }

        //Get the tree nodes for all addresses
        for(int i = 0; i<allAddresses.size(); i++){
            address = allAddresses.get(i);
            TreeNode tmp = GenTree.getNode(address);
            hits.add(tmp);
        }

        //Alphabetize the TreeNodes (honestly don't know if we'll ever need them, but still).
        Collections.sort(hits);


        //---------------------------OPTIMIZATION---------------------------------//
        //The nodes we're personally interested in for R-Noun are the ones that 'exist'.
        //In R's architecture they're the ones at the second dimension level. R/noun/TERM

        //Create a set of each node reversed to BASE addresses.
        for(int i = 0; i<hits.size(); i++){
            TreeNode tmp = hits.get(i);
            tmp = tmp.getBaseNode();
            if(basePrep.contains(tmp))
                continue;
            basePrep.add(tmp);
        }


        //Now create that into a sort-able TreeNodeBase array that tells how many terms it matched.
        int matchedTerms = 0;
        for(int i =0; i<basePrep.size(); i++){
            matchedTerms = 0;
            TreeNode tmp = basePrep.get(i);
            TreeNodeBase btmp = new TreeNodeBase(tmp);


            //Count how many it contains.
            //TODO this doesn't count for base nodes due to the way containsAll is defined.
            for(int j=0; j<terms.length; j++){
                if(tmp.containsAll(terms[j])){
                    matchedTerms++;
                }
            }
            //System.out.println(matchedTerms);
            btmp.setRank(matchedTerms);
            allBaseNodes.add(btmp);
        }

        //Order Base nodes by rank.
        Collections.sort(allBaseNodes);

        return allBaseNodes;

        //Then insert then into the hits arrayList.
//        for(int i=0; i<allBaseNodes.size(); i++){
//            TreeNodeBase btmp = allBaseNodes.getNoun(i);
//            //System.out.println("Add: "+btmp.getOrigin().getAddress());
//            hits.add(i, btmp.getOrigin());
//        }

//		for(int i=0; i<hits.size(); i++){
//			System.out.println("full: " + hits.getNoun(i).getAddress());
//		}

        //---------------------------OPTIMIZATION OVER---------------------------------//

        //return hits;
    }



    //TODO: will eventually need a customized "black list" of words to NOT save the addresses of.
    //i.e. has, is, parent (literally any header).
    protected boolean add(TreeNode n){
        name = n.getTitle();
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

    protected boolean del(String name, String address){

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