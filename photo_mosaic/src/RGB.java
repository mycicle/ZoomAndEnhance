class RGB{
    public int[] rgb;

    public RGB(int red, int green, int blue){
        this.rgb = new int[3];
        this.rgb[0] = red;
        this.rgb[1] = green;
        this.rgb[2] = blue;
    }

    public int[] getRGB(){
        return this.rgb;
    }
    public int getRed(){
        return this.rgb[0];
    }
    public int getBlue(){
        return this.rgb[1];
    }
    public int getGreen(){
        return this.rgb[2];
    }

    public String toString(){
        return "(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
    }

}