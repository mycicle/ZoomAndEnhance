function bw_converter(infolderpath)
files = dir([infolderpath '/*.jpg']);
num_files = size(files,1);
mkdir bwimages
for i = 1:num_files
    bw([infolderpath,'/', 'image', int2str(i), '.jpg'],int2str(i))
end
end