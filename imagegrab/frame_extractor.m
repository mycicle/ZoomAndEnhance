function frame_extractor(inputvidname, skip_frames)
vid = VideoReader([inputvidname, '.mp4']);
num_frames = vid.NumberOfFrames;
count = 1;
mkdir frames
for i = 1:skip_frames:num_frames
    frames = read(vid, i);
    imwrite(frames,['./frames/frame', int2str(count), '.jpg']);
    count = count+1;
end
end