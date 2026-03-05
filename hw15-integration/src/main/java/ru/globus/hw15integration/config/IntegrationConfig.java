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
import java.util.Collections;
import java.util.Map;

@Configuration
@Slf4j
public class IntegrationConfig {

    @Bean
    public MessageChannel processDirectoryChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow imagePipeline(ImageProcessService imageProcessService, IOService ioService) {
        return IntegrationFlow.from(processDirectoryChannel()).handle(this::validateAndListFiles)
                .route(File[].class, files -> files.length > 0,
                       mapping -> mapping
                         .subFlowMapping(true, sf -> sf
                              .split()
                              .enrichHeaders(
                                      h -> h.headerFunction("originalFilename", m -> ((File) m.getPayload()).getName())
                                            .headerFunction("originalDir", m -> ((File) m.getPayload()).getParent()))
                              .transform(File.class, ioService::readImage)
                              .handle(BufferedImage.class,(image, headers) -> imageProcessService.resizeImage(
                                                                                          image, 800, 600))
                              .handle(imageProcessService, "grayscaleImage").handle(imageProcessService, "sharpenImage")
                              .handle(BufferedImage.class, (image, headers) -> {
                                  String originalFilename = (String) headers.get("originalFilename");
                                  String originalDir = (String) headers.get("originalDir");
                                  String outputFilename = originalDir + "/processed/" + originalFilename;
                                  ioService.writeImage(image, outputFilename);
                                  return outputFilename;
                              }).aggregate())
                         .subFlowMapping(false, sf -> sf.transform(files -> Collections.emptyList())))
                              .get();
    }

    private File[] validateAndListFiles(String directoryPath, Map<String, Object> headers) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Directory not found: " + directoryPath);
        }
        File[] files = directory.listFiles((dir, name) -> name.matches(".*\\.(jpg|jpeg|png)$"));
        if (files == null || files.length == 0) {
            return new File[0];
        }
        return files;
    }

}
