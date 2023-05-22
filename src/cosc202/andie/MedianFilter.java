package cosc202.andie;

import java.awt.Point;
import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median filter.
 * </p>
 * 
 * <p>
 * A Median filter blurs an image by taking the median of a radius of pixels.
 * This is implemented by accessing each and every pixel needed to get the 
 * median value.
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of the filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * The coordinates of the corners of the selected area. If there is no selected area, these will be equal to -1.
     * </p>
     */
    private int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

    /**
     * <p>
     * Construct a Median filter with a given radius
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the pixels wanted to sort.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger median effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MedianFilter
     * @param p1 The top left corner of the selection to apply the filter over
     * @param p2 The bottom right corner of the selection to apply the filter over
     */
    MedianFilter(int radius, Point p1, Point p2) {
        this.radius = radius;
        this.x1 = (int) p1.getX();
        this.y1 = (int) p1.getY();
        this.x2 = (int) p2.getX();
        this.y2 = (int) p2.getY();
    }

    /**
     * <p>
     * Construct a Median filter with a given radius
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the pixels wanted to sort.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger median effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    MedianFilter(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Median filter with the default size.
     * </p>
     * 
     * <p>
     * By default, a Median filter has radius 1.
     * </p>
     * 
     * @see MedianFilter(int)
     */
    MedianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Median filter to an image.
     * </p>
     * 
     * <p>
     * The Median filter is implmented by finding the separate 
     * RBG and alpha channels and sorting these values inside
     * a given radius. 
     * A larger radius gives us a stronger blurring as pixels
     * are all set to the median value of the channels.
     * </p>
     * 
     * @param input The image to apply the Median filter to.
     * @return The resulting image.
     */

    public BufferedImage apply(BufferedImage input) throws Exception {

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        try{
            int sideLength = 2*radius+1;
            int size = sideLength*sideLength;

            //The dimensions of the selection - default to the entire image
            Point p1 = new Point(0, 0);
            Point p2 = new Point(input.getWidth(), input.getHeight()-1);
            //Otherwise, use the selection provided
            if(x1 != -1){
                p1.setLocation(x1, y1);
                p2.setLocation(x2, y2);
            }
            
            //Create new threads
            int cores = Runtime.getRuntime().availableProcessors();
            Thread[] threads = new Thread[cores];
            int heightSegment = ((int) (p2.getY() - p1.getY())) / threads.length;
            for(int i = 0; i < threads.length; i++){
                //Make sure that the filter extends all the way down
                if(i == threads.length-1) threads[i] = new RunProcess(input, output, size, (int) p1.getX(), (int) p1.getY() + heightSegment * i, (int) p2.getX(), (int) p2.getY());
                else threads[i] = new RunProcess(input, output, size, (int) p1.getX(), (int) p1.getY() + heightSegment * i, (int) p2.getX(), (int) p1.getY() + heightSegment * (i+1));
                threads[i].start();
            }

            //Wait for each thread to finish.
            for(int i = 0; i < threads.length; i++) threads[i].join();
        }catch(NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        return output;
    }

    /**
     * <p>
     * An extension of {@code Thread} which runs a portion of the computation for Median Filter.
     * </p>
     */
    private class RunProcess extends Thread{
        /** The input image - we take the original pixel values from here. */
        private BufferedImage input;
        /** The output image - mutated pixels are put onto here. */
        private BufferedImage output;
        /** The points defining the selection over which to apply Median Filter */
        private int x1, y1, x2, y2;
        /** The size of the filter (2*radius+1)^2 */
        private int size;

        /**
         * Instantiate a thread for a segment of the input image.
         * 
         * @param input      The image to mutate
         * @param filterSize The size of the filter (i.e. (2*radius+1)^2)
         * @param x1         The x-oordinate of the top left corner of selection
         * @param y1         The y-oordinate of the top left corner of selection
         * @param x2         The x-oordinate of the bottom right corner of selection
         * @param y2         The y-oordinate of the bottom right corner of selection
         */
        public RunProcess(BufferedImage input, BufferedImage output, int filterSize, int x1, int y1, int x2, int y2){
            super();
            this.input = input;
            this.output = output;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.size = filterSize;
        }

        /**
         * <p>
         * Make the thread start processing the image.
         * </p>
         */
        @Override
        public void run(){
            int[] a = new int[size];
            int[] r = new int[size];
            int[] g = new int[size];
            int[] b = new int[size];
            int median = size/2;
            boolean hasAlpha = input.getColorModel().hasAlpha();
            //Go to every pixel in the selection
            for (int x = this.x1; x < this.x2; x++) {
                for (int y = this.y1; y < this.y2; y++) {
                    //Go to every pixel within the radius of the current pixel
                    int index = 0;
                    for (int i = x - radius; i <= x + radius; i++) {
                        for (int j = y - radius; j <= y + radius; j++) {
                            //Make sure this is a valid point (i.e. it is within the image bounds)
                            int validX = i, validY = j;
                            if(i < 0) validX = 0;
                            else if(i >= input.getWidth()) validX = input.getWidth()-1;
                            if(j < 0) validY = 0;
                            else if(j >= input.getHeight()) validY = input.getHeight()-1;
                            //Put this pixel into the arrays
                            int pixel = input.getRGB(validX, validY);
                            if(hasAlpha)  a[index] = (pixel & 0xFF000000) >> 24;
                            else a[index] = 255;
                            r[index] = (pixel & 0x00FF0000) >> 16;
                            g[index] = (pixel & 0x0000FF00) >> 8;
                            b[index] = (pixel & 0x000000FF);
                            index++;
                        
                        }
                    }
                    //Sort all the pixels that were in the radius around this pixel and choose the median value
                    Arrays.sort(a); Arrays.sort(r); Arrays.sort(g); Arrays.sort(b);
                    int newColour = (a[median] << 24) | (r[median] << 16) | (g[median] << 8) | (b[median] << 0);
                    output.setRGB(x, y, newColour);
                }
            }
            // This thread has computed everything it needs to; join main thread.
            this.interrupt();
        }
    }

}