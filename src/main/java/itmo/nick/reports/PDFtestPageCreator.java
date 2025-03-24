package itmo.nick.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import itmo.nick.test.SimpleTest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Данные для отчета PDF
 *
 * @author Николай Жмакин
 * @since 13.03.2025
 */
@Service
public class PDFtestPageCreator {

	private final PDFTextSettings pdfTextSettings;

	public PDFtestPageCreator(PDFTextSettings pdfTextSettings) {
		this.pdfTextSettings = pdfTextSettings;
	}

	public void stroopTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		document.add(new Paragraph(test.getTestName(), pdfTextSettings.mainTitleFont()));
		addEmptyLine(document);

		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		addEmptyLine(document);

		LinkedList<String> allPersonData = test.getAllPersonData(personId);
		PdfPTable AllDataTable = new PdfPTable(6);
		Stream.of("№", "Дата", "П1", "П2", "П3", "П4")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				AllDataTable.addCell(header);
			});
		int cols = test.getParamsCount() + 2;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i < allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 3), pdfTextSettings.mainTextFont())); // P2
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 4), pdfTextSettings.mainTextFont())); // P3
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 5), pdfTextSettings.mainTextFont())); // P4
			}
		}
		document.add(AllDataTable);
		addEmptyLine(document);

		LinkedList<String> allBestData = test.getBestPersonDataAndCompareToOriginal(personId);
		PdfPTable tableBest = new PdfPTable(8);
		Stream.of("П1Л", "П1Э", "П2Л", "П2Э", "П3Л", "П3Э","П4Л","П4Э")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableBest.addCell(header);
			});
		if (!allBestData.isEmpty()) {
			tableBest.addCell(new Phrase(allBestData.get(0), pdfTextSettings.mainTextFont())); // П1Л
			tableBest.addCell(new Phrase(allBestData.get(1), pdfTextSettings.mainTextFont())); // П1Э
			tableBest.addCell(new Phrase(allBestData.get(2), pdfTextSettings.mainTextFont())); // П2Л
			tableBest.addCell(new Phrase(allBestData.get(3), pdfTextSettings.mainTextFont())); // П2Э
			tableBest.addCell(new Phrase(allBestData.get(4), pdfTextSettings.mainTextFont())); // П3Л
			tableBest.addCell(new Phrase(allBestData.get(5), pdfTextSettings.mainTextFont())); // П3Э
			tableBest.addCell(new Phrase(allBestData.get(6), pdfTextSettings.mainTextFont())); // П4Л
			tableBest.addCell(new Phrase(allBestData.get(7), pdfTextSettings.mainTextFont())); // П4Э
		}
		document.add(tableBest);
		addEmptyLine(document);

		LinkedList<String> allCompareData = test.getPercentCompareToOtherAndOriginal(personId);
		PdfPTable tableCompare = new PdfPTable(8);
		Stream.of("П1C%", "П1Э%", "П2C%", "П2Э%", "П3C%", "П3Э%","П4C%","П4Э%")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableCompare.addCell(header);
			});
		if (!allCompareData.isEmpty()) {
			tableCompare.addCell(new Phrase(allCompareData.get(0), pdfTextSettings.mainTextFont())); // П1C%
			tableCompare.addCell(new Phrase(allCompareData.get(1), pdfTextSettings.mainTextFont())); // П1Э%
			tableCompare.addCell(new Phrase(allCompareData.get(2), pdfTextSettings.mainTextFont())); // П2C%
			tableCompare.addCell(new Phrase(allCompareData.get(3), pdfTextSettings.mainTextFont())); // П2Э%
			tableCompare.addCell(new Phrase(allCompareData.get(4), pdfTextSettings.mainTextFont())); // П3C%
			tableCompare.addCell(new Phrase(allCompareData.get(5), pdfTextSettings.mainTextFont())); // П3Э%
			tableCompare.addCell(new Phrase(allCompareData.get(6), pdfTextSettings.mainTextFont())); // П4C%
			tableCompare.addCell(new Phrase(allCompareData.get(7), pdfTextSettings.mainTextFont())); // П4Э%
		}
		document.add(tableCompare);

		addEmptyLine(document);
		document.add(new Paragraph("Итог", pdfTextSettings.mainTitleFont()));

		LinkedList<String> summary = test.getSummary(personId);
		document.add(new Paragraph("Разность относительно других участников: " + summary.get(0) + "%", pdfTextSettings.mainTextFont()));
		document.add(new Paragraph("Разность относительно оригинальных результатов: " + summary.get(1) + "%", pdfTextSettings.mainTextFont()));

	}

	public void memtraxTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		document.add(new Paragraph(test.getTestName(), pdfTextSettings.mainTitleFont()));
		addEmptyLine(document);

		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));

		LinkedList<String> allPersonData = test.getAllPersonData(personId);
		PdfPTable AllDataTable = new PdfPTable(4);
		Stream.of("№", "Дата", "П1", "П2")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				AllDataTable.addCell(header);
			});
		int cols = test.getParamsCount() + 2;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i < allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 3), pdfTextSettings.mainTextFont())); // P2
			}
		}
		document.add(AllDataTable);
		addEmptyLine(document);

		LinkedList<String> allBestData = test.getBestPersonDataAndCompareToOriginal(personId);
		PdfPTable tableBest = new PdfPTable(4);
		Stream.of("П1Л", "П1Э", "П2Л", "П2Э")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableBest.addCell(header);
			});
		if (!allBestData.isEmpty()) {
			tableBest.addCell(new Phrase(allBestData.get(0), pdfTextSettings.mainTextFont())); //П1Л
			tableBest.addCell(new Phrase(allBestData.get(1), pdfTextSettings.mainTextFont())); //П1Э
			tableBest.addCell(new Phrase(allBestData.get(2), pdfTextSettings.mainTextFont())); //П2Э
			tableBest.addCell(new Phrase(allBestData.get(3), pdfTextSettings.mainTextFont())); //П2Э
		}
		document.add(tableBest);
	}

	public void staticReactionTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		setPageWith3Params(document, test, personId);
	}

	public void visualProcessingTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		setPageWith3Params(document, test, personId);
	}

	public void audioProcessingTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		setPageWith3Params(document, test, personId);
	}

	public void tableShulteTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		document.add(new Paragraph(test.getTestName(), pdfTextSettings.mainTitleFont()));
		addEmptyLine(document);

		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));

		LinkedList<String> allPersonData = test.getAllPersonData(personId);
		PdfPTable AllDataTable = new PdfPTable(3);
		Stream.of("№", "Дата", "П1")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				AllDataTable.addCell(header);
			});
		int cols = test.getParamsCount() + 2;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i < allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
			}
		}
		document.add(AllDataTable);
		addEmptyLine(document);

		LinkedList<String> allBestData = test.getBestPersonDataAndCompareToOriginal(personId);
		PdfPTable tableBest = new PdfPTable(2);
		Stream.of("П1Л", "П1Э")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableBest.addCell(header);
			});
		if (!allBestData.isEmpty()) {
			tableBest.addCell(new Phrase(allBestData.get(0), pdfTextSettings.mainTextFont())); //П1Л
			tableBest.addCell(new Phrase(allBestData.get(1), pdfTextSettings.mainTextFont())); //П1Э
		}
		document.add(tableBest);

		/*
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		LinkedList<String> allCompareData = test.getPercentCompareToOtherAndOriginal(personId);
		PdfPTable tableCompare = new PdfPTable(2);
		Stream.of("П1C%", "П1Э%")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableCompare.addCell(header);
			});
		if (!allCompareData.isEmpty()) {
			tableCompare.addCell(new Phrase(allCompareData.get(0), pdfTextSettings.mainTextFont()));
			tableCompare.addCell(new Phrase(allCompareData.get(1), pdfTextSettings.mainTextFont()));
		}
		document.add(tableCompare);


		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
		document.add(new Paragraph("Итог", pdfTextSettings.mainTitleFont()));
		for(String line : test.getSummary(personId)) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		*/

	}

	public void tenWordsTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		setPageWith5Params(document, test, personId);
	}

	public void dynamicReactionTestPage(Document document, SimpleTest test, String personId) throws DocumentException {
		setPageWith5Params(document, test, personId);
	}


	private void addReportAllDataSummaryTable(Document document, LinkedList<String> allPersonData, SimpleTest test) throws DocumentException {
		PdfPTable table = new PdfPTable(7);
		Stream.of("№", "Дата", "П1", "П2", "П3", "П4", "П5")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				table.addCell(header);
			});
		int cols = test.getParamsCount() + 3;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i <= allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				table.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				table.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				table.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
				if (cols < 6) {
					continue;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 3), pdfTextSettings.mainTextFont())); // P2
				if (cols < 7) {
					continue;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 4), pdfTextSettings.mainTextFont())); // P3
				if (cols < 8) {
					continue;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 5), pdfTextSettings.mainTextFont())); // P4
				if (cols < 9) {
					continue;
				}
				table.addCell(new Phrase(allPersonData.get(pointer + 6), pdfTextSettings.mainTextFont())); // P5
			}
		}
		document.add(table);
	}

	private void addEmptyLine(Document document) throws DocumentException {
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));
	}

	private void setPageWith3Params(Document document, SimpleTest test, String personId) throws DocumentException {
		document.add(new Paragraph(test.getTestName(), pdfTextSettings.mainTitleFont()));
		addEmptyLine(document);

		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		document.add(new Paragraph(" ", pdfTextSettings.mainTitleFont()));

		LinkedList<String> allPersonData = test.getAllPersonData(personId);
		PdfPTable AllDataTable = new PdfPTable(5);
		Stream.of("№", "Дата", "П1", "П2", "П3")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				AllDataTable.addCell(header);
			});
		int cols = test.getParamsCount() + 2;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i < allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 3), pdfTextSettings.mainTextFont())); // P2
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 4), pdfTextSettings.mainTextFont())); // P3
			}
		}
		document.add(AllDataTable);
		addEmptyLine(document);

		LinkedList<String> allBestData = test.getBestPersonDataAndCompareToOriginal(personId);
		PdfPTable tableBest = new PdfPTable(6);
		Stream.of("П1Л", "П1Э", "П2Л", "П2Э", "П3Л", "П3Э")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableBest.addCell(header);
			});
		if (!allBestData.isEmpty()) {
			tableBest.addCell(new Phrase(allBestData.get(0), pdfTextSettings.mainTextFont())); //П1Л
			tableBest.addCell(new Phrase(allBestData.get(1), pdfTextSettings.mainTextFont())); //П1Э
			tableBest.addCell(new Phrase(allBestData.get(2), pdfTextSettings.mainTextFont())); //П2Э
			tableBest.addCell(new Phrase(allBestData.get(3), pdfTextSettings.mainTextFont())); //П2Э
			tableBest.addCell(new Phrase(allBestData.get(4), pdfTextSettings.mainTextFont())); //П3Э
			tableBest.addCell(new Phrase(allBestData.get(5), pdfTextSettings.mainTextFont())); //П3Э
		}
		document.add(tableBest);
	}

	private void setPageWith5Params(Document document, SimpleTest test, String personId) throws DocumentException {
		document.add(new Paragraph(test.getTestName(), pdfTextSettings.mainTitleFont()));
		addEmptyLine(document);

		for(String line : test.getTestInfo()) {
			document.add(new Paragraph(line, pdfTextSettings.mainTextFont()));
		}
		addEmptyLine(document);

		LinkedList<String> allPersonData = test.getAllPersonData(personId);
		PdfPTable AllDataTable = new PdfPTable(7);
		Stream.of("№", "Дата", "П1", "П2", "П3", "П4", "П5")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				AllDataTable.addCell(header);
			});
		int cols = test.getParamsCount() + 2;
		if (!allPersonData.isEmpty()) {
			for (int i = 0; i < allPersonData.size() / cols; i++) {
				int pointer = i * cols;
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer), pdfTextSettings.mainTextFont())); // №
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 1), pdfTextSettings.mainTextFont())); // Date
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 2), pdfTextSettings.mainTextFont())); // P1
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 3), pdfTextSettings.mainTextFont())); // P2
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 4), pdfTextSettings.mainTextFont())); // P3
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 5), pdfTextSettings.mainTextFont())); // P4
				AllDataTable.addCell(new Phrase(allPersonData.get(pointer + 6), pdfTextSettings.mainTextFont())); // P5
			}
		}
		document.add(AllDataTable);
		addEmptyLine(document);

		LinkedList<String> allBestData = test.getBestPersonDataAndCompareToOriginal(personId);
		PdfPTable tableBest = new PdfPTable(10);
		Stream.of("П1Л", "П1Э", "П2Л", "П2Э", "П3Л", "П3Э","П4Л", "П4Э", "П5Л", "П5Э")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(columnTitle, pdfTextSettings.mainTextFont()));
				tableBest.addCell(header);
			});
		if (!allBestData.isEmpty()) {
			tableBest.addCell(new Phrase(allBestData.get(0), pdfTextSettings.mainTextFont())); // П1Л
			tableBest.addCell(new Phrase(allBestData.get(1), pdfTextSettings.mainTextFont())); // П1Э
			tableBest.addCell(new Phrase(allBestData.get(2), pdfTextSettings.mainTextFont())); // П2Л
			tableBest.addCell(new Phrase(allBestData.get(3), pdfTextSettings.mainTextFont())); // П2Э
			tableBest.addCell(new Phrase(allBestData.get(4), pdfTextSettings.mainTextFont())); // П3Л
			tableBest.addCell(new Phrase(allBestData.get(5), pdfTextSettings.mainTextFont())); // П3Э
			tableBest.addCell(new Phrase(allBestData.get(6), pdfTextSettings.mainTextFont())); // П4Л
			tableBest.addCell(new Phrase(allBestData.get(7), pdfTextSettings.mainTextFont())); // П4Э
			tableBest.addCell(new Phrase(allBestData.get(8), pdfTextSettings.mainTextFont())); // П5Л
			tableBest.addCell(new Phrase(allBestData.get(9), pdfTextSettings.mainTextFont())); // П5Э
		}
		document.add(tableBest);
	}

}
