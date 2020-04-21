import com.sun.xml.internal.ws.resources.UtilMessages;
import org.jetbrains.annotations.NotNull;

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
    int scaled_height, scaled_width;
    int scale;
    int average_color;
    RGB average_color_RGB;
    int[][] downscaled_image;

    public ImageProcessor(String path) throws Exception{
        image = ImageIO.read(new File(path));
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.imageRGB = this._getImageRGB();
        this.scaled_height = 0;
        this.scaled_width = 0;
        this.average_color = this.encodeRGB(this.getAverageColorRGB());
        this.average_color_RGB = this.decodeRGB(this.average_color);
    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public int getScaledHeight() {return this.scaled_height;}
    public int getScaledWidth() {return this.scaled_width;}
    //IMPORTANT NOTE ABOUT DOWNSCALE IMAGE
    //it will set the imageRGB object within the image processor object equal to a more pixelated version of itself
    //it will return a color array equivalent to the color of each of those pixles in the pixelated region
    //scaled height and width will be the height and width of the smaller pixelated regions
    //scale witll be the number of pixelated regionson each side
    //output is of size scale x scale
    public int[][] downscaleImage(int scale) throws Exception{
        if (((this.width % scale) != 0) || ((this.height % scale) != 0)){
            System.out.println("Need to have th image width and height evenly divisible by the scale");
            throw new InvalidParameterException();
        }

        int scaled_width = this.width / scale;
        int scaled_height = this.height / scale;
        this.scaled_width = width/scaled_width;
        this.scaled_height = height/scaled_height;

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
        this.downscaled_image = output;
        return output;
    }

    public Image getImage() { return new Image(this.average_color_RGB, this.downscaled_image); }
    public int[][]getImageRGB(){
        return this.imageRGB;
    }

    public int[][] _getImageRGB(){
        final byte[] pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
        final int w = this.getWidth();
        final int h = this.getHeight();
        System.out.println(w + " " + h);
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        //so we will need to do this. If it has no alpha channel then it will be 255 every time
        int[][] output = new int[h][w];
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
    public int encodeRGB(@NotNull RGB rgb){
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

    public void insertImage(int[][] image, int x, int y){
        for (int i = y, h = 0; i < y + scaled_height; i++, h++){
            for (int j = x, k = 0; j < x + scaled_width; j++, k++){
                this.imageRGB[i][j] = image[h][k];
            }
        }

    }

    public void shrink_image_to(int width, int height){
        if (this.width > width){
            int center = (int) this.width / 2;
            int left_bound = (int) (center - width/2);
            int right_bound = (int) (center + width/2);
            int[][] new_imageRGB = new int[this.height][right_bound-left_bound];
            for (int i = 0; i < this.height; i++) {
                for (int j = left_bound, k=0; j < right_bound; j++, k++) {
                    new_imageRGB[i][k] = this.imageRGB[i][j];
                }
            }
            this.imageRGB = new_imageRGB;
            this.width = right_bound-left_bound;
        }

        if (this.height > height){
            int center = (int) this.height/2;
            int lower_bound = (int)(center-height/2);
            int upper_bound = (int)(center+height/2);
            int[][] new_imageRGB = new int[upper_bound-lower_bound][this.width];
            for(int i = lower_bound, h=0; i < upper_bound; i++, h++){
                for (int j = 0; j < this.width; j++){
                    new_imageRGB[h][j] = this.imageRGB[i][j];
                }
            }
            this.imageRGB = new_imageRGB;
            this.height = upper_bound-lower_bound;
        }
    }
}