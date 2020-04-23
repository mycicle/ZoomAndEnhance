function print_file_paths_bw(inputfolder)
files = dir([inputfolder, '/*.jpg']);
folder = what(inputfolder);
path = folder.path;
num_files = size(files,1);
fileID = fopen('bw_paths.txt', 'a');
for i = 1:num_files
    fprintf(fileID,'%s\n', [path, '\bwframe', int2str(i), '.jpg']);
end
fclose(fileID);
end