package funct;

import pa.Node;

import java.util.ArrayList;

/**
 * This is going to rely heavily on the more complex aspects of LDATA. Tabling for now.
<<<<<<< HEAD:src/funct/MatrixBuilder.java
 * @author devinmcgloin
 * @version 6/8/15.
 *
=======
 *
 * @author devinmcgloin
 * @version 6/8/15.
>>>>>>> maven-finally:src/main/java/funct/MatrixBuilder.java
 */
public class MatrixBuilder {

    /**
     * TODO Naive attempt, need to rewrite
     *
     * @param nodes
     * @param attributes
     * @return
     */
    public static String[][] genMatrix(ArrayList<Node> nodes, ArrayList<String> attributes) {
        //[rows][Col]
        String[][] matrix = new String[nodes.size()][attributes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < attributes.size(); j++) {
                matrix[i][j] = Node.get(nodes.get(i), attributes.get(j));
            }
        }
        return matrix;
    }

}
