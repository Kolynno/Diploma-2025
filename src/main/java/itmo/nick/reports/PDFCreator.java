package itmo.nick.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import itmo.nick.database.PersonTableService;
import itmo.nick.database.ResultTableService;
import itmo.nick.test.SimpleTest;
import itmo.nick.test.attention.AttentionTestOne;
import itmo.nick.test.attention.AttentionTestTwo;
import itmo.nick.test.memory.MemoryTestOne;
import itmo.nick.test.memory.MemoryTestTwo;
import itmo.nick.test.processing.ProcessingTestOne;
import itmo.nick.test.processing.ProcessingTestTwo;
import itmo.nick.test.reaction.ReactionTestOne;
import itmo.nick.test.reaction.ReactionTestTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static itmo.nick.reports.PDFTextSettings.PDF_REPORT_PATH;


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
	private PersonTableService personTableService;
	@Autowired
	private ResultTableService resultTableService;

	/**
	 * Данные для отчета
	 */
	private PDFReportData pdfReportData;

	/**
	 * Создать файл отчета
	 * @param personId - идентификатор участника
	 */
	public void create(String personId) {
		try {
			pdfReportData = new PDFReportData();
			setup(personId);
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void setup(String personId) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(PDF_REPORT_PATH));
		document.open();

		firstPage(personId, document);
		testsPage(personId, document);

		document.close();
	}

	private void firstPage(String personId, Document document) throws DocumentException {
		document.add(new Paragraph("Подробный отчет", pdfTextSettings.mainHeaderFont()));
		document.add(new Paragraph("Участник: " + personTableService.getFio(personId), pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Дата рождения: " +  personTableService.getBirthday(personId), pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Пол: " +  personTableService.getSex(personId), pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Образование: " +  personTableService.getEducation(personId), pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Результаты тестов: с " + resultTableService.getFirstDateResult() + " по " + resultTableService.getLastDateResult(), pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Кратко", pdfTextSettings.mainTitleFont()));
		document.add(new Paragraph(" ", pdfTextSettings.mainTextFont()));
		addReportSummaryTable(document);
	}

	private void addReportSummaryTable(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		addTableHeader(table);
		addRows(table);
		document.add(table);
	}

	private void testsPage(String personId, Document document) throws DocumentException {
		for (int i = 1; i <= SimpleTest.TEST_AMOUNT; i++) {
			document.newPage();
			addTestInfo(document, i, personId);
		}
	}

	private void addTestInfo(Document document, int testId, String personId) throws DocumentException {
		SimpleTest test = getCurrentTest(testId);

		document.add(new Paragraph(test.getTestName(), pdfTextSettings.mainTitleFont()));
		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		for(String line : test.getAllPersonDataAndCompareToOther(personId)) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		for(String line : test.getBestPersonDataAndCompareToOriginal(personId)) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		for(String line : test.getPercentCompareToOtherAndOriginal(personId)) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		document.add(new Paragraph("Итог", pdfTextSettings.mainTitleFont()));
		for(String line : test.getSummary(personId)) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
	}

	private SimpleTest getCurrentTest(int testId) {
		return switch (testId) {
			case 1 -> AttentionTestOne.getInstance();
			case 2 -> AttentionTestTwo.getInstance();
			case 3 -> MemoryTestOne.getInstance();
			case 4 -> MemoryTestTwo.getInstance();
			case 5 -> ProcessingTestOne.getInstance();
			case 6 -> ProcessingTestTwo.getInstance();
			case 7 -> ReactionTestOne.getInstance();
			case 8 -> ReactionTestTwo.getInstance();
			default -> new SimpleTest(0);
		};
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

	private void addRows(PdfPTable table) {
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
