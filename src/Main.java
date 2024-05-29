import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static int[][] process(String filename) {
        try {
            int[][] matrix = new int[150][];
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
        int[] arrayRow = new int[5];
        for (int i = 0; i < row.size(); i++) {
            arrayRow[i] = row.get(i).intValue();
        }
        return arrayRow;
    }

    public static void printDecisionTree(int[][] data,
                                         ArrayList<Integer> attributes,
                                         ArrayList<Integer> rows,
                                         int level,
                                         double currentIGR) {
        // recursively prints the decision tree.

        DecisionTree decisionTree = new DecisionTree(data);
        // base case return if either attribute or rows is empty
        if (attributes.size() == 0 || rows.size() == 0 || currentIGR <= 0.01) {
            int mostCommonLabel = decisionTree.findMostCommonValue(rows);
            System.out.println("\t".repeat(level) + "value = " + mostCommonLabel);
            return;
        }

        HashMap<Integer, Double> attributeIGR = new HashMap<>();

        for (Integer attr : attributes) {
            attributeIGR.put(attr, decisionTree.computeIGR(attr, rows));
        }

        // get the attribute with highest IGR from hashmap
        int attributeWithHighestIGR = -1;
        double highestIGR = -1;
        for (Map.Entry<Integer, Double> entry : attributeIGR.entrySet()) {
            int attr = entry.getKey();
            double igr = entry.getValue();
            if (igr > highestIGR) {
                attributeWithHighestIGR = attr;
                highestIGR = igr;
            }
        }
        if (highestIGR <= 0.02) {
            int mostCommonLabel = decisionTree.findMostCommonValue(rows);
            System.out.println("\t".repeat(level) + "value = " + mostCommonLabel);
            return;
        }

        // contains the attribute values --> list of rows with attribute value
        HashMap<Integer, ArrayList<Integer>> splitAttribute =
                decisionTree.split(attributeWithHighestIGR, rows);

        // remove attribute from attributes arraylist
        attributes.remove(Integer.valueOf(attributeWithHighestIGR));


        for (Map.Entry<Integer, ArrayList<Integer>> entry : splitAttribute.entrySet()) {
            int valInAttribute = entry.getKey();

            ArrayList<Integer> rowsAssocWithAttrVal = entry.getValue();
            // get the IGR of current value to recurse on; note: base case will result in printing value if <= 0.01
            double currValIGR = decisionTree.computeIGR(attributeWithHighestIGR, rows);
            System.out.println("\t".repeat(level) + "When attribute " +
                    (attributeWithHighestIGR + 1) +
                    " has value " + valInAttribute);

            // recursive call
            printDecisionTree(data, attributes, rowsAssocWithAttrVal, level+1, currValIGR);
        }
    }

    public static ArrayList<Integer> getAllRows (int[][] matrix) {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            rows.add(i);
        }
        return rows;
    }
    public static void main(String[] args) {
        int[][] matrix = process("/Users/zhihe/CSC466/labs/src/DecisionTree/data.txt");

        int totalAttributes = matrix[0].length - 1;
        ArrayList<Integer> attributes =
                (IntStream.range(0, totalAttributes))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        ArrayList<Integer> allRows = getAllRows(matrix);

        printDecisionTree(matrix, attributes, allRows, 0, 100);
    }
}
