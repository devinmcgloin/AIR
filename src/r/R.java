package r;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import r.TreeNode;

/**
 * Created by devinmcgloin on 6/5/15.
 */
public class R {


    TreeNode<String> current;
    String tmp;
    final String FILEEXTENSION = "./R/";
    File rFolder = new File(FILEEXTENSION);

    public void loadDB(String dbName){

        FileReader in = null;
        BufferedReader br = null;
        String line = "";
        String name = "";
        String lastAdded = "";
        int i = 0;
        int curTabs = 0;
        int prevTabs = 0;

        //Open File
        try {
            in = new FileReader(FILEEXTENSION + dbName);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid database name.");
            return;
        }

        //Create a reader
        br = new BufferedReader(in);
        try {
            //int tst = 0;
            //LOOP: Read line by line. "name" of node starts at first character, ends at "\r\n"
            while ( (line = br.readLine()) != null ) {
                //Break line into characters to count number of tabs. (Four spaces per tab).
                i = 0;

                while(line.charAt(i) == ' '){
                    i++;
                }
                //tst++;
                //System.out.println(tst);

                prevTabs = curTabs;
                curTabs = i/4;
                lastAdded = name;
                name = line.trim();

                //Adding to the same level.
                if(prevTabs == curTabs){
                    tmp = new String(name);
                    current.addChild(tmp);
                }
                //Up a level (always increments by 1)
                else if(prevTabs < curTabs){
                    root.(lastAdded);
                    tmp = new String(name);
                    current.addChild(tmp);

                }
                //Most complex. Going backwards by some number of levels in the tree.
                else{
                    for(i = prevTabs-curTabs; i>0; i--){
                        goBack();
                    }
                    tmp = new String(name);
                    current.addChild(tmp);
                    hash.add(tmp);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Loop back to R/
        while(current.getParent().getParent()!= null){
            goBack();
        }

    }

    private static String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }
}
