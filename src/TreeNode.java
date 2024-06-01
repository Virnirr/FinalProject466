import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

class TreeNode {
    private int featureIdx;
    private int featureVal;
    private ArrayList<TreeNode> paths;
    private int label;
    public TreeNode(int featureIdx, int featureVal, ArrayList<TreeNode> paths, int label) {
        this.featureIdx = featureIdx;
        this.featureVal = featureVal;
        this.label = label;
        this.paths = paths;
    }

    // Getters and setters for left and right
    public void addTreePath(TreeNode path) {
        this.paths.add(path);
    }
    public ArrayList<TreeNode> getPaths () {
        return this.paths;
    }

    public boolean is_leaf() {
        return this.paths == null && this.featureIdx == -1 && this.featureVal == -1;
    }

    public int getLabel() {return this.label;}

    public int getFeatureIdx() {
        return featureIdx;
    }

    public int getFeatureVal() {
        return featureVal;
    }
}
