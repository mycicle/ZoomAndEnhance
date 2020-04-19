


import java.util.*;

public class ImageContainer {

    ArrayList<Image> images;
    ArrayList<Image> done;

    public ImageContainer() {
        images = new ArrayList<>();
        done = new ArrayList<>();
    }

    public void add(Image image){
        images.add(image);
    }

    public ArrayList<Image> getImages(){
        return this.images;
    }

    public Image getImages(RGB average_color){
        Image output;
        int red = 0; int green = 0; int blue = 0;

        for (int i = 0; i < 3; i++){
            red = ((average_color.getRed()+5)/10)*10;
            green = ((average_color.getGreen()+5)/10)*10;
            blue = ((average_color.getBlue()+5)/10)*10;
        }

        //if we have a new image that fits the bill
        for (Image i : images){
            if ((i.getAverageColorRounded().equals(new RGB(red, green, blue)) && !(done.contains(i)))){
                done.add(i);
                return i;
            }
        }
        //if we need to repeat images
        for (Image i : images){
            if ((i.getAverageColorRounded().equals(new RGB(red, green, blue)))){
                return i;
            }
        }
        return images.get(0);
    }
}

