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
	private PDFTextSettings pdfTextSettings;
	@Autowired
	private PersonTableService personTableService;
	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private PDFtestPageCreator PDFtestPageCreator;

	/**
	 * Создать файл отчета
	 * @param personId - идентификатор участника
	 */
	public void create(String personId) {
		try {
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
		lastPage(document);

		document.close();
	}

	private void firstPage(String personId, Document document) throws DocumentException {
		document.add(new Paragraph("Подробный отчет", pdfTextSettings.mainHeaderFont()));
		document.add(
			new Paragraph("Участник: " + personTableService.getFio(personId), pdfTextSettings.mainTextFont()))
		;
		document.add(
			new Paragraph(
				"Дата рождения: " + personTableService.getBirthday(personId),
				pdfTextSettings.mainTextFont()
			)
		);
		document.add(
			new Paragraph("Пол: " +  personTableService.getSex(personId), pdfTextSettings.mainTextFont())
		);
		document.add(
			new Paragraph(
				"Образование: " +  personTableService.getEducation(personId),
				pdfTextSettings.mainTextFont()
			)
		);
		document.add(
			new Paragraph(
				"Результаты тестов: с " + resultTableService.getFirstDateResult()
					+ " по " + resultTableService.getLastDateResult(),
				pdfTextSettings.mainTextFont()
			)
		);
	}

	private void lastPage(Document document) throws DocumentException {
		document.newPage();
		document.add(new Paragraph("Краткий итог", pdfTextSettings.mainTitleFont()));
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

		switch (test.getTestId()) {
			case 1 -> PDFtestPageCreator.stroopTestPage(document, test, personId);
			case 2 -> PDFtestPageCreator.memtraxTestPage(document, test, personId);
			case 3 -> PDFtestPageCreator.staticReactionTestPage(document, test, personId);
			case 4 -> PDFtestPageCreator.visualProcessingTestPage(document, test, personId);
			case 5 -> PDFtestPageCreator.audioProcessingTestPage(document, test, personId);
			case 6 -> PDFtestPageCreator.tableShulteTestPage(document, test, personId);
			case 7 -> PDFtestPageCreator.tenWordsTestPage(document, test, personId);
			case 8 -> PDFtestPageCreator.dynamicReactionTestPage(document, test, personId);
		}
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
		LinkedList<String> ato = AttentionTestOne.getInstance().getSummary();
		LinkedList<String> att = AttentionTestTwo.getInstance().getSummary();
		LinkedList<String> mto = MemoryTestOne.getInstance().getSummary();
		LinkedList<String> mtt = MemoryTestTwo.getInstance().getSummary();
		LinkedList<String> pto = ProcessingTestOne.getInstance().getSummary();
		LinkedList<String> ptt = ProcessingTestTwo.getInstance().getSummary();
		LinkedList<String> rto = ReactionTestOne.getInstance().getSummary();
		LinkedList<String> rtt = ReactionTestTwo.getInstance().getSummary();

		table.addCell(new Phrase("1", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест Струпа", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(ato.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(ato.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("2", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест Memtrax", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(mto.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(mto.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("3", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест времени реакции", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(rto.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(rto.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("4", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест устойчивости внимания: визуальный", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(pto.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(pto.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("5", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест устойчивости внимания: звуковой", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(ptt.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(ptt.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("6", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест таблица Шульте", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(att.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(att.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("7", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест заучивание 10 слов", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(mtt.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(mtt.get(1), pdfTextSettings.mainTextFont()));

		table.addCell(new Phrase("8", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase("Тест реакции на движение", pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(rtt.get(0), pdfTextSettings.mainTextFont()));
		table.addCell(new Phrase(rtt.get(1), pdfTextSettings.mainTextFont()));
	}
}
