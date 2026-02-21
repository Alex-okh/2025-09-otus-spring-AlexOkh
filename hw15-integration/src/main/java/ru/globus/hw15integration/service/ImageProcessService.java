package ru.globus.hw15integration.service;

import java.awt.image.BufferedImage;

public interface ImageProcessService {
    BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight);

    BufferedImage grayscaleImage(BufferedImage originalImage);

    BufferedImage sharpenImage(BufferedImage originalImage);
}
