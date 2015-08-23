package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/8/15.
 * TODO: Implement this, develop query language for tables.
 * This is going to rely heavily on the more complex aspects of LDATA. Tabling for now.
 */
public class MatrixBuilder {

    /**
     * TODO Naive attempt, need to rewrite
     * @param nodes
     * @param attributes
     * @return
     */
    public static String [][] genMatrix(ArrayList<Node> nodes, ArrayList<String> attributes){
        //[rows][Col]
        String [][] matrix = new String [nodes.size()][attributes.size()];

        for(int i = 0; i < nodes.size(); i++){
            for(int j = 0; j < attributes.size(); j++){
                matrix[i][j] = Node.get(nodes.get(i), attributes.get(j)).get(0);
            }
        }
        return matrix;
    }

}
