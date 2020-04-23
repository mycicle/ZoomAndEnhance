function print_file_paths_color(inputfolder)
files = dir([inputfolder, '/*.jpg']);
folder = what(inputfolder);
path = folder.path;
num_files = size(files,1);
fileID = fopen('color_paths.txt', 'a');
for i = 1:num_files
    fprintf(fileID,'%s\n', [path, '\frame', int2str(i), '.jpg']);
end
fclose(fileID);
end