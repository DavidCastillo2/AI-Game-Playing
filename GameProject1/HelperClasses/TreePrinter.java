package HelperClasses;

import java.util.ArrayList;

public class TreePrinter {
    public static String lengthOfEachLevel(GameNode root){
        StringBuilder retVal = new StringBuilder();
        ArrayList<GameNode> cur = new ArrayList<>();
        cur.add(root);

        //For each layer
        int i = 0;
        while(true) {
            ArrayList<GameNode> next = new ArrayList<>();

            long count = 0;
            retVal.append("Layer: ").append(i).append("\t NumberOfNodes: ");

            // For each node in the current Layer
            for (GameNode child : cur) {

                // Get all children
                next.addAll(child.getChildren());
                count += next.size();
            }
            cur = next;
            if (next.size() == 0) break;
            i++;
            retVal.append(count).append("\n");
        }
        return retVal.toString();
    }


    public static String utilityOfEachNode(GameNode root, int maxDepth){
        StringBuilder retVal = new StringBuilder();
        ArrayList<GameNode> prev = new ArrayList<>();
        prev.add(root);

        //For each layer
        for (int i=0; i < maxDepth; i++){
            ArrayList<GameNode> nextPrev = new ArrayList<>();

            // For each node in previous layer
            for (GameNode cur: prev){

                // Get all children
                ArrayList<GameNode> children = cur.getChildren();
                for (GameNode c : children) {
                    retVal.append(c.getUtility()).append(" ");
                }
                retVal.append(" | ");
                nextPrev.addAll(children);
            }
            retVal.append("\n");
            prev = nextPrev;
            if (prev.size() == 0) break;
        }
        return retVal.toString();
    }
}
