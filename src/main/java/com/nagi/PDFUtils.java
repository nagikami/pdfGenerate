package com.nagi;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PDFUtils {
    public static void createBlankPDF(String filename) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            // a valid PDF document requires at least one page
            PDPage blankPage = new PDPage();
            doc.addPage(blankPage);
            doc.save(filename);
        }
    }

    public static void addImageToPDF(String inputFile, String imagePath, String outputFile) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new File(inputFile))) {
            //we will add the image to the first page.
            //PDPage page = doc.getPage(0);

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage,
            // call LosslessFactory.createFromImage() instead
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            int width = pdImage.getWidth();
            int height = pdImage.getHeight();

            PDPage page = new PDPage(new PDRectangle(width, height));
            doc.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                // contentStream.drawImage(ximage, 20, 20 );
                // better method inspired by http://stackoverflow.com/a/22318681/535646
                // reduce this value if the image is too large
                float scale = 1f;
                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
            }
            doc.save(outputFile);
        }
    }

    public static void imageToPDF(String imagePath, String pdfPath) throws IOException {
        if (!pdfPath.endsWith(".pdf")) {
            System.err.println("Last argument must be the destination .pdf file");
            System.exit(1);
        }

        try (PDDocument doc = new PDDocument()) {

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage,
            // call LosslessFactory.createFromImage() instead
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            int width = pdImage.getWidth();
            int height = pdImage.getHeight();

            PDPage page = new PDPage(new PDRectangle(width, height));
            doc.addPage(page);

            // draw the image at full size at (x=20, y=20)
            try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                // draw the image at full size at (x=20, y=20)
                contents.drawImage(pdImage, 0, 0, width, height);

                // to draw the image at half size at (x=20, y=20) use
                // contents.drawImage(pdImage, 20, 20, pdImage.getWidth() / 2, pdImage.getHeight() / 2);
            }
            doc.save(pdfPath);
        }
    }

    public static void imagesToPDF(String imagesPath, String PDFPath) throws IOException {
        try (PDDocument doc = new PDDocument()) {

            File files = new File(imagesPath);
            for (File file : Objects.requireNonNull(files.listFiles())) {
                String filepath = file.getPath();
                if (file.isDirectory()) {
                    imagesToPDF(filepath, PDFPath);
                    continue;
                }
                if (filepath.endsWith(".jpg") || filepath.endsWith(".png")) {
                    // createFromFile is the easiest way with an image file
                    // if you already have the image in a BufferedImage,
                    // call LosslessFactory.createFromImage() instead
                    PDImageXObject pdImage = PDImageXObject.createFromFile(filepath, doc);
                    int width = pdImage.getWidth();
                    int height = pdImage.getHeight();

                    PDPage page = new PDPage(new PDRectangle(width, height));
                    doc.addPage(page);

                    // draw the image at full size at (x=20, y=20)
                    try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                        // draw the image at full size at (x=20, y=20)
                        contents.drawImage(pdImage, 0, 0, width, height);

                        // to draw the image at half size at (x=20, y=20) use
                        // contents.drawImage(pdImage, 20, 20, pdImage.getWidth() / 2, pdImage.getHeight() / 2);
                    }
                }
            }
            if (doc.getNumberOfPages() != 0) {
                doc.save(PDFPath + File.separator + imagesPath.substring(imagesPath.lastIndexOf(File.separator)) + ".pdf");
            }
        }
    }
}
