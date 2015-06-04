package r;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Blazej on 6/1/2015.
 */
public class HashSearch {

    //TODO: change

    ArrayList<TreeNode> hits; //first BaseNodes then TreeNodes where we derived BaseNodes from.
    //TODO: Consider. are we ever going to need to know the TreeNodes (term matched nodes in tree) that we derived the base nodes from?

    HashSearch(){
        hits = new ArrayList<TreeNode>();
    }

    /**
     * For the purposes of AIR it returns an ArrayList of TreeNodes, with TreeNodeBase nodes at the
     * very beginning. This is cause they are the BASE dimension words we are interested in for PA's
     *
     * @param input    - search input coming from cmd.
     * @param GenTree - Reference to the genTree General Tree we're currently using.
     *
     * @return - ArrayList of hit nodes, ranked by most terms matched within a BASE node.
     */
    public ArrayList<TreeNodeBase> hashSearch(String input, GeneralTree GenTree){
        String address = "";
        hits = new ArrayList<TreeNode>();
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
        //In R's architecture they're the ones at the second dimension level. R/Noun.txt/TERM

        //Create a set of each node reversed to BASE addresses.
        for(int i = 0; i<hits.size(); i++){
            TreeNode tmp = hits.get(i);
            tmp = tmp.getBaseNode(tmp);
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
            for(int j=0; j<terms.length; j++){
                if(tmp.containsAnyChildWithName(terms[j])){
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
//            TreeNodeBase btmp = allBaseNodes.get(i);
//            //System.out.println("Add: "+btmp.getOrigin().getAddress());
//            hits.add(i, btmp.getOrigin());
//        }

//		for(int i=0; i<hits.size(); i++){
//			System.out.println("full: " + hits.get(i).getAddress());
//		}

        //---------------------------OPTIMIZATION OVER---------------------------------//

        //return hits;
    }

}
