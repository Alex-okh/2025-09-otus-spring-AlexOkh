package ru.globus.hw15integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import ru.globus.hw15integration.service.IOService;
import ru.globus.hw15integration.service.ImageProcessService;
import java.awt.image.BufferedImage;
import java.io.File;

@Configuration
@Slf4j
public class IntegrationConfig {

    @Bean
    public MessageChannel processDirectoryChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow imagePipeline(ImageProcessService imageProcessService, IOService ioService) {
        return IntegrationFlow.from(processDirectoryChannel())
                              .<String>handle((payload, headers) -> {
                                  File directory = new File(payload);
                                  if (!directory.exists() || !directory.isDirectory()) {
                                      throw new IllegalArgumentException("Directory not found: " + payload);
                                  }
                                  File[] files = directory.listFiles(
                                          (dir, name) -> name.matches(".*\\.(jpg|jpeg|png)$"));
                                  if (files == null || files.length == 0) {
                                      return null;
                                  }
                                  return files;
                              })
                              .split()
                              .enrichHeaders(
                                      h -> h.headerFunction("originalFilename", m -> ((File) m.getPayload()).getName()))
                              .enrichHeaders(
                                      h -> h.headerFunction("originalDir", m -> ((File) m.getPayload()).getParent()))
                              .transform(File.class, ioService::readImage)
                              .handle(BufferedImage.class,
                                      (image, headers) -> imageProcessService.resizeImage(image, 800, 600))
                              .handle(imageProcessService, "grayscaleImage")
                              .handle(imageProcessService, "sharpenImage")
                              .handle(BufferedImage.class, (image, headers) -> {
                                  String originalFilename = (String) headers.get("originalFilename");
                                  String originalDir = (String) headers.get("originalDir");
                                  ioService.writeImage(image, originalDir + "/processed/" + originalFilename);
                                  return null;
                              })
                              .get();
    }
}
