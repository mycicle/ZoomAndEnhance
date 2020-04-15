import sun.net.ftp.FtpClient;

import javax.imageio.*;
import java.io.*;
import java.awt.image.*;


public class PhotoMosaic {


    public static void main(String[] args) throws Exception {
        ImageProcessor ip = new ImageProcessor("./assets/star_wars.jpg");
        int[][] image = ip.getImageRGB();
        for(int i = 0; i < ip.getHeight(); i++){
            for (int j = 0; j < ip.getWidth(); j++){
                System.out.println(ip.decodeRGB(image[i][j]));
            }
        }
    }
}

class RGB{
    public int[] rgb;

    public RGB(int red, int green, int blue){
        this.rgb = new int[3];
        this.rgb[0] = red;
        this.rgb[1] = green;
        this.rgb[2] = blue;
    }

    public int[] getRGB(){
        return this.rgb;
    }
    public int getRed(){
        return this.rgb[0];
    }
    public int getBlue(){
        return this.rgb[1];
    }
    public int getGreen(){
        return this.rgb[2];
    }

    public String toString(){
        return "(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
    }

}
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


//    public int[][] getPixelValues(){
//        final byte[] pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
//        final int w = this.width;
//        final int h = this.height;
//        final boolean hasAlphaChannel = image.getAlphaRaster() != null;
//
//        int[][] result = new int[h][w];
//        if (hasAlphaChannel) {
//            final int pixelLength = 4;
//            for (int pixel = 0, row = 0, col = 0; pixel +3 < pixels.length; pixel += pixelLength){
//                int argb = 0;
//                argb += (((int) pixels[pixel] & 0xff) << 24); //alpha channel
//                argb += ((int) pixels[pixel + 1] & 0xff); //blue
//                argb += (((int) pixels[pixel + 2] & 0xff) << 8); //green
//                argb += (((int) pixels[pixel + 3] & 0xff) << 16); //red
//                result[row][col] = argb;
//                col ++;
//                if (col == width){
//                    col = 0;
//                    row ++;
//                }
//            }
//        }
//        else {
//            final int pixelLength = 3;
//            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
//                int argb = 0;
//                argb += -16777216; // 255 alpha
//                argb += ((int) pixels[pixel] & 0xff); // blue
//                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
//                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
//                result[row][col] = argb;
//                col++;
//                if (col == width) {
//                    col = 0;
//                    row++;
//                }
//            }
//        }
//        return result;
//    }

}
