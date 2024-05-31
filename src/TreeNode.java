import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

class TreeNode {
    private final int featureIdx;
    private final String dataType;
    private final double featureVal;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(int featureIdx, double featureVal, String dataType) {

        this.featureIdx = featureIdx;
        this.featureVal = featureVal;
        this.dataType = dataType;
        this.left = null;
        this.right = null;
    }

//    public String nodeDef() {
//        if (this.left != null || this.right != null) {
//            return String.format("NODE | Information Gain = %.2f | Split IF X[%d] < %.2f THEN left O/W right",
//                    this.informationGain, this.featureIdx, this.featureVal);
//        } else {
//            Map<Double, Long> labelCounts = Arrays.stream(this.data)
//                    .map(row -> row[row.length - 1])
//                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
//            String output = labelCounts.entrySet()
//                    .stream()
//                    .map(entry -> String.format("%.1f->%d", entry.getKey(), entry.getValue()))
//                    .collect(Collectors.joining(", "));
//            return String.format("LEAF | Label Counts = %s | Pred Probs = %s", output, Arrays.toString(this.predictionProbs));
//        }
//    }

    // Getters and setters for left and right
    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
