vid = VideoReader('C:\Users\miked\git\ZoomAndEnhance\photo_mosaic_grayscale\src\main\resources\battle.mp4');
num_frames = vid.NumberOfFrames;
for i = 1:100:num_frames
    frames=read(vid,i);
    imwrite(frames,['Image', int2str(i), '.jpg']);
    im(i) = image(frames);
end