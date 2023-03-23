package cosc202.andie;

import java.awt.image.*;

public class GaussianBlurFilter implements ImageOperation, java.io.Serializable{
    private int radius;

    GaussianBlurFilter(int radius){
        this.radius = radius;
    }

    GaussianBlurFilter(){
        this(1);
    }

    public BufferedImage apply(BufferedImage input){
        int size = (2*radius+1) * (2*radius+1);
        float[] array = new float[size];
        float sigma = ((float) radius) / 3; //Cast to float first, otherwise truncated to zero.
        float twoSigmaSq = 2 * sigma * sigma; //If sigma==0, then we are dividing by zero.
        float sum = 0;
        int index = 0;

        //compute the x and y of kernel
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                //apply the Gaussian formula to it
                float value = (float) Math.exp(-(x * x + y * y) / twoSigmaSq);
                value /= twoSigmaSq * Math.PI; //Remember the coefficient
                array[index] = value;
                sum += value;
                index++;
            }
        }
        //divide every value by the sum of previous matrix
        for (int i = 0; i < array.length; i++) {
            array[i] /= sum;
        }

        Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(input, output);

        return output;
    }
}
