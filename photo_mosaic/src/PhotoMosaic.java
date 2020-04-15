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


