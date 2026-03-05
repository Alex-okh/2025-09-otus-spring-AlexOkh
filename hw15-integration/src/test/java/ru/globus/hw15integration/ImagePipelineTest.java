package ru.globus.hw15integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.globus.hw15integration.gateway.ProcessingGateway;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ImagePipelineTest {
    @TempDir
    Path tempDir;

    @Autowired
    private ProcessingGateway processingGateway;

    @Test
    @DisplayName("Должен создавать новый файл в ожидаемом каталоге с ожидаемым названием")
    void testImagePipeline() throws IOException {
        File inputDir = tempDir.toFile();
        File testImage = new File(inputDir, "test.png");
        createTestImage(testImage, 300, 400);

        var returnedProcessedFiles = processingGateway.process(inputDir.getAbsolutePath());

        File processedDir = new File(inputDir, "processed");
        assertThat(processedDir).exists()
                                .isDirectory();
        var processedFiles = processedDir.listFiles();
        assertThat(processedFiles).hasSize(1);
        assertThat(processedFiles[0].getName()).endsWith("processed.png");
        assertThat(returnedProcessedFiles).size().isEqualTo(processedFiles.length);
    }

    @Test
    @DisplayName("Не должен создавать новый каталог если нет картинок в источнике")
    void testImagePipelineWithNonimage() throws IOException {
        File inputDir = tempDir.toFile();
        File testImage = new File(inputDir, "test.txt");
        createTestImage(testImage, 300, 400);

        var processedFiles = processingGateway.process(inputDir.getAbsolutePath());

        File processedDir = new File(inputDir, "processed");
        assertThat(processedDir).doesNotExist();
        assertThat(processedFiles).isEmpty();
    }

    private void createTestImage(File file, int width, int height) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(img, "jpg", file);
    }
}
