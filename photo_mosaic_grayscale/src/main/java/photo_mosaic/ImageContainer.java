package photo_mosaic;
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

    }
    public Image getImageCloseEnough(int average_color, int threshold){

    }
    public Image getImageRounded(int average_color){

    }

}
