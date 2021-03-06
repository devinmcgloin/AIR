package r;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class HashBrowns {


    static Logger logger = Logger.getLogger(HashBrowns.class);
    protected ArrayList<String> addresses;
    protected HashMap<String, ArrayList<String>> map;
    protected String name = "";
    protected String address = "";


    HashBrowns() {
        map = new HashMap<String, ArrayList<String>>();
        addresses = new ArrayList<String>();
    }

    protected ArrayList<String> search(String key) {
        //Get the list of addresses related to this node.
        addresses = map.get(key);
        if (addresses == null) {
            return null;
        }
        return addresses;
    }

    protected ArrayList<TreeNodeBase> fullHashSearch(String input, GeneralTree GenTree) {
        String address = "";
        ArrayList<TreeNode> hits = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();
        ArrayList<String> allAddresses = new ArrayList<>();
        ArrayList<TreeNode> basePrep = new ArrayList<>(); //helps eliminate dupes of Bases.
        ArrayList<TreeNodeBase> allBaseNodes = new ArrayList<>(); //just a wrapper for holding TreeNode to sort.

        String terms[] = input.split("`");

        //Should loop through all words.
        for (int i = 0; i < terms.length; i++) {
            terms[i] = terms[i].trim();

            //HashSearch returns addresses related to one node name.
            addresses = GenTree.hashSearch(terms[i]);
            if (addresses == null) {
                continue;
            }

            //Add those addresses to all addresses
            for (String address1 : addresses) {
                allAddresses.add(address1);
            }
        }

        //Get the tree nodes for all addresses
        for (String allAddress : allAddresses) {
            TreeNode tmp = GenTree.getNode(allAddress);
            hits.add(tmp);
        }

        //Alphabetize the TreeNodes (honestly don't know if we'll ever need them, but still).
        if (hits.size() != 0)
            Collections.sort(hits);
        else
            return allBaseNodes;


        //---------------------------OPTIMIZATION---------------------------------//
        //The nodes we're personally interested in for R-NOUN are the ones that 'exist'.
        //In R's architecture they're the ones at the second dimension level. R/noun/TERM

        //Create a set of each node reversed to BASE addresses.
        for (TreeNode tmp : hits) {
            tmp = tmp.getBaseNode();
            if (basePrep.contains(tmp))
                continue;
            basePrep.add(tmp);
        }


        //Now create that into a sort-able TreeNodeBase array that tells how many terms it matched.
        int matchedTerms = 0;
        for (int i = 0; i < basePrep.size(); i++) {
            matchedTerms = 0;
            TreeNode tmp = basePrep.get(i);
            TreeNodeBase btmp = new TreeNodeBase(tmp);


            //Count how many it contains.
            //TODO this doesn't count for base nodes due to the way containsAll is defined.
            for (String term : terms) {
                if (tmp.containsAll(term)) {
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
    protected boolean add(TreeNode n) {
        name = n.getTitle();
        address = n.getAddress();

        //List of current values to the name key
        if (map.get(name) != null) {
            addresses = map.get(name);
        } else {
            addresses = new ArrayList<>();
        }

        //Then it'll have to check if that add entry already exits. Although it never should.
        //TODO THIS IS WHERE ITS SLOW
//        if (addresses.contains(name)) {
//            logger.debug("HashBrowns: addresses already contained: " + name);
//            return false;
//        }

        //Then it adds this new address entry. Order of addresses doesn't matter for the time being.
        addresses.add(address);

        //Replace the old hash key (name) with the update list of addresses
        map.put(name, addresses);

        return true;
    }

    protected boolean del(String name, String address) {

        //Gets a key and address. Address is inside of arrayList of addresses.
        //Remove the address from the arrayList of addresses and add the updated list.

        if (map.get(name) != null) {
            addresses = map.get(name);
        }
//        logger.debug("Trying hash del for first time! Only on failed search.");
        addresses.remove(address);

        //Replace the old hash key (name) with the update list of addresses
        map.put(name, addresses);

        return false;
    }


}