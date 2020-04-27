package photo_mosaic;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;

public class ImageProcessor {
    BufferedImage image;
    int[][] intensity_image;
    int width, height;
    BufferedImage grayscale_image;
    int average_intensity;

    public ImageProcessor(String path) throws Exception{
        image = ImageIO.read(new File(path));
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.intensity_image = this.getImageIntensity();
        this.grayscale_image = this.getGrayscale();
        this.average_intensity = this.getAverageIntensity();
    }

    public int getAverageIntensity(){
        double average_intensity = 0;
        double size = this.width * this.height;
        for (int i = 0; i < this.width; i++){
            for (int j = 0; j < this.height; j++){
                average_intensity += intensity_image[i][j];
            }
        }
        return (int)(average_intensity / size);
    }

    public int[][] getImageIntensity() {
        final byte[] pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
        int[][] output = new int[this.width][this.height];
        final int W = this.width;
        final int H = this.height;

        if (image.getType() == 5) {
            //the least significant byte is blue
            final int pixelLength = 3;
            for (int pixel = 0, i = 0, j = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int intensity = 0;
                intensity = (int) (((pixels[pixel] & 0xFF) * 0.299) + (((pixels[pixel + 1] & 0xFF)) * 0.587) + (((pixels[pixel + 2] & 0xFF)) * 0.114));
//                bgr = bgr | ((int) pixels[pixel] & 0xFF);  //blue
//                bgr = bgr | (((int) pixels[pixel+1] & 0xFF) << 8); //green
//                bgr = bgr | (((int) pixels[pixel+2] & 0xFF) << 16); //red
                output[i][j] = intensity;
                i++;
                if (i == W) {
                    i = 0;
                    j++;
                }
            }
        }

        return output;
    }

    public int[][] getImageIntensity(BufferedImage im) {
        final byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();
        int[][] output = new int[im.getWidth()][im.getHeight()];
        final int W = im.getWidth();
        final int H = im.getHeight();


        if (im.getType() == 5) {
            //the least significant byte is blue
            final int pixelLength = 3;
            for (int pixel = 0, i = 0, j = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int intensity = 0;
                intensity = (int) (((pixels[pixel] & 0xFF) * 0.299) + (((pixels[pixel + 1] & 0xFF)) * 0.587) + (((pixels[pixel + 2] & 0xFF)) * 0.114));
//                bgr = bgr | ((int) pixels[pixel] & 0xFF);  //blue
//                bgr = bgr | (((int) pixels[pixel+1] & 0xFF) << 8); //green
//                bgr = bgr | (((int) pixels[pixel+2] & 0xFF) << 16); //red
                output[i][j] = intensity;
                i++;
                if (i == W) {
                    i = 0;
                    j++;
                }
            }
        }

        return output;
    }

    public void insertImage(int[][] input, int w, int h, int i, int j){
        for (int a = i, x = 0; a < i+w; a++, x++){
            for (int b = j, y = 0; b < j+h; b++, y++){
                this.intensity_image[a][b] = input[x][y];
            }
        }
    }

    public int[][] resizeImage(int w, int h){
        double scale_width_percent = w / this.width;
        double scale_height_percent = h / this.height;
        if(this.width % w != 0 | this.height % h != 0){
            return null;
        }
        int step_w = this.width / w;
        int step_h = this.height / h;
        int[][] output = new int[w][h];

        for (int i = 0, x=0; i < this.width; i += step_w, x++){
            for (int j = 0, y=0; j < this.height; j += step_h, y++){
                output[x][y] = this.averageIntensity(i, j, step_w, step_h);
            }
        }
        this.width = w;
        this.height = h;
        this.intensity_image = output;
        return output;
    }

    public int averageIntensity(int i, int j, int step_w, int step_h){
        int sum = 0;
        int length = step_w * step_h;
        for (int h = i; h < i+step_w; h++){
            for (int k = j; k< j+step_h; k++){
                sum += this.intensity_image[h][k];

            }
        }
        sum = (int)(sum / length);
        return sum;
    }

    public BufferedImage getGrayscale(){
        BufferedImage output = new BufferedImage(this.width, this.height, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < this.width; i ++){
            for (int j = 0; j < this.height; j++){
                output.setRGB(i, j, new Color(this.intensity_image[i][j], this.intensity_image[i][j], this.intensity_image[i][j]).getRGB());
            }
        }
        return output;
    }

    public void saveImage(String path) throws Exception{
        ImageIO.write(this.grayscale_image, "jpg", new File(path));
    }

    public void saveImage(int[][] input, int w, int h, String path) throws Exception{
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < w; i ++){
            for (int j = 0; j < h; j++){
                output.setRGB(i, j, new Color(input[i][j], input[i][j], input[i][j]).getRGB());
            }
        }
        ImageIO.write(output, "jpg", new File(path));
    }
}
