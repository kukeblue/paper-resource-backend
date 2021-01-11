package top.kukechen.paperresourcebackend.units;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.kukechen.paperresourcebackend.controller.GradeController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfReader {

    private static Logger logger = LoggerFactory.getLogger(PdfReader.class);

    public static String getFirstPage(String path) {
        try {
            File file = new File(path);
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
        }catch (Error error) {

        } catch (IOException e) {
            logger.error("PDF读取失败");
            e.printStackTrace();

        } finally {
            return "";
        }
    }
}
