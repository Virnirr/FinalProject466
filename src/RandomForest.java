import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomForest {
    private int n_base_learner;
    private int bootstrap_size;
    private int max_depth;
    private int min_samples_leaf;
    private int min_information_gain;
    private int num_of_features_splitting;
    private final ArrayList<DecisionTree> forest = new ArrayList<>();
    // hyperparameters:
    /*
    n_base_learner
    num_of_features_splitting
    bootstrap_sample_size
    max_depth, min_samples_leaf, min_information_gain
     */
    public RandomForest(int n_base_learner,
                        int bootstrap_size) {
        this.n_base_learner = n_base_learner;
        this.bootstrap_size = bootstrap_size;
    }

    private ArrayList<ArrayList<Integer>> create_bootstrap_samples(int matrix[][]) {
        // note return type is Set( ArrayList(index of features), ArrayList(Index of label associated with features)
        ArrayList<ArrayList<Integer>> bootstrapDataRows = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < this.n_base_learner; i++) {
            ArrayList<Integer> currentBootstrapData = new ArrayList<>();
            for (int j = 0; j < this.bootstrap_size; j++) {
                int sampled_idx = random.nextInt(matrix.length);
                currentBootstrapData.add(sampled_idx);
            }
            bootstrapDataRows.add(currentBootstrapData);
        }

        // return random rows of data to be used
        return bootstrapDataRows;
    }

    public static ArrayList<Integer> getAllRows (int[][] matrix) {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            rows.add(i);
        }
        return rows;
    }

    public void train(int[][] data) {
        // Trains the model with given X features and Y labels in the datasets
        int totalAttributes = data[0].length - 1;

        ArrayList<ArrayList<Integer>> bootstrap_sample = this.create_bootstrap_samples(data);

        System.out.println(bootstrap_sample);
        System.out.println("this is the bootstrap size %d".formatted(bootstrap_sample.size()));


        // Learn base_learner number of Decision Trees
        for (int base_learner_idx = 0; base_learner_idx < this.n_base_learner; base_learner_idx++){
            DecisionTree base_learner = new DecisionTree(data);
            TreeNode currNode = base_learner.getDecisionTree();
            ArrayList<Integer> attributes =
                    (IntStream.range(0, totalAttributes))
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            base_learner.printDecisionTree(
                    data,
                    attributes, // all attributes
                    bootstrap_sample.get(base_learner_idx), // corresponding bootstrap rows (random)
                    0,
                    100,
                    currNode);
            // Add the random tree into forest
            this.forest.add(base_learner);
        }
    }
    public int prediction_list(ArrayList<Integer> features_to_predict) {
        ArrayList<Integer> pred_prob_list = new ArrayList<>();

        for (DecisionTree tree : this.forest) {
            pred_prob_list.add(tree.predictLabel(features_to_predict));
        }

        System.out.println(pred_prob_list);

        int count_of_0 = 0;
        int count_of_1 = 0;
        for (Integer pred_val : pred_prob_list) {
            if (pred_val == 0) {
                count_of_0++;
            }
            else {
                count_of_1++;
            }
        }

        return count_of_0 > count_of_1 ? 0 : 1;
    }
}
