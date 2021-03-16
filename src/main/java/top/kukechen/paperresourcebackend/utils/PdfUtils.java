package top.kukechen.paperresourcebackend.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfUtils {

    private static Logger logger = LoggerFactory.getLogger(PdfUtils.class);

    public static String getPdfFirstPage(File file) {
        try {
            InputStream is = null;
            PDDocument document = null;
            document = PDDocument.load(file);
            int pageSize = document.getNumberOfPages();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(1);
            String text = stripper.getText(document);
            return text;
        }catch (IOException e) {
            logger.error("PDF读取失败");
            e.printStackTrace();
            return "";
        }
    }

    public static int getPdfPageSize(File file) {
        try {
            InputStream is = null;
            PDDocument document = null;
            document = PDDocument.load(file);
            int pageSize = document.getNumberOfPages();
            return pageSize;
        }catch (IOException e) {
            logger.error("PDF读取失败");
            e.printStackTrace();
            return 0;
        }
    }
}
