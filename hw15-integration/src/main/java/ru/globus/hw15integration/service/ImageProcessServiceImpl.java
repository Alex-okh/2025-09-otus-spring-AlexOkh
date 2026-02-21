package ru.globus.hw15integration.service;

import org.springframework.stereotype.Service;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

@Service
public class ImageProcessServiceImpl implements ImageProcessService {

    @Override
    public BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        var resized = new BufferedImage(newWidth, newHeight, originalImage.getType());
        double scaleX = (double) newWidth / originalImage.getWidth();
        double scaleY = (double) newHeight / originalImage.getHeight();
        AffineTransformOp scaleOperation = new AffineTransformOp(AffineTransform.getScaleInstance(scaleX, scaleY),
                                                                 AffineTransformOp.TYPE_BICUBIC);
        scaleOperation.filter(originalImage, resized);
        return resized;
    }

    @Override
    public BufferedImage grayscaleImage(BufferedImage originalImage) {
        var grayscaleImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                                               BufferedImage.TYPE_BYTE_GRAY);
        var convertOp = new ColorConvertOp(originalImage.getColorModel()
                                                        .getColorSpace(), grayscaleImage.getColorModel()
                                                                                        .getColorSpace(), null);
        convertOp.filter(originalImage, grayscaleImage);
        return grayscaleImage;
    }

    @Override
    public BufferedImage sharpenImage(BufferedImage originalImage) {
        var sharpenedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                                               originalImage.getType());
        float[] kernelData = {0, -1, 0, -1, 5, -1, 0, -1, 0};
        Kernel kernel = new Kernel(3, 3, kernelData);
        var convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        convolveOp.filter(originalImage, sharpenedImage);
        return sharpenedImage;
    }
}
