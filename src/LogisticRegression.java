import java.util.Arrays;

public class LogisticRegression {
    private double learningRate;
    private int numIterations;
    private double[] weights;
    private double bias;

    // Constructor to initialize learning rate and number of iterations
    public LogisticRegression(double learningRate, int numIterations) {
        this.learningRate = learningRate;
        this.numIterations = numIterations;
    }

    // Fit the model to the data
    public double[] fit(int[][] intInputs, int[] intExpectedOutputs) {
        // Convert integer inputs and expected outputs to double
        double[][] inputs = Arrays.stream(intInputs)
                .map(row -> Arrays.stream(row)
                        .asDoubleStream()
                        .toArray())
                .toArray(double[][]::new);
        double[] expectedOutputs = Arrays.stream(intExpectedOutputs).asDoubleStream().toArray();

        int numFeatures = inputs[0].length; // Number of features
        int numSamples = inputs.length; // Number of samples

        // Initialize weights and bias
        weights = new double[numFeatures];
        bias = 0.0;

        // Train for the specified number of iterations
        for (int i = 0; i < numIterations; i++) {
            // List of gradients for each feature
            double[] weightGradientSum = new double[numFeatures];
            // Sum of bias's gradient
            double biasGradientSum = 0.0;

            // Loop through each sample
            for (int j = 0; j < numSamples; j++) {
                double[] inputVector = inputs[j];
                double expectedOutput = expectedOutputs[j];
                double prediction = predict(inputVector);
                double error = prediction - expectedOutput;

                // Calculate gradients for weights and bias
                for (int k = 0; k < numFeatures; k++) {
                    weightGradientSum[k] += inputVector[k] * error;
                }
                biasGradientSum += error;
            }

            // Update weights and bias
            for (int k = 0; k < numFeatures; k++) {
                double avgWeightGradient = weightGradientSum[k] / numSamples;
                weights[k] -= learningRate * avgWeightGradient;
            }
            double avgBiasGradient = biasGradientSum / numSamples;
            bias -= learningRate * avgBiasGradient;
        }

        // Return weights and bias
        double[] result = new double[weights.length + 1];
        for (int i = 0; i < weights.length; i++) {
            result[i] = weights[i];
        }
        result[result.length - 1] = bias;

        return result;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
    // Make a prediction based on current weights and bias using the sigmoid function
    public double predict(double[] inputVector) {
        double predict = 0.0;
        for (int i = 0; i < inputVector.length; i++) {
            predict += inputVector[i] * weights[i];
        }
        predict += bias;
        return sigmoid(predict); // Sigmoid Function for classification

    }


    // Calculate mean squared error between actual and predicted values
    public double mse(double[] actualOutputs, double[] predictedOutputs) {
        int n = actualOutputs.length;
        double sumSquaredError = 0.0;

        for (int i = 0; i < n; i++) {
            double error = actualOutputs[i] - predictedOutputs[i];
            sumSquaredError += error * error;
        }

        return sumSquaredError / n;
    }

    @Override
    public String toString() {
        return "LogisticRegression(" +
                "learningRate = " + learningRate + ",\n" +
                "numIterations = " + numIterations + ",\n" +
                "weights = " + Arrays.toString(weights) + ",\n" +
                "bias = " + bias +
                ")";
    }


    public static class Main {
        public static void main(String[] args) {
            // Example data for prediction
            int[][] testData = {
                    {1073, 1, 37, 79, 0, 1, 0, 100, 0, 1, 18, 0, 1, 1, 2, 0, 1, 0, 322, 469, 882, 754},
                    {324, 0, 33, 73, 0, 1, 0, 90, 0, 1, 224, 0, 1, 1, 3, 1, 1, 1, 168, 575, 1035, 1525},
                    {495, 1, 43, 69, 0, 1, 0, 100, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 377, 333, 1147, 1088},
                    {1201, 3, 42, 89, 0, 1, 0, 100, 1, 1, 513, 0, 1, 1, 3, 0, 0, 0, 238, 324, 775, 1019},
                    {934, 0, 37, 137, 0, 1, 0, 100, 0, 0, 4, 0, 1, 0, 3, 0, 0, 1, 500, 443, 1601, 849}
            };
            int[] expectedOutputs = {1, 1, 1, 1, 0}; // Assuming last column represents the target variable (AIDS or not)

            // Create an instance of logistic regression with specified learning rate and number of iterations
            LogisticRegression logisticRegression = new LogisticRegression(0.01, 1000);

            // Train the logistic regression model
            double[] weightsAndBias = logisticRegression.fit(testData, expectedOutputs);
            System.out.println(Arrays.toString(weightsAndBias));
            }
        }
    }

