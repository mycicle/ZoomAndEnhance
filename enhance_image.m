function image = enhance_image(img)
    edge_threshold = 0.4;
    amount = 0.3;
%     PSF = fspecial('gaussian', 5, 5);
%     PSF = fspecial('unsharp');  
    img2 = localcontrast(img, edge_threshold, amount);
    %Wiender2 requires a grayscale image be loaded in, it is possible to do
    %the conversion back and forth between grayscale and RGB, we can
    %possibly just modify pixel intensity with code from a previous
    %homework and then put chromonance back as it was
%     image = wiener2(img2, [5,5]);
%     image = imsharpen(img2);
%     image = imsharpen(deconvlucy(img2, PSF, 20));
%     image = deconvlucy(imsharpen(img2), PSF, 20);
    end
