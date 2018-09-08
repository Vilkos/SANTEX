package com.santex.service.implementation;

import com.santex.dao.ProductDao;
import com.santex.service.ProductImageService;
import com.twelvemonkeys.image.ResampleOp;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductDao productDao;
    private static final Path images = Paths.get(System.getProperty("catalina.home"), "images");
    private static Path upload = Paths.get(System.getProperty("catalina.home"), "upload");
    private final int[] IMAGE_SCALE_DIMENSIONS = new int[]{300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500};

    public ProductImageServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    public synchronized void add(InputStream in, String SKU) throws IOException {
        Path imageFolder = images.resolve(SKU);
        if (Files.notExists(imageFolder)) {
            Files.createDirectory(imageFolder);
        }

        BufferedImage input = ImageIO.read(in);

        if (input != null) {
            if (input.getWidth() != input.getHeight()) {
                input = crop(input);
            }
        }

        for (int size : IMAGE_SCALE_DIMENSIONS) {
            String imageName = SKU + "_" + size + "x" + size + ".jpg";
            Path imagePath = imageFolder.resolve(imageName);

            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(imagePath, CREATE, TRUNCATE_EXISTING))) {

                BufferedImageOp resampler = new ResampleOp(size, size, ResampleOp.FILTER_LANCZOS);
                BufferedImage outputImage = resampler.filter(input, null);

                encode(outputImage, out);

                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Async
    public void addFromWeb(MultipartFile image, String SKU) {
        try {
            add(image.getInputStream(), SKU);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void addInBatch() {
        if (Files.notExists(upload)) try {
            Files.createDirectory(upload);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(upload)) {
            Path file;
            String productSKU;
            for (Path path : stream) {
                productSKU = FilenameUtils.removeExtension(path.getFileName().toString());
                file = upload.resolve(path.getFileName());
                if (productDao.findBySKU(productSKU).isPresent()) {
                    try (InputStream in = Files.newInputStream(file)) {
                        add(in, productSKU);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // removing folder with all the content.
    public void remove(String SKU) {
        Path imageFolder = images.resolve(SKU);
        try {
            if (Files.exists(imageFolder)) {
                Files.walkFileTree(imageFolder, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.deleteIfExists(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try { // The same with Java 8
            Files.walk(imageFolder, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private BufferedImage crop(BufferedImage originalImage) {
        int minSide;
        int maxSide;
        if (originalImage.getWidth() - originalImage.getHeight() > 0) {
            maxSide = originalImage.getWidth();
            minSide = originalImage.getHeight();
        } else {
            maxSide = originalImage.getHeight();
            minSide = originalImage.getWidth();
        }
        int x;
        int y;
        int cropSize = (maxSide - minSide) / 2;
        if (originalImage.getWidth() > originalImage.getHeight()) {
            x = cropSize;
            y = 0;
        } else {
            x = 0;
            y = cropSize;
        }
        return originalImage.getSubimage(x, y, minSide, minSide);
    }

    private void encode(BufferedImage bufferedImage, OutputStream outputStream)
            throws IOException {
        if (bufferedImage == null) {
            throw new IllegalArgumentException("Null 'image' argument.");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("Null 'outputStream' argument.");
        }

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam p = writer.getDefaultWriteParam();
        p.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        p.setCompressionQuality(.90f);
        ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(bufferedImage, null, null), p);
        ios.flush();
        writer.dispose();
        ios.close();
    }
}
