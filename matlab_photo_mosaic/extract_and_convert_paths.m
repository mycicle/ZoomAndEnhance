function extract_and_convert_paths(inputvidname, skip_frames)
vid = VideoReader([inputvidname, '.mp4']);
num_frames = vid.NumberOfFrames;
mkdir colorframes
mkdir bwframes
count = 1;

folder_bw = what('./bwframes');
path_bw = folder_bw.path;
fileID_bw = fopen('bw_paths.txt', 'a');

folder_color = what('./colorframes/');
path_color = folder_color.path;
fileID_color = fopen('color_paths.txt', 'a');

for i = 1:skip_frames:num_frames
    frames = read(vid, i);
    imwrite(frames,['./colorframes/colorframe', int2str(count), '.jpg']);
    
    fprintf(fileID_color,'%s\n', [path_color, '\frame', int2str(count), '.jpg']);
    
    s = size(frames);
    r = zeros(s(1), s(2));
    g = zeros(s(1), s(2));
    b = zeros(s(1), s(2));
    r = frames(:,:,1); g = frames(:,:,2); b = frames(:,:,3);
    bwframes = 0.299*r + 0.587*g + 0.114*b;
    imwrite(bwframes,['./bwframes/bwframe', int2str(count), '.jpg']);
    
    fprintf(fileID_bw,'%s\n', [path_bw, '\bwframe', int2str(count), '.jpg']);
    
    count = count + 1;
end

fclose(fileID_bw);
fclose(fileID_color);

end