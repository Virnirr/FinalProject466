import javax.sound.sampled.Line;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int SIZE_OF_MATRIX = 2139;
    private static final int SIZE_OF_COLS = 23;
    private static final String RELATIVE_FILE_PATH = "/src/output.csv";
    private static final String SOURCE_FILE_PATH = "/src/AIDS_Classification.csv";

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
        int cols = data[0].length-1;
        double[][] normalizedData = new double[rows][cols+1];
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

        // copy over the labels
        for (int i = 0; i<rows; i++){
            normalizedData[i][cols] = (double) data[i][cols];
        }

        return normalizedData;
    }

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

    public static void categorize_to_file(int [][] matrix) {
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

    public static double[] getLastColumn(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] lastColumn = new double[rows];

        for (int i = 0; i < rows; i++) {
            lastColumn[i] = matrix[i][cols - 1]; // Assign the last element of each row to the new array
        }

        return lastColumn;
    }

    public static double[][] removeLastColumn(double[][] array) {
        int rows = array.length;
        int cols = array[0].length - 1;
        double[][] newArray = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        return newArray;
    }

    public static Regression doRegression(double[][] matrix,
                                          boolean linearLogFlag,
                                          boolean isTraining,
                                          double learningRate,
                                          int numIterations,
                                          Regression oldRegression){
        double[] outputs;
        double[][] inputs;
        double[] labels;
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
        System.out.print("MSE: ");
        System.out.println(regression.mse(labels, outputs));
        System.out.print("BCE: ");
        System.out.println(regression.bce(labels, outputs));
//        System.out.println(regression); // prints weights and bias

        return regression;
    }

    public static int[][] computeConfusionMatrix(int[][] data,
                                                 int[][] testingSet,
                                                 RandomForest forest) {

        int [][] confusionMatrix = new int[2][2];
        int TP = 0;
        int FN = 0;
        int FP = 0;
        int TN = 0;

        for (int i = 0; i < testingSet.length; i++) {
            int[] primitiveArray = Arrays.copyOfRange(data[i], 0, data[0].length - 1);
            Integer[] objectArray = Arrays.stream(primitiveArray).boxed().toArray(Integer[]::new);
            ArrayList<Integer> features_to_predict = new ArrayList<>(Arrays.asList(objectArray));

            int predictedCategory = forest.prediction_list(features_to_predict);
            int actualCategory = data[i][data[0].length - 1];

            if (actualCategory == 1 && predictedCategory == 1) {
                TP++;
            }
            else if (actualCategory == 0 && predictedCategory == 1) {
                FP++;
            }
            else if (actualCategory == 1 && predictedCategory == 0) {
                FN++;
            }
            else if (actualCategory == 0 && predictedCategory == 0) {
                TN++;
            }
        }

        confusionMatrix[0][0] = TP;
        confusionMatrix[0][1] = FN;
        confusionMatrix[1][0] = FP;
        confusionMatrix[1][1] = TN;

        System.out.println("True Positive %d".formatted(TP));
        System.out.println("False Positive %d".formatted(FP));
        System.out.println("False Negative %d".formatted(FN));
        System.out.println("True Negative %d".formatted(TN));

        return confusionMatrix;
    }

    public static double compute_precision(int[][] confusionMatrix) {
        int TP = confusionMatrix[0][0];
        int FP = confusionMatrix[1][0];
        int FN = confusionMatrix[0][1];
        int TN = confusionMatrix[1][1];

        return (double) TP / (TP + FP);
    }
    public static double compute_recall(int[][] confusionMatrix) {
        int TP = confusionMatrix[0][0];
        int FP = confusionMatrix[1][0];
        int FN = confusionMatrix[0][1];
        int TN = confusionMatrix[1][1];

        return (double) TP / (TP + FN);
    }
    public static double compute_f1_score(int[][] data, int[][] testingData, RandomForest forest) {
        int[][] confusionMatrix = computeConfusionMatrix(data, testingData, forest);

        double precision = compute_precision(confusionMatrix);
        double recall = compute_recall(confusionMatrix);

        System.out.println("Precision: %f".formatted(precision));
        System.out.println("Recall: %f".formatted(recall));
        System.out.println("F1 Score: %f".formatted((2 * precision * recall) / (precision + recall)));

        return 2 * precision * recall / (precision + recall);
    }

    public static void writeDoubleArrayToCSV(String fileName, double[][] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    writer.write(String.valueOf(data[i][j]));
                    if (j < data[i].length - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath();
        String path_to_data = filePath.concat(RELATIVE_FILE_PATH);
        String path_to_src_data = filePath.concat(SOURCE_FILE_PATH);
        System.out.println(path_to_data);
        int[][] matrix = aids_data_parser(path_to_data);
        int[][] matrixTrain;
        int[][] matrixTest;
        int[][] sourceMatrix = aids_data_parser(path_to_src_data);
        double[][] normalizedMatrix;
        double[][] normalizedMatrixTrain;
        double[][]normalizedMatrixTest;
        int[][] matrixValidation;
        Regression regressionObj;

        normalizedMatrix = normalizeData(sourceMatrix);
//        writeDoubleArrayToCSV("./src/normalized.csv", normalizedMatrix);

        // split into train and test lists for the binned dataset
        List<int[]> listMatrix = new ArrayList<>(Arrays.asList(matrix));
        Collections.shuffle(listMatrix, new Random());

        int splitTraining = (int) (listMatrix.size() * 0.9);

        matrixTrain = listMatrix.subList(0, splitTraining).toArray(new int[0][]);
        matrixTest = listMatrix.subList(splitTraining, listMatrix.size()).toArray(new int[0][]);

        // split into train and test lists for the normalized dataset
        List<double[]> normalizedListMatrix = new ArrayList<>(Arrays.asList(normalizedMatrix));
        Collections.shuffle(normalizedListMatrix, new Random());

        normalizedMatrixTrain = normalizedListMatrix.subList(0, splitTraining).toArray(new double[0][]);
        normalizedMatrixTest = normalizedListMatrix.subList(splitTraining, normalizedListMatrix.size()).toArray(new double[0][]);


        // linear, train
        System.out.println("\nlinear, train");
        regressionObj = doRegression(normalizedMatrixTrain, true, true, 0.2, 2000, null);
        // linear, test
        System.out.println("\nlinear, test");
        doRegression(normalizedMatrixTest, true, false, 0, 0, regressionObj);
        // logistic, train
        System.out.println("\nLogistic, train");
        regressionObj = doRegression(normalizedMatrixTrain, false, true, 0.01, 20000, null);
        // logistic, test
        System.out.println("\nLogistic, test");
        doRegression(normalizedMatrixTest, false, false, 0, 0, regressionObj);

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

//        // hyper parameter tuning for random forest
        double maxF1Score = 0.0;
        int best_n_base_learner = 30;
        int best_bootstrap_size = 600;
        RandomForest forest =  new RandomForest(best_n_base_learner, best_bootstrap_size);
        forest.train(matrixTrain);

//        for (int cur_base_learner = best_n_base_learner; cur_base_learner < 100; cur_base_learner+=10) {
//            for (int curr_boot_strap_size = best_bootstrap_size; curr_boot_strap_size < 2000; curr_boot_strap_size+=250) {
//                forest = new RandomForest(cur_base_learner, curr_boot_strap_size);
//                forest.train(matrixTrain);
//                double f1_score = compute_f1_score(matrix, matrixValidation, forest);
//                if (f1_score > maxF1Score) {
//                    maxF1Score = f1_score;
//                    best_n_base_learner = cur_base_learner;
//                    best_bootstrap_size = curr_boot_strap_size;
//                }
//            }
//        }
        System.out.println();
        System.out.println("Random Forest Testing Results:");
        compute_f1_score(matrix, matrixTest, forest);
    }
}
