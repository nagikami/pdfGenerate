package com.nagi;

public class CreatePDF {
    public static void main(String[] args) {
        try {
            String imagesPath = "C:\\Downloads\\DLraw.net-Yusha Yamemasu vol 01-05";
            String PDFPath = "C:\\Downloads\\DLraw.net-Yusha Yamemasu vol 01-05\\pdfs";
            PDFUtils.imagesToPDF(imagesPath, PDFPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
