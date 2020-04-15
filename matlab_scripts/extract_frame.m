% vid = VideoReader('./assets/battle.mp4');
% num_frames = vid.NumberOfFrames;
% for i = 1:1000:num_frames
%     frames=read(vid,i);
%     imwrite(frames,['Image', int2str(i), '.jpg']);
%     im(i) = image(frames);
% end
% 

function frame = extract_frame(video, index)
    try
    frame = read(video, index);
    
    catch(exception);
        disp("Error! Check the index of the frame you are trying to extract");
        disp(exception.getReport());
    end

    
