import java.util.Arrays;

public class LinearRegression extends Regression {

    public LinearRegression(double learningRate, int numIterations) {
        super(learningRate, numIterations);
    }

    // Fit the model to the data
    public double[] fit(double[][] inputs, double[] expectedOutputs) {
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

    @Override
    public String toString() {
        return "LinearRegression" + super.toString();
    }
}
