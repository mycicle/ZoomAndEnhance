function bw(filename, bwname)
IMG = imread(filename, 'jpg');
%IMG = readjpg(filename);
s = size(IMG);
r = zeros(s(1), s(2));
g = zeros(s(1), s(2));
b = zeros(s(1), s(2));
r = IMG(:,:,1); g = IMG(:,:,2); b = IMG(:,:,3);
y = 0.299*r + 0.587*g + 0.114*b;
imwrite(y, ['./bwimages/bwimage', bwname, '.jpg']);
end