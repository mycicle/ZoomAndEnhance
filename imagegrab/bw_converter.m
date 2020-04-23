function bw_converter(infolderpath)
files = dir([infolderpath '/*.jpg']);
num_files = size(files,1);
mkdir bwframes
for i = 1:num_files
    bw([infolderpath,'/', 'frame', int2str(i), '.jpg'],int2str(i))
end
end