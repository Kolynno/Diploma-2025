package itmo.nick.reports;

import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Данные для итоговой страницы отчета
 *
 * @author Николай Жмакин
 * @since 29.03.2025
 */
@Component
public class PDFSummaryData {
	private LinkedList<String> summaryData = new LinkedList<>();

	public void addData(LinkedList<String> summary) {
		summaryData.addAll(summary);
	}

	public LinkedList<String> getSummaryData() {
		return summaryData;
	}
}
