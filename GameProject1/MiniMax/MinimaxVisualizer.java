package MiniMax;

import HelperClasses.GameNode;

import java.util.ArrayList;

/**
 * The point of this class is to help visualize
 * what is happening in mini-max in order to ensure
 * the algorithm is functioning properly.
 */
public class MinimaxVisualizer {
    /**
     * First, I need to print out the entire tree. I should emphasize the path being taken somehow as I print it.
     */
    public static void visualizeTree(GameNode root, int depth){
        //get current
        GameNode current = root;
        ArrayList<GameNode> layer = new ArrayList<>();
        //get all of the children at this tier
        while (!current.getChildren().isEmpty()){
            layer.addAll(current.getChildren());
        }
    }
}
