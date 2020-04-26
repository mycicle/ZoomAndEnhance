function photomosaic_color(inputvideo, inputphoto, outputname, num_photos_x, num_photos_y)

%extract_and_convert(inputvideo, 120);
IMG = imread([inputphoto, '.jpg']);
Size_IMG = size(IMG);
Output = zeros(Size_IMG(1), Size_IMG(2), 3, 'uint8');
Color_Index = imresize(IMG, [num_photos_y num_photos_x]);
Size_Index = size(Color_Index);
Photo_Index = zeros(Size_Index(1), Size_Index(2), 'int32');

files = dir(['colorframes', '/*.jpg']);
Num_Files = size(files,1);
Frame_Index = zeros(1, Num_Files, 3);

for i = 1:Num_Files
    Current_IMG = imread(['colorframes/colorframe', int2str(i), '.jpg']);
    Singularity_Current_IMG = imresize(Current_IMG, [1 1]);
    for j = 1:3
        Frame_Index(1, i, j) = Singularity_Current_IMG(1, 1, j);
    end
end

for i = 1:Size_Index(2)
    for j = 1:Size_Index(1)
            n = 0;
            while Photo_Index(j, i) == 0
                Random = randi([1, int16(Num_Files)]);
                for l = Random:int16(Num_Files)
                    Difference_R =(abs(int32(Frame_Index(1, l, 1)) - int32(Color_Index(j, i, 1))));
                    Difference_G =(abs(int32(Frame_Index(1, l, 2)) - int32(Color_Index(j, i, 2))));
                    Difference_B =(abs(int32(Frame_Index(1, l, 3)) - int32(Color_Index(j, i, 3))));
                    if (Difference_R <= n && Difference_G <= n && Difference_B <= n)
                        Photo_Index(j, i) = l;
                        break
                    else
                        n = n + 3;
                    end
                end
            end
    end
end

for i = 1:Size_Index(2)
    for j = 1: Size_Index(1)
        Add_IMG = imread(['colorframes/colorframe', int2str(Photo_Index(j, i)), '.jpg']);
        Size_Add_IMG = size(Add_IMG);
        Min_Size = min(Size_Add_IMG(1),Size_Add_IMG(2));
        Square_IMG = zeros(Min_Size, Min_Size, 3, 'uint8');
        for k = 1:Min_Size
            for l = 1:Min_Size
                Vertical_Correction = ((Size_Add_IMG(1)-Min_Size)/2);
                Horizontal_Correction = ((Size_Add_IMG(2)-Min_Size)/2);
                for m = 1:3
                    Square_IMG(k, l, m) = Add_IMG((k + Vertical_Correction), (l + Horizontal_Correction), m);
                end
            end
        end
        Small_x = (Size_IMG(2)/num_photos_x);
        Small_y = (Size_IMG(1)/num_photos_y);
        Small_IMG = imresize(Square_IMG, [ Small_y Small_x]);
        for k = 1:Small_y
            for l = 1:Small_x
                for m = 1:3
                    Output((l+((j-1)*Small_x)), (k+((i-1)*Small_y)), m) = Small_IMG(k, l, m);
                end
            end
        end
    end
end

imwrite(Output,[outputname '.jpg']);

end