function print_file_paths(inputfolder)
files = dir([inputfolder, '/*.jpg']);
folder = what(inputfolder);
path = folder.path;
num_files = size(files,1);
fileID = fopen('paths.txt', 'a');
for i = 1:num_files
    fprintf(fileID,'%s\n', [path, '\bwimage', int2str(i), '.jpg']);
end
fclose(fileID);
end