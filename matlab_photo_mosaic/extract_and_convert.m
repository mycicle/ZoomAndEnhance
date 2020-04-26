function extract_and_convert(inputvidname, skip_frames)
vid = VideoReader([inputvidname, '.mp4']);
num_frames = vid.NumberOfFrames;
mkdir colorframes
mkdir bwframes
count = 1;
for i = 1:skip_frames:num_frames
    frames = read(vid, i);
    imwrite(frames,['./colorframes/colorframe', int2str(count), '.jpg']);
    
    s = size(frames);
    r = zeros(s(1), s(2));
    g = zeros(s(1), s(2));
    b = zeros(s(1), s(2));
    r = frames(:,:,1); g = frames(:,:,2); b = frames(:,:,3);
    bwframes = 0.299*r + 0.587*g + 0.114*b;
    imwrite(bwframes,['./bwframes/bwframe', int2str(count), '.jpg']);
    
    count = count + 1;
end
end