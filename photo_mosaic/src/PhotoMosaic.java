import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.nio.Buffer;


public class PhotoMosaic {


    public static void main(String[] args) throws Exception {
        ImageProcessor ip = new ImageProcessor("./assets/star_wars.jpg");
        int[][] image = ip.getImageRGB();

        RGB[][] decoded = new RGB[ip.getHeight()][ip.getWidth()];
        int[][] encoded = new int[ip.getHeight()][ip.getWidth()];

        for(int i = 0; i < ip.getHeight(); i++){
            for (int j = 0; j < ip.getWidth(); j++){
//                System.out.println(ip.decodeRGB(image[i][j]));
                decoded[i][j] = ip.decodeRGB(image[i][j]);
            }
        }
        System.out.println("Average Image Color: " + ip.getAverageColorRGB());
        for(int i = 0; i < ip.getHeight(); i++){
            for (int j = 0; j < ip.getWidth(); j++){
                encoded[i][j] = ip.encodeRGB(decoded[i][j]);
            }
        }



        BufferedImage br = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < ip.getWidth(); i++){
            for (int j = 0; j < ip.getHeight(); j++){
                br.setRGB(j, i, encoded[i][j]);
            }
        }
        ImageIO.write(br, "jpg", new File("./test_from_encode.jpg"));

    }
}


