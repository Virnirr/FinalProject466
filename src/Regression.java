import java.util.Arrays;

public abstract class Regression {
    protected double learningRate;
    protected int numIterations;
    protected double[] weights;
    protected double bias;

    public Regression(double learningRate, int numIterations) {
        this.learningRate = learningRate;
        this.numIterations = numIterations;
    }

    public abstract double[] fit(double[][] inputs, double[] expectedOutputs);

    // Make a prediction based on current weights and bias
    public double predict(double[] inputVector) {
        double prediction = 0.0;
        for (int i = 0; i < inputVector.length; i++) {
            prediction += (double) (inputVector[i]) * weights[i];
        }
        prediction += bias;
        return prediction;
    }

    // Make predictions for entire set of data
    public double[] predictAll(double[][] inputs) {
        double[] predictions = new double[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            predictions[i] = predict(inputs[i]);
        }
        return predictions;
    }

    // Calculate mean squared error between actual and predicted values
    public double mse(double[] actualOutputs, double[] predictedOutputs) {
        int n = actualOutputs.length;
        double sumSquaredError = 0.0;

        for (int i = 0; i < n; i++) {
            double error = (actualOutputs[i]) - predictedOutputs[i];
            sumSquaredError += error * error;
        }

        return sumSquaredError / n;
    }

    // calcualte binary cross entropy loss
    public double bce(double[] actualOutputs, double[] predictedOutputs){
        int n = actualOutputs.length;
        double sumError = 0.0;

        // basically just do a log loss for each thin
        for (int i = 0; i < n; i++){
            double y = (double) actualOutputs[i];
            double p = predictedOutputs[i];
            sumError += -1*((y*Math.log(p)) + (1-y)*Math.log(1-p));
        }

        return sumError;
    }


    @Override
    public String toString() {
        return  "(" +
                "learningRate = " + learningRate + ",\n" +
                "numIterations = " + numIterations + ",\n" +
                "weights = " + Arrays.toString(weights) + ",\n" +
                "bias = " + bias +
                ")";
    }
}