package top.kukechen.paperresourcebackend;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@RestController
@EnableSwagger2
public class PaperResourceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaperResourceBackendApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) throws IOException {
		String path = "/Users/huanchen/Project/out/paper-resource-backend/2018-2019学年福建省厦门市江头片区六年级(下)期末数学试卷.pdf";
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
		return String.format(text.trim());
	}
}
