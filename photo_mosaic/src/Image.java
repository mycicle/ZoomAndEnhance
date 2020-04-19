public class Image {

    public RGB average_color;
    public int[][] image;

    public Image(RGB average_color, int[][] image){
        this.average_color= average_color;
        this.image = image;
    }

    public RGB getAverageColor() { return this.average_color; }
    public RGB getAverageColorRounded() {
        int red = 0; int green = 0; int blue =0;

        for (int i =0; i < 3; i++){
            red = ((average_color.getRed()+5)/10)*10;
            green = ((average_color.getGreen()+5)/10)*10;
            blue = ((average_color.getBlue()+5)/10)*10;
        }
        return new RGB(red, green, blue);
    }

    public int[][] getImage() { return this.image; }
}
