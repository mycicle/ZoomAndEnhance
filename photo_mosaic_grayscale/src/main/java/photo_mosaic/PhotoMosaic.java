package photo_mosaic;

import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.io.*;

public class PhotoMosaic {
    public static void make_mosaic(String base_path, String[] paths, int w, int h) throws Exception{
        ImageProcessor base = new ImageProcessor(base_path);
        if (base.width % w != 0 | base.height%h != 0){
            System.out.println("bad dims");
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
        base.saveImage(base.intensity_image, base.width, base.height, "C:\\Users\\miked\\git\\ZoomAndEnhance\\photo_mosaic_grayscale\\mosaic.jpg");
    }


    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{

        final File folder = new File("src\\main\\resources");
        List<String> result = new ArrayList<>();
        search(".*\\.jpg", folder, result);
        String[] paths = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            paths[i] = result.get(i);
        }

        make_mosaic("base_image_8k.jpg", paths, 48, 27);
    }
}



