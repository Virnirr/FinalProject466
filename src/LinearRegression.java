import java.util.Arrays;

public class LinearRegression {
    private double learningRate;
    private int numIterations;
    private double[] weights;
    private double bias;

    public LinearRegression(double learningRate, int numIterations) {
        this.learningRate = learningRate;
        this.numIterations = numIterations;
    }

    // Fit the model to the data
    public double[] fit(int[][] intInputs, int[] intExpectedOutputs) {
        // convert parameter lists to doubles
        double[][] inputs = Arrays.stream(intInputs)
                                    .map(row -> Arrays.stream(row)
                                            .asDoubleStream()
                                            .toArray())
                                    .toArray(double[][]::new);
        double[] expectedOutputs = Arrays.stream(intExpectedOutputs).asDoubleStream().toArray();


        int numFeatures = inputs[0].length;
        int numSamples = inputs.length;

        // Initialize weights and bias
        weights = new double[numFeatures];
        bias = 0.0;

        // train for numIterations
        for (int i = 0; i < numIterations; i++) {

            // list of gradients for each feature
            double[] weightGradientSum = new double[numFeatures];
            // sum of bias's gradient
            double biasGradientSum = 0.0;

            // go to each sample
            for (int j = 0; j < numSamples; j++) {
                double[] inputVector = inputs[j];
                double expectedOutput = expectedOutputs[j];
                double prediction = predict(inputVector);
                double error = prediction - expectedOutput;

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

    // Make a prediction based on current weights and bias
    public double predict(double[] inputVector) {
        double prediction = 0.0;
        for (int i = 0; i < inputVector.length; i++) {
            prediction += inputVector[i] * weights[i];
        }
        prediction += bias;
        return prediction;
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
        return "LinearRegression(" +
                "learningRate = " + learningRate + ",\n" +
                "numIterations = " + numIterations + ",\n" +
                "weights = " + Arrays.toString(weights) + ",\n" +
                "bias = " + bias +
                ")";
    }
}
