package itmo.nick.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Path;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Отчет в формате PDF
 *
 * @author Николай Жмакин
 * @since 02.03.2025
 */
@Service
public class PDFCreator {

	@Autowired
	PDFTextSettings pdfTextSettings;

	public void create() {
		try {
			setup();
		} catch (FileNotFoundException | DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	private void setup() throws FileNotFoundException, DocumentException {
		Document document = new Document();

		PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
		document.addLanguage("CYRILLIC");

		document.open();


		PdfPTable table = new PdfPTable(4);
		addTableHeader(table);
		addRows(table);

		document.add(new Chunk("Подробный отчет", pdfTextSettings.mainHeaderFont()));

		document.add(new Chunk("Участник: ***", pdfTextSettings.mainTextFont()));
		document.add(new Chunk("Дата рождения: ***", pdfTextSettings.mainTextFont()));
		document.add(new Chunk("Пол: ***", pdfTextSettings.mainTextFont()));
		document.add(new Chunk("Образование: ***", pdfTextSettings.mainTextFont()));
		document.add(new Chunk("Результаты тестов: с *** по ***", pdfTextSettings.mainTextFont()));

		document.add(new Chunk("Кратко", pdfTextSettings.mainTitleFont()));

		document.add(table);

		document.close();
	}

	private void addTableHeader(PdfPTable table) {
		Stream.of("№ теста", "Название", "Отличие от эталонного, %", "Отличие от других участников, %")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle));
				table.addCell(header);
			});
	}

	private void addRows(PdfPTable table) {
		table.addCell("1");
		table.addCell("Тест Струпа");
		table.addCell("***");
		table.addCell("***");

		table.addCell("2");
		table.addCell("Тест Memtrax");
		table.addCell("***");
		table.addCell("***");

		table.addCell("3");
		table.addCell("Тест времени реакции");
		table.addCell("***");
		table.addCell("***");
	}
}
