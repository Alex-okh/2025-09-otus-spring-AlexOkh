package ru.globus.hw15integration.service;

import java.awt.image.BufferedImage;
import java.io.File;

public interface IOService {
    BufferedImage readImage(File file);

    void writeImage(BufferedImage image, String originalFilename);
}
