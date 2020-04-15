import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

class ImageProcessor{
    BufferedImage image;
    String path;
    int height, width;

    public ImageProcessor(String path) throws Exception{
        image = ImageIO.read(new File(path));
        this.width = image.getWidth();
        this.height = image.getHeight();

    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }


    public int[][] getImageRGB(){
        final byte[] pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
        final int w = this.getWidth();
        final int h = this.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        //so we will need to do this. If it has no alpha channel then it will be 255 every time
        int[][] output = new int[w][h];
        //If this thing is a 3 byte BGR image
        if (image.getType() == 5){
            //the least significant byte is blue
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel+2 < pixels.length; pixel += pixelLength){
                int bgr = 0;
                bgr = bgr | ((int) pixels[pixel] & 0xFF);  //blue
                bgr = bgr | (((int) pixels[pixel+1] & 0xFF) << 8); //green
                bgr = bgr | (((int) pixels[pixel+2] & 0xFF) << 16); //red
                output[row][col] = bgr;
                col++;
                if (col == w){
                    col = 0;
                    row++;
                }
            }
        }

        return output;
    }

    public RGB decodeRGB(int RGB){
        int red = (RGB & ((0xFF) << 16)) >> 16;
        int green = (RGB & ((0xFF) << 8)) >> 8;
        int blue = RGB & (0xFF);

        RGB rgb = new RGB(red,green,blue);
        return rgb;
    }
}