import java.io.File;
import java.io.FileNotFoundException;
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
    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath();
        String path_to_data = filePath.concat("/src/AIDS_Classification_50000.csv");

        System.out.println(path_to_data);

        int[][] matrix = aids_data_parser(path_to_data);
//        System.out.println(Arrays.deepToString(matrix));

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
