package com.ssafy.project.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static ByteArrayResource resizeAndCompress(byte[] imageBytes, int targetWidth, float jpegQuality) throws IOException {
        try (InputStream is = new ByteArrayInputStream(imageBytes)) {
            BufferedImage originalImage = ImageIO.read(is);
            if (originalImage == null) {
                throw new IllegalArgumentException("지원하지 않는 이미지 포맷입니다.");
            }
            return resizeAndCompress(originalImage, targetWidth, jpegQuality);
        }
    }
    public static ByteArrayResource resizeAndCompress(BufferedImage originalImage, int targetWidth, float jpegQuality) throws IOException {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int targetHeight = (originalHeight * targetWidth) / originalWidth;

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(jpegQuality);
        }

        writer.setOutput(new MemoryCacheImageOutputStream(baos));
        writer.write(null, new IIOImage(resized, null, null), param);
        writer.dispose();

        return new ByteArrayResource(baos.toByteArray());
    }

    public static ByteArrayResource resizeAndCompress(MultipartFile multipartFile, int targetWidth, float jpegQuality) throws IOException {
        // 1. MultipartFile → BufferedImage (확장자에 관계없이 처리 가능)
        BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
        if (originalImage == null) {
            throw new IllegalArgumentException("지원하지 않는 이미지 포맷입니다: " + multipartFile.getOriginalFilename());
        }

        // 2. Resize
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int targetHeight = (originalHeight * targetWidth) / originalWidth;

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        // 3. JPEG 압축
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(jpegQuality); // 예: 0.6f
        }

        writer.setOutput(new MemoryCacheImageOutputStream(baos));
        writer.write(null, new IIOImage(resized, null, null), param);
        writer.dispose();

        return new ByteArrayResource(baos.toByteArray());
    }


}
