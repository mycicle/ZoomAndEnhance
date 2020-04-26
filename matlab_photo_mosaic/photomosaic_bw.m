function photomosaic_bw(inputvideo, inputphoto, outputname, num_photos_x, num_photos_y)

%extract_and_convert(inputvideo, 120);
IMG = imread([inputphoto, '.jpg']);
Size_IMG = size(IMG);
Output = zeros(Size_IMG(1), Size_IMG(2), 'uint8');
Color_Index = imresize(IMG, [num_photos_y num_photos_x]);
Size_Index = size(Color_Index);
Photo_Index = zeros(Size_Index(1), Size_Index(2), 'int32');

files = dir(['bwframes', '/*.jpg']);
Num_Files = size(files,1);
Frame_Index = zeros(1, Num_Files);

for i = 1:Num_Files
    Current_IMG = imread(['bwframes/bwframe', int2str(i), '.jpg']);
    Singularity_Current_IMG = imresize(Current_IMG, [1 1]);
    Frame_Index(1, i) = Singularity_Current_IMG(1, 1); 
end

for i = 1:Size_Index(2)
    for j = 1:Size_Index(1)
        n = 0;
        while Photo_Index(j, i) == 0
            Random_1 = randi([1, int16(Num_Files)]);
            Random_2 = randi([1, int16(Num_Files)]);
            for k = min(Random_1, Random_2):max(Random_1, Random_2)
                Difference = (abs(int32(Frame_Index(1,k)) - int32(Color_Index(j, i))));
                if (Difference <= n)
                    Photo_Index(j, i) = k;
                    break
                else
                    n = n + 1;
                end
            end
        end
    end
end

for i = 1:Size_Index(2)
    for j = 1: Size_Index(1)
        Add_IMG = imread(['bwframes/bwframe', int2str(Photo_Index(j, i)), '.jpg']);
        Size_Add_IMG = size(Add_IMG);
        Min_Size = min(Size_Add_IMG(1),Size_Add_IMG(2));
        Square_IMG = zeros(Min_Size,Min_Size, 'uint8');
        for k = 1:Min_Size
            for r = 1:Min_Size
                Vertical_Correction = ((Size_Add_IMG(1)-Min_Size)/2);
                Horizontal_Correction = ((Size_Add_IMG(2)-Min_Size)/2);
                Square_IMG(k,r) = Add_IMG((k + Vertical_Correction), (r + Horizontal_Correction));
            end
        end
        Small_x = (Size_IMG(2)/num_photos_x);
        Small_y = (Size_IMG(1)/num_photos_y);
        Small_IMG = imresize(Square_IMG, [ Small_y Small_x]);
        for k = 1:Small_y
            for r = 1:Small_x
                Output((r+((j-1)*Small_x)), (k+((i-1)*Small_y))) = Small_IMG(r,k);
            end
        end
    end
end

imwrite(Output, [outputname, '.jpg']);

end