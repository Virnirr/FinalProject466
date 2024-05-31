import java.util.*;
import java.util.stream.*;

public class LinearRegression {
    private double learningRate;
    private int numIterations;
    private List<Double> weights;
    private double bias;

    public LinearRegression(double learningRate, int numIterations) {
        this.learningRate = learningRate;
        this.numIterations = numIterations;
        this.weights = new ArrayList<>();
        this.bias = 0.0;
    }

    // Fit the model to the data
    public List<Double> fit(List<List<Double>> inputs, List<Double> expectedOutputs) {
        int numFeatures = inputs.get(0).size();
        int numSamples = inputs.size();

        // Initialize weights and bias
        weights = new ArrayList<>();
        for (int i = 0; i < numFeatures; i++) {
            weights.add(0.0);
        };
        bias = 0.0;

        // train for numIterations
        for (int i = 0; i < numIterations; i++) {

            // list of gradients for each feature
            List<Double> weightGradientSum = new ArrayList<>(numFeatures);
            for (int j = 0; j < numFeatures; j++) {
                weightGradientSum.add(0.0);
            }
            // sum of bias's gradient
            double biasGradientSum = 0.0;

            // go to each sample
            for (int j = 0; j < numSamples; j++) {
                List<Double> inputVector = inputs.get(j);
                double expectedOutput = expectedOutputs.get(j);
                double prediction = predict(inputVector);
                double error = prediction - expectedOutput;

                for (int k = 0; k < numFeatures; k++) {
                    weightGradientSum.set(k, weightGradientSum.get(k) + inputVector.get(k) * error);
                }
                biasGradientSum += error;
            }

            // Update weights and bias
            for (int k = 0; k < numFeatures; k++) {
                double avgWeightGradient = weightGradientSum.get(k) / numSamples;
                weights.set(k, weights.get(k) - learningRate * avgWeightGradient);
            }
            double avgBiasGradient = biasGradientSum / numSamples;
            bias -= learningRate * avgBiasGradient;
        }

        // Return weights and bias
        List<Double> result = new ArrayList<>(weights);
        result.add(bias);
        return result;
    }

    // Make a prediction based on current weights and bias
    public double predict(List<Double> inputVector) {
        double prediction = 0.0;
        for (int i = 0; i < inputVector.size(); i++) {
            prediction += inputVector.get(i) * weights.get(i);
        }
        prediction += bias;
        return prediction;
    }
}
