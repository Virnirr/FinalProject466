import javax.sound.sampled.Line;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int SIZE_OF_MATRIX = 2139;
    private static final int SIZE_OF_COLS = 23;
    private static final String RELATIVE_FILE_PATH = "/src/output.csv";
    public static int[][] aids_data_parser(String filename) {
        try {
            int[][] matrix = new int[SIZE_OF_MATRIX][];
            int rowIndex = 0;
            File file = new File(filename);
            Scanner scan_file = new Scanner(file);

            String[] tokenized_attributes;

            while (scan_file.hasNextLine()) {
                tokenized_attributes = scan_file.nextLine().split(",");

                ArrayList<Double> integerList = new ArrayList<>(
                        Arrays.stream(tokenized_attributes)
                                .mapToDouble(Double::parseDouble)
                                .boxed()
                                .collect(Collectors.toList())
                );


                matrix[rowIndex] = arrayListToArray(integerList);
                rowIndex++;
            }
            return matrix;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static int[] arrayListToArray(ArrayList<Double> row) {
        int[] arrayRow = new int[SIZE_OF_COLS];
        for (int i = 0; i < row.size(); i++) {
            arrayRow[i] = row.get(i).intValue();
        }
        return arrayRow;
    }

    public static ArrayList<Integer> getAllRows (int[][] matrix) {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            rows.add(i);
        }
        return rows;
    }

    public static double[][] normalizeData(int[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        double[][] normalizedData = new double[rows][cols];
        int[] min = new int[cols];
        int[] max = new int[cols];

        // Initialize min and max arrays
        for (int i = 0; i < cols; i++) {
            min[i] = Integer.MAX_VALUE;
            max[i] = Integer.MIN_VALUE;
        }
        // Find min and max for each column
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] < min[j]) {
                    min[j] = data[i][j];
                }
                if (data[i][j] > max[j]) {
                    max[j] = data[i][j];
                }
            }
        }
        // Normalize data using min-max normalization
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (max[j] == min[j]) {
                    normalizedData[i][j] = 0; // If max equals min, set to 0 to avoid division by zero
                } else {
                    normalizedData[i][j] = (double)(data[i][j] - min[j]) / (max[j] - min[j]);
                }
            }
        }

        return normalizedData;
    }

//    public static void categorizeFeature(int[][] data, ArrayList<Integer> category) {
//        for (int featureIdx : category) {
//            TreeSet<Integer> sortedFeature = new TreeSet<>();
//            for (int[] row : data) {
//                sortedFeature.add(row[featureIdx]);
//            }
//
//            int n = sortedFeature.size();
//            int[] boundaries = new int[10];
//            int step = (int) Math.ceil((double) n / 10);
//            for (int i = 0; i < 9; i++) {
//                boundaries[i] = (i + 1) * step;
//            }
//            boundaries[9] = n;
//
//            ArrayList<HashSet<Integer>> categoryValues = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                categoryValues.add(new HashSet<>());
//            }
//
//            int index = 0;
//            for (int value : sortedFeature) {
//                int categoryIndex = Arrays.binarySearch(boundaries, index);
//                if (categoryIndex < 0) {
//                    categoryIndex = -categoryIndex - 2;
//                }
//                categoryValues.get(categoryIndex).add(value);
//                index++;
//            }
//
//            for (int[] row : data) {
//                int value = row[featureIdx];
//                for (int i = 0; i < 10; i++) {
//                    if (categoryValues.get(i).contains(value)) {
//                        row[featureIdx] = i + 1;
//                        break;
//                    }
//                }
//            }
//        }
//    }




    public static void categorizeFeature(int[][] data, ArrayList<Integer> category) {

        for (int feature_idx : category) {

            TreeSet<Integer> sorted_feature = new TreeSet<>();
            for (int[] row : data) {
                sorted_feature.add(row[feature_idx]);
            }


            ArrayList<ArrayList<Integer>> vals = new ArrayList<>();
            int n = sorted_feature.size();
            int[] boundaries = new int[10];
            int step = (int) Math.ceil((double) n / 10);
            for (int i = 0; i < 9; i++) {
                boundaries[i] = (i + 1) * step;
            }
            boundaries[9] = n;


            ArrayList<HashSet<Integer>> categoryValues = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                categoryValues.add(new HashSet<>());
            }

            int index = 0;
            for (int value : sorted_feature) {
                int categoryIndex = Arrays.binarySearch(boundaries, index);
                if (categoryIndex < 0) {
                    categoryIndex = -categoryIndex - 2;
                    if (categoryIndex < 0) {
                        categoryIndex = 0;
                    }
                }
                categoryValues.get(categoryIndex).add(value);
                index++;
            }

            for (int[] row : data) {
                int value = row[feature_idx];
                boolean found = false;
                for (int i = 0; i < 10; i++) {
                    if (categoryValues.get(i).contains(value)) {
                        row[feature_idx] = i + 1;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    row[feature_idx] = 1;
                }
            }
        }
    }

    public static void remove_numeric_features(int[][] matrix, String fileName) {
        ArrayList<Integer> numeric_features = new ArrayList<Integer>(Arrays.asList(0, 2, 3, 10, 18, 19, 20, 21));

        // Create a StringBuilder to store the CSV data
        StringBuilder csvData = new StringBuilder();

        // Iterate over each row of the matrix
        for (int i = 0; i < matrix.length; i++) {
            // Create a StringBuilder to store the current row data
            StringBuilder rowData = new StringBuilder();

            // Iterate over each column of the matrix
            for (int j = 0; j < matrix[i].length; j++) {
                // Check if the current column is not a numeric feature
                if (!numeric_features.contains(j)) {
                    // Append the value to the row data
                    rowData.append(matrix[i][j]).append(",");
                }
            }

            // Remove the trailing comma from the row data
            rowData.setLength(rowData.length() - 1);

            // Append the row data to the CSV data
            csvData.append(rowData.toString()).append("\n");
        }

        // Write the CSV data to a file
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(csvData.toString());
            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static void categorize_features(int [][] matrix) {
        categorizeFeature(matrix, new ArrayList<Integer>(Arrays.asList(0, 2, 3, 10, 18, 19 , 20, 21)));
        String csvFile = "output.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            for (int[] row : matrix) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    line.append(row[i]);
                    if (i < row.length - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.write("\n");
            }

            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the CSV file.");
            e.printStackTrace();
        }

//        System.out.println(Arrays.deepToString(matrix));
    }

    public static int predictLabel(TreeNode tree, ArrayList<Integer> featureValues) {
        // return either 0 or 1 for label
        TreeNode currNode = tree;
        while (currNode.getPaths() != null) {
            // find the current path and go through the path iteratively
            for (TreeNode node : currNode.getPaths()) {
                if (node.is_leaf()) {
                    currNode = node;
                    break;
                }
                if (featureValues.get(node.getFeatureIdx()) ==  node.getFeatureVal()) {
                    currNode = node;
                    break;
                }
            }
        }
        return currNode.getLabel();
    }

    public static int[] getLastColumn(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] lastColumn = new int[rows];

        for (int i = 0; i < rows; i++) {
            lastColumn[i] = matrix[i][cols - 1]; // Assign the last element of each row to the new array
        }

        return lastColumn;
    }

    public static int[][] removeLastColumn(int[][] array) {
        int rows = array.length;
        int cols = array[0].length - 1;
        int[][] newArray = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        return newArray;
    }

    public static Regression doRegression(int[][] matrix, boolean linearLogFlag, boolean isTraining, double learningRate, int numIterations, Regression oldRegression){
        double[] outputs;
        int[][] inputs;
        int[] labels;
        Regression regression;

        // use old regression (so i can re-run with test set and old weights)
        if(!isTraining){
            regression = oldRegression;
        // use linear or logistic
        } else if (linearLogFlag){
            regression = new LinearRegression(learningRate, numIterations);
        } else {
            regression = new LogisticRegression(learningRate, numIterations);
        }

        // get inputs and labels
        inputs = removeLastColumn(matrix);
        labels = getLastColumn(matrix);

        // train (don't do this for testing set)
        if (isTraining) {
            regression.fit(inputs, labels);
        }

        // print mse and weights and stuff
        outputs = regression.predictAll(inputs);
        System.out.println(regression.mse(labels, outputs));
        System.out.println(regression);

        return regression;
    }


    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath();
        String path_to_data = filePath.concat(RELATIVE_FILE_PATH);
        System.out.println(path_to_data);
        int[][] matrix = aids_data_parser(path_to_data);
        int[][] matrixTrain;
        int[][] matrixTest;
        Regression regressionObj;

        List<int[]> listMatrix = new ArrayList<>(Arrays.asList(matrix));
        Collections.shuffle(listMatrix, new Random());
        int splitIndex = (int) (listMatrix.size() * 0.8);

        matrixTrain = listMatrix.subList(0, splitIndex).toArray(new int[0][]);
        matrixTest = listMatrix.subList(splitIndex, listMatrix.size()).toArray(new int[0][]);




        // linear, train
        System.out.println("\nlinear, train");
        regressionObj = doRegression(matrixTrain, true, true, 0.0001, 10000, null);
        // linear, test
        System.out.println("\nlinear, test");
        doRegression(matrixTest, true, false, 0.0001, 10000, regressionObj);
        // logistic, train
        System.out.println("\nLogistic, train");
        regressionObj = doRegression(matrixTrain, false, true, 0.001, 2000, null);
        // logistic, test
        System.out.println("\nLogistic, test");
        doRegression(matrixTest, false, false, 0.001, 2000, regressionObj);

//        categorize_features(matrix);
//        System.out.println(Arrays.deepToString(normalizeData(matrix)));


//        System.out.println(Arrays.deepToString(matrix));
//        categorize_features(matrix);

//        remove_numeric_features(matrix, "removed_numeric.csv");

//        int totalAttributes = matrix[0].length - 1;
//        ArrayList<Integer> attributes =
//                (IntStream.range(0, totalAttributes))
//                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
//        ArrayList<Integer> allRows = getAllRows(matrix);
//        System.out.println(allRows);
//        System.out.println(attributes);
//
//        DecisionTree tree = new DecisionTree(matrix);
//        TreeNode decisionTree = new TreeNode(-1, -1, new ArrayList<TreeNode>(), -1);
//        tree.printDecisionTree(matrix, attributes, allRows, 0, 100, decisionTree);
        ArrayList<Integer> features_to_predict = new ArrayList<Integer>(
                Arrays.asList(4,1,2,6,0,0,0,100,0,0,1,0,0,0,1,0,1,1,5,7,1,1)
        );
//
//        System.out.println("DONE WITH TRAINING");
//        System.out.println(predictLabel(decisionTree, features_to_predict));

        RandomForest forest = new RandomForest(20, 500);
        forest.train(matrix);

        System.out.println(forest.prediction_list(features_to_predict));
    }
}
