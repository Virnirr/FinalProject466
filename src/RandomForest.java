import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
public class RandomForest {
    private int n_base_learner;
    private int max_depth;
    private int min_samples_leaf;
    private int min_information_gain;
    private int num_of_features_splitting;


    // hyperparameters:
    /*
    n_base_learner
    num_of_features_splitting
    bootstrap_sample_size
    max_depth, min_samples_leaf, min_information_gain
     */
    public RandomForest(int n_base_learner,
                        int max_depth,
                        int min_samples_leaf,
                        int min_information_gain,
                        int num_of_features_splitting) {
        this.n_base_learner = n_base_learner;
        this.max_depth = max_depth;
        this.min_samples_leaf = min_samples_leaf;
        this.min_information_gain = min_information_gain;
        this.num_of_features_splitting = num_of_features_splitting;
    }

    private <T> ArrayList<T> create_bootstrap_sampels(int matrix[][]) {
        // note return type is Set( ArrayList(index of features), ArrayList(Index of label associated with features)
        ArrayList<ArrayList<Integer>> randomFeatures = new ArrayList<>();
        ArrayList<Integer> randomLabels = new ArrayList<>();

        for (int i = 0; i < this.n_base_learner; i++) {
            Random random = new Random();
            int sampled_idx = random.nextInt(matrix[0].length);
            ArrayList<Integer> selectedFeatures = new ArrayList<>();
            // add everything except for the label to sample arraylist
            for (int j = 0; j < matrix[0].length - 1; j++){
                selectedFeatures.add(matrix[sampled_idx][j]);
            }
            randomFeatures.add(selectedFeatures);
            randomLabels.add(matrix[sampled_idx][matrix.length-1]);
        }

        ArrayList<T> bootstrapData = new ArrayList<>();
        bootstrapData.add((T) randomFeatures);
        bootstrapData.add((T) randomLabels);

        return bootstrapData;
    }

//    private <T> void train(int matrix[][]) {
//        // Trains the model with given X features and Y labels in the datasets
//        ArrayList<T> bootstrap_sample = this.create_bootstrap_sampels(matrix);
//
//        ArrayList<T> base_learner_list = new ArrayList<T>();
//
//        for (int base_learner_idx = 0; base_leanrer_idx < this.n_base_learner; base_learner_idx++){
//            DecisionTree base_learner = DecisionTree()
//        }
//    }
}
