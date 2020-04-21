import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.nio.Buffer;
import java.util.*;

public class PhotoMosaic {


    public static BufferedImage make_mosaic(String[] paths, int scale) throws Exception{
        ArrayList<ImageProcessor> ips = new ArrayList<>();
        ImageContainer images  = new ImageContainer();
        BufferedImage output;
        ImageProcessor base = null;
        boolean first = true;
        //ips is an array list of all of the photos with the first one being the base image and the subsequent ones being the
        //images to be inserted
        //images is an array list of the images and their average colors. The elements in images correspond to the elements in ips
        //may not need images but it is helpful to have for debugging logic
        for (int i = 0; i < paths.length; i ++){
            if (first){
                base = new ImageProcessor((paths[i]));
                first = false;
            }
            ImageProcessor ip = new ImageProcessor(paths[i]);
            ip.shrink_image_to(base.getWidth(), base.getHeight());
            ips.add(ip);
            images.add(new Image(ip.getAverageColorRGB(), ip.downscaleImage(scale)));
        }

        output = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        //now we have our base image and the output container. We may want to change the return type to a Buffered Image

        int[][] base_colormap = base.downscaleImage(scale);

        for (int i = 0, h = 0; i < base.getHeight(); i+=base.scaled_height, h++){
            for (int j = 0, k = 0; j < base.getWidth(); j+=base.scaled_width, k++){
                base.insertImage(images.getImages(base.decodeRGB(base_colormap[h][k])).getImage(), i, j);
            }
        }

        for (int i = 0; i < base.getHeight(); i++){
            for (int j = 0; j < base.getWidth(); j++){
                output.setRGB(j, i, base.imageRGB[i][j]);
            }
        }
        return output;
    }

    
    public static void main(String[] args) throws Exception {

//        ImageProcessor ip = new ImageProcessor("assets/night2.jpg");
//        BufferedImage bi = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        for (int i = 0; i < ip.getHeight(); i++){
//            for (int j = 0; j < ip.getWidth(); j++){
//                bi.setRGB(j , i, ip.imageRGB[i][j]);
//            }
//        }
//        ImageIO.write(bi, "jpg", new File("./testout.jpg"));


        String[] paths = new String[] {"assets/test2.jpg", "assets/sunset3.jpg", "assets/star_wars.jpg", "assets/night.jpg", "assets/night.jpg", "assets/night3.jpg"};
        BufferedImage mosaic = make_mosaic(paths, 20);
        ImageIO.write(mosaic, "jpg", new File("./mosaic"));
//
//        ImageProcessor ip = new ImageProcessor("assets/star_wars.jpg");
//        ip.shrink_image_to(100,400);
//        BufferedImage bi = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        System.out.println(ip.getWidth() + ip.getHeight());
//        for (int i = 0; i < ip.getHeight(); i++){
//            for (int j = 0; j < ip.getWidth(); j++){
//                bi.setRGB(j , i, ip.imageRGB[i][j]);
//            }
//        }
//        ImageIO.write(bi, "jpg", new File("./shrunk_image_200x200.jpg"));
        /*
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



        BufferedImage brd = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        int[][] downscaled_image_color_map = ip.downscaleImage(40);
        for (int i = 0; i < ip.getWidth(); i++){
            for (int j = 0; j < ip.getHeight(); j++){
                brd.setRGB(j, i, ip.imageRGB[i][j]);
            }
        }
        ImageIO.write(brd, "jpg", new File("./downscale_test.jpg"));

        System.out.println(ip.getScaledHeight());


        BufferedImage cmp_imag = new BufferedImage(40, 40, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < 40; i++){
            for (int j =0; j < 40; j++){
                cmp_imag.setRGB(i, j, downscaled_image_color_map[j][i]);
                System.out.println(downscaled_image_color_map[i][j]);
            }
        }

        ImageIO.write(cmp_imag, "jpg", new File("./downscaled_image_color_map"));

        BufferedImage compound_image = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < ip.getWidth(); i+= ip.getScaledWidth()){
            for (int j = 0; j < ip.getHeight(); j += ip.getScaledHeight()){
                ip.insertImage(downscaled_image_color_map, i, j);
                System.out.println(i);
            }
        }

        for (int i = 0; i < ip.getWidth(); i++){
            for (int j = 0; j < ip.getHeight(); j++){
                compound_image.setRGB(j, i, ip.imageRGB[i][j]);
            }
        }

        ImageIO.write(compound_image, "jpg", new File("./compoundImageTest.jpg"));


        ImageContainer ic = new ImageContainer();

        ic.add(new Image(ip.getAverageColorRegionRGB(downscaled_image_color_map, ip.getScaledHeight(), ip.getScaledWidth()), downscaled_image_color_map));

        BufferedImage image_from_container = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < ip.getWidth(); i+= ip.getScaledWidth()){
            for (int j = 0; j < ip.getHeight(); j += ip.getScaledHeight()){
                ip.insertImage(ic.getImages(ip.decodeRGB(ip.imageRGB[j][i])).getImage(), i, j);

            }
        }
        for (int i = 0; i < ip.getWidth(); i++){
            for (int j = 0; j < ip.getHeight(); j++){
                image_from_container.setRGB(j, i, ip.imageRGB[i][j]);
            }
        }
        ImageIO.write(image_from_container, "jpg", new File("./imageFromContainerTest.jpg"));
 */
        //now to make the small images we will need to make an image processor object for each of them and then use their "outputs" from downscale image as the inputs
        //into the original image

        //pseudo code
        /*
        Image baseImage = ./asset.jpg
        ImageLibrary il = ./video.mp4
        BinnedColorHashmap bch = new BinnedColorHashmap;
        downscaledImageRGB = baseImage.downscaleImage.imageRGB
        colorMap = baseImage.downscaleImage

        for img in il:
            bch.populate(img.getAverageColor, index_in_il)

        for point in colorMap:
            downscaledImageRGB.set(bch.get(point))

         */


    }
}


