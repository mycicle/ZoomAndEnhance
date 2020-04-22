package photo_mosaic;

public class Image {
    public int average_color;
    public int[][] image;
    public int width, height;

    public Image(int average_color, int[][] image, int width, int height){
        this.average_color = average_color;
        this.image = image;
        this.width = width;
        this.height = height;
    }
    public Image(int average_color, int[][] image){
        this(average_color, image, 0, 0);
    }

    public int getAverageColor(){
        return this.average_color;
    }

    public int getAverageColorRounded(){
        //this rounds the color to the nearest ten
        return (int) (((this.average_color+5)/10)*10);
    }

    public int[][] getImage() {
        return this.image;
    }
}
