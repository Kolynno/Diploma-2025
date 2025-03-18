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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
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
	@Autowired
	private ApplicationContext applicationContext;

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
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		addReportAllDataSummaryTable(document, test.getAllPersonData(personId), test);
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		addReportBestDataTable(document, test.getBestPersonDataAndCompareToOriginal(personId));
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		addReportCompareDataTable(document, test.getPercentCompareToOtherAndOriginal(personId));
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		document.add(new Paragraph("Итог", pdfTextSettings.mainTitleFont()));
		for(String line : test.getSummary(personId)) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
	}

	private void addReportCompareDataTable(Document document, LinkedList<String> allCompareData) throws DocumentException {
		PdfPTable table = new PdfPTable(8);
		Stream.of("П1C%", "П1Э%", "П2C%", "П2Э%", "П3C%", "П3Э%","П4C%","П4Э%")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				table.addCell(header);
			});
		if (!allCompareData.isEmpty()) {
			table.addCell(new Phrase(allCompareData.get(0), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(1), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(2), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(3), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(4), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(5), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(6), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allCompareData.get(7), pdfTextSettings.mainTextFont()));
		}
		document.add(table);
	}

	private void addReportBestDataTable(Document document, LinkedList<String> allBestData) throws DocumentException {
		PdfPTable table = new PdfPTable(8);
		Stream.of("П1Л", "П1Э", "П2Л", "П2Э", "П3Л", "П3Э","П4Л","П4Э")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				table.addCell(header);
			});
		if (!allBestData.isEmpty()) {
			table.addCell(new Phrase(allBestData.get(0), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(1), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(2), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(3), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(4), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(5), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(6), pdfTextSettings.mainTextFont()));
			table.addCell(new Phrase(allBestData.get(7), pdfTextSettings.mainTextFont()));
		}
		document.add(table);
	}


	private void addReportAllDataSummaryTable(Document document, LinkedList<String> allPersonData, SimpleTest test) throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		Stream.of("№", "Дата", "П1", "П2", "П3", "П4")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				table.addCell(header);
			});
		int cols = test.getParamsCount() + 2;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i <= allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				table.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				table.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				table.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
				if (cols < 5) {
					return;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 3), pdfTextSettings.mainTextFont())); // P2
				if (cols < 6) {
					return;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 4), pdfTextSettings.mainTextFont())); // P3
				if (cols < 7) {
					return;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 5), pdfTextSettings.mainTextFont())); // P4
				if (cols < 8) {
					return;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 6), pdfTextSettings.mainTextFont())); // P5
			}
		}
		document.add(table);
	}

	private SimpleTest getCurrentTest(int testId) {
		return switch (testId) {
			case 1 -> applicationContext.getBean(AttentionTestOne.class);
			case 2 -> applicationContext.getBean(AttentionTestTwo.class);
			case 3 -> applicationContext.getBean(MemoryTestOne.class);
			case 4 -> applicationContext.getBean(MemoryTestTwo.class);
			case 5 -> applicationContext.getBean(ProcessingTestOne.class);
			case 6 -> applicationContext.getBean(ProcessingTestTwo.class);
			case 7 -> applicationContext.getBean(ReactionTestOne.class);
			case 8 -> applicationContext.getBean(ReactionTestTwo.class);
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
