package itmo.nick.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
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
	@Autowired
	private TestTableService testTableService;

	public void create() {
		try {
			setup();
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void setup() throws IOException, DocumentException {
		Document document = new Document();

		PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
		document.open();


		PdfPTable table = new PdfPTable(4);
		addTableHeader(table);
		addRows(table);

		document.add(new Paragraph("Подробный отчет", pdfTextSettings.mainHeaderFont()));

		document.add(new Paragraph("Участник: ***", pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Дата рождения: ***", pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Пол: ***", pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Образование: ***", pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Результаты тестов: с *** по ***", pdfTextSettings.mainTextFont()));

		document.add(new Paragraph("Кратко", pdfTextSettings.mainTitleFont()));
		document.add(new Paragraph(" ", pdfTextSettings.mainTextFont()));

		document.add(table);

		for (int i = 0; i < SimpleTest.TEST_AMOUNT; i++) {
			document.newPage();
			addTestInfo(document, i);
		}

		document.close();
	}

	private void addTestInfo(Document document, int testId) throws DocumentException {
		document.add(new Paragraph(testTableService.getNameById(testId), pdfTextSettings.mainTitleFont()));

		//а далее тут для каждого теста свои данные

	}

	private void addTableHeader(PdfPTable table) {
		Stream.of("№ теста", "Название", "Отличие от эталонного, %", "Отличие от других участников, %")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				table.addCell(header);
			});
	}

	private void addRows(PdfPTable table) throws DocumentException, IOException {
		table.addCell(new Phrase("1", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест Струпа", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("***", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("***", pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("2", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест Memtrax", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("***", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("***", pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("3", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест времени реакции", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("***", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("***", pdfTextSettings.mainTextFont()));

		//и остальные тесты
	}
}
