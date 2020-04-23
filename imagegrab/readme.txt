make sure matlab files and the video file are in the same directory. Then run doit.m in MATLAB.

frame_extractor takes the inputs of the file name (without .mp4 at the end) and the rate to grab frames.
Example, frame_extractor('test', 10);
test is the name of the file and frames are grabbed every 10th frame.
frame_extractor creates a new folder called images and stores the color grabbed frames.
The new folder is created in the directory with the MATLAB files.


bw_converter reads the images in the folder images and coverts them the band and white.
It creates a new folder called bwimages and stores the converted frames. The new folder
is created in the directory with the MATLAB files.

print_file_paths reads the file path fomr each black and white image and prints the file
paths to a .txt file. This file is named paths.txt and is created in the directory with 
the MATLAB files.

There should be no need to change anything in doit.m outside of the name of the input 
video file and the frame grabbing rate.



