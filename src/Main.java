import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static int[][] aids_data_parser(String filename) {
        try {
            int[][] matrix = new int[50000][];
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
        int[] arrayRow = new int[23];
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



    public static void categorizeFeature(int[][] data, ArrayList<Integer> category) {

        int Q1 = data.length / 4;
        int Q2 = (int) ((double) data.length * 0.5);
        int Q3 = (int) ((double) data.length * 0.75);
        int Q4 = data.length;
        System.out.println(Q1);
        System.out.println(Q2);
        System.out.println(Q3);
        System.out.println(Q4);
        for (int feature_idx : category) {
            ArrayList<Integer> sorted_feature = new ArrayList<>();
            for (int row = 0; row < data.length; row++) {
                sorted_feature.add(data[row][feature_idx]);
            }

            Collections.sort(sorted_feature);
            ArrayList<Integer> Q1_Values = new ArrayList<Integer>(sorted_feature.subList(0, Q1));
            ArrayList<Integer> Q2_Values = new ArrayList<Integer>(sorted_feature.subList(Q1, Q2));
            ArrayList<Integer> Q3_Values = new ArrayList<Integer>(sorted_feature.subList(Q2, Q3));
            ArrayList<Integer> Q4_Values = new ArrayList<Integer>(sorted_feature.subList(Q3, Q4));

            for (int i = 0; i < data.length; i++) {
                if (Q1_Values.contains(data[i][feature_idx]))
                    data[i][feature_idx] = 1;
                else if (Q2_Values.contains(data[i][feature_idx]))
                    data[i][feature_idx] = 2;
                else if (Q3_Values.contains(data[i][feature_idx]))
                    data[i][feature_idx] = 3;
                else if (Q4_Values.contains(data[i][feature_idx]))
                    data[i][feature_idx] = 4;
            }
        }
    }

    public static void categorize_features(int [][] matrix) {
        categorizeFeature(matrix, new ArrayList<Integer>(Arrays.asList(0, 1 , 2, 3, 7, 10, 18, 19 , 20, 21)));
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

        System.out.println(Arrays.deepToString(matrix));
    }




    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath();
        String path_to_data = filePath.concat("/src/output.csv");
        System.out.println(path_to_data);

        int[][] matrix = aids_data_parser(path_to_data);

//        System.out.println(Arrays.deepToString(normalizeData(matrix)));


//        System.out.println(Arrays.deepToString(matrix));
//        categorize_features(matrix);

        int totalAttributes = matrix[0].length - 1;
        ArrayList<Integer> attributes =
                (IntStream.range(0, totalAttributes))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        ArrayList<Integer> allRows = getAllRows(matrix);
        System.out.println(allRows);
        System.out.println(attributes);

        DecisionTree tree = new DecisionTree(matrix);
        tree.printDecisionTree(matrix, attributes, allRows, 0, 100);
    }

}
