package photo_mosaic;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.io.*;

public class PhotoMosaic {
    public static void make_mosaic(String base_path, String[] paths, int w, int h) throws Exception{
        ImageProcessor base = new ImageProcessor(base_path);
        if (base.width % w != 0 | base.height%h != 0){
            return;
        }

        ImageProcessor[] processors = new ImageProcessor[paths.length];
        Image[]  images = new Image[paths.length];
        ImageContainer container = new ImageContainer(paths.length);

        //TODO make this more memory efficient
        for (int i = 0; i < paths.length; i ++){
            processors[i] = new ImageProcessor(paths[i]);
            images[i] = new Image(processors[i].getAverageIntensity(), processors[i].resizeImage(w, h), w, h);
            container.add(images[i]);
        }

        //w is the width of the small pics
        //h is the height pf the small pics
        //the number of small pics horizontally = base width / w, same with height
        int pics_horizontal = base.width / w;
        int pics_vertical = base.height / h;

        for (int i = 0, x = 0; i < pics_horizontal; i++, x+= w){
            for (int j = 0, y = 0; j < pics_vertical; j++, y+= h){
                int base_intensity = base.averageIntensity(x, y, w, h);
                int[][] insertion = container.getImageClosest(base_intensity).getImage();
                base.insertImage(insertion, w, h, x, y);
            }
        }
        base.saveImage(base.intensity_image, base.width, base.height, "mosaic");
    }
    public static void main(String[] args) throws Exception{
        ImageProcessor ip = new ImageProcessor("/home/mycicle/git/ZoomAndEnhance/photo_mosaic_grayscale/src/main/resources/night2.jpg");
        ip.saveImage("grayNight2");
        int[][] resize = ip.resizeImage(470, 197);
        ip.saveImage(resize, 470, 197, "resize_test");

        String[] paths = new String[] {
        };
        make_mosaic("/home/mycicle/git/ZoomAndEnhance/photo_mosaic_grayscale/src/main/resources/night2.jpg", paths, );
    }
}



