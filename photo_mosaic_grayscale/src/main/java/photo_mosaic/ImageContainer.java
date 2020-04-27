package photo_mosaic;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class ImageContainer {
    ArrayList<Image> images;
    ArrayList<Integer> done;

    public ImageContainer(int expected_size){
        //having the correct expected size can make this noticeably faster
        images = new ArrayList<>(expected_size);
        done = new ArrayList<>(expected_size);
    }
    public ImageContainer(){
        this(0);
    }

    public void add(Image im){
        this.images.add(im);
    }

    public Image getImage(int average_color){
        Image output = null;
        for (int i = 0; i < images.size(); i ++){
            Image current_image = images.get(i); //minor optimization so we don't have to use the get function multiple times
            if (!(done.contains(i)) && (current_image.getAverageColor() == average_color)) {
                //if we havent seen it before and it has the correct color
                done.add(i);
                output = current_image;
                return output;
            }
        }

        //same as beforebut we include images form done
        if (output == null) {
            for (int i = 0; i < images.size(); i ++){
                Image current_image = images.get(i); //minor optimization so we don't have to use the get function multiple times
                if ((current_image.getAverageColor() == average_color)) {
                    //if we havent seen it before and it has the correct color
                    done.add(i);
                    output = current_image;
                    return output;
                }
            }
        }

        return output;
    }


    public Image getImageClosest(int average_color){
        //same thing as get image but we use rounded values (to the nearest ten) for the intensity values
        Image output = null;
        int average_color_rounded = ((average_color+5)/10)*10;
        for (int i = 0; i < images.size(); i ++){
            Image current_image = images.get(i); //minor optimization so we don't have to use the get function multiple times
            if (!(done.contains(i)) && ((((current_image.getAverageColor()+5)/10)*10) == average_color_rounded)) {
                //if we havent seen it before and it has the correct color
                done.add(i);
                output = current_image;
                return output;
            }
        }

        //same as beforebut we include images form done
        if (output == null) {
            for (int i = 0; i < images.size(); i ++){
                Image current_image = images.get(i); //minor optimization so we don't have to use the get function multiple times
                if (((((current_image.getAverageColor()+5)/10)*10) == average_color_rounded)) {
                    //if we havent seen it before and it has the correct color
                    done.add(i);
                    output = current_image;
                    return output;
                }
            }
        }

        //this will get the "close enough" image
        if (output == null){
            //it's 256 so the first image always works
            int disparity = 256;
            Image best_image = null;
            for (int i = 0; i < images.size(); i++){
                Image current_image = images.get(i);

                //if the difference between the desired color and the current image's color is less than the previous one, choose this one as your base
                if (java.lang.Math.abs(average_color - current_image.getAverageColor()) < disparity){
                    best_image = current_image;
                    disparity = java.lang.Math.abs(average_color - current_image.getAverageColor());
                }
            }
            output = best_image;
        }

        return output;
    }

}
