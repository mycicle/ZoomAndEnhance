% video = VideoReader("./assets/battle.mp4");
img = imread('Image1001.jpg');
output = enhance_image(img);
imshowpair(img, output,'montage');