package cosc202.andie;

import java.awt.image.*;

public class SharpenFilter implements ImageOperation, java.io.Serializable {

    SharpenFilter(){
    }

    public BufferedImage apply(BufferedImage input) throws Exception {
        BufferedImage output = null;
        try{
            float[] array = {0, -1/2.0f, 0, -1/2.0f, 3, -1/2.0f, 0, -1/2.0f, 0};

            Kernel kernel = new Kernel(3, 3, array);
            ConvolveOp convOp = new ConvolveOp(kernel);
            output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            convOp.filter(input, output);
        }catch(Exception ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }
        return output;
    }
}
