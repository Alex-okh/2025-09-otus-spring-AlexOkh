package ru.globus.hw15integration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class IOServiceImpl implements IOService {

    @Override
    public BufferedImage readImage(File file) {

        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            log.error("Error while reading image from {}", file.getName(), e);
        }
        return null;
    }

    @Override
    public void writeImage(BufferedImage image, String filename) {
        try {
            File outputFile = new File(filename + "_processed.png");
            outputFile.getParentFile()
                      .mkdirs();
            ImageIO.write(image, "png", outputFile);
            log.info("Saved image to {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error while saving image {}", filename, e);
        }
    }
}
