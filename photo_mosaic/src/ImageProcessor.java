import com.sun.xml.internal.ws.resources.UtilMessages;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.security.InvalidParameterException;

class ImageProcessor{
    BufferedImage image;
    String path;
    int height, width;
    int[][] imageRGB;

    public ImageProcessor(String path) throws Exception{
        image = ImageIO.read(new File(path));
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.imageRGB = this._getImageRGB();
    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }

    public int[][] downscaleImage(int scale) throws Exception{
        if (((this.width % scale) != 0) || ((this.height % scale) != 0)){
            System.out.println("Need to have th image width and height evenly divisible by the scale");
            throw new InvalidParameterException();
        }

        int scaled_width = this.width / scale;
        int scaled_height = this.height / scale;


//        This thing will step through the image one sub image at a time
//        it will get the average color of that sub image and then set that as the color of all of the pixels in that region
//        It will also be able to return a 2d array of the new colorings

        int[][] output = new int[scale][scale];

        for (int i = 0, a=0; i < this.height; i += scaled_height, a++){
            for (int j = 0, b=0; j < this.width; j+= scaled_width, b++){

                int[][] sub_image = new int[scaled_height][scaled_width];
                for (int h=i, x=0; h < i+scaled_height; h++, x++){
                    for (int k=j, y=0; k< j+scaled_width; k++, y++){
                        sub_image[x][y] = this.imageRGB[h][k];
                    }
                }

                int average_color = this.getAverageColorRegion(sub_image, scaled_height, scaled_width);
                for (int h=i; h < i+scaled_height; h++){
                    for (int k=j; k< j+scaled_width; k++){
                        this.imageRGB[h][k] = average_color;
                    }
                }

                output[a][b] = average_color;
            }
        }
        return output;
    }


    public int[][]getImageRGB(){
        return this.imageRGB;
    }

    public int[][] _getImageRGB(){
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

        this.imageRGB = output;
        return output;
    }

    public RGB decodeRGB(int RGB){
        int red = (RGB & ((0xFF) << 16)) >> 16;
        int green = (RGB & ((0xFF) << 8)) >> 8;
        int blue = RGB & (0xFF);

        RGB rgb = new RGB(red,green,blue);
        return rgb;
    }
    public int encodeRGB(RGB rgb){
        int output = 0;
        output = output | (rgb.getRed() << 16);
        output = output | (rgb.getGreen() << 8);
        output = output | rgb.getBlue();
        return output;
    }

    public RGB getAverageColorRGB() {
        RGB rgb;
        double counter = 0;
        double sumRed = 0;
        double sumGreen = 0;
        double sumBlue = 0;

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                rgb = this.decodeRGB(this.imageRGB[i][j]);
                if (rgb.getAverageIntensity() < 50) {
                    continue;
                }
                sumRed += rgb.getRed();
                sumGreen += rgb.getGreen();
                sumBlue += rgb.getBlue();
                counter += 1;
            }
        }
        RGB average_color = new RGB((int) (sumRed / counter), (int) (sumGreen / counter), (int) (sumBlue / counter));
        return average_color;
    }

    public RGB getAverageColorRegionRGB(int[][] region, int height, int width){
        RGB rgb;
        double counter = 0;
        double sumRed = 0;
        double sumGreen = 0;
        double sumBlue = 0;

        for (int i = 0; i < height; i ++){
            for (int j = 0; j < width; j ++){
                rgb = this.decodeRGB(region[i][j]);
                sumRed += rgb.getRed();
                sumGreen += rgb.getGreen();
                sumBlue += rgb.getBlue();
                counter += 1;
            }
        }
        RGB average_color = new RGB((int) (sumRed / counter), (int) (sumGreen / counter), (int) (sumBlue / counter));
        return average_color;
    }

    public int getAverageColorRegion(int[][] image, int height, int width){
        RGB rgb = this.getAverageColorRegionRGB(image, width, height);
        int encoded_rgb = this.encodeRGB(rgb);
        return encoded_rgb;
    }
}