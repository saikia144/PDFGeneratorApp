package com.pdf.generator.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageUtil {
    public static String imageDownloader(String url, String name) throws IOException {
        File file = downloadImage(url, name);
        return file.getAbsolutePath();
    }

    private static File downloadImage(String imageUrl, String name) throws IOException {
        URL url = new URL(imageUrl);
        String folderPath = "D:\\PDFGenerator\\images\\player";
        String filePath = folderPath + "\\" + name + ".png";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean folderCreated = folder.mkdirs();
            if (!folderCreated) {
                System.out.println("Failed to create the folder.");
            }
        }

        File imageFile = new File(filePath);
        System.out.println(imageFile.toPath());
        Path imagePath = imageFile.toPath();
        Files.copy(url.openStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return imageFile;
    }
}
