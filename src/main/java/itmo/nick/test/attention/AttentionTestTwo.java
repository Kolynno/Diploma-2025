package itmo.nick.test.attention;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.stream.IntStream;

/**
 * Тест внимания 2 - Таблица Шульте
 *
 * @author Николай Жмакин
 * @since 02.02.2025
 */
@Getter
@Setter
@Component
public class AttentionTestTwo extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	static AttentionTestTwo attentionTestTwo;
	AttentionTestTwoData data = new AttentionTestTwoData();

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	private AttentionTestTwo() {
		super(LAST_STAGE);
	}

	public static AttentionTestTwo getInstance() {
		if (attentionTestTwo == null) {
			attentionTestTwo = new AttentionTestTwo();

			return attentionTestTwo;
		} else {
			return attentionTestTwo;
		}
	}

	/**
	 * Рассчет итогового результата (в секундах) в виде человеческого текста
	 * для отображения на этапе результата
	 * @param original оригинальное время тестов
	 * @return удобочитаемый текст результата
	 */
	@Override
	public String result(String[] original) {
		return "Среднее время - " + original[1] + " сек. \n" +
			"Ваше время - " + Math.round(data.getTime()) + " сек.";
	}

	public void setData(AttentionTestTwoData data) {
		this.data = data;
	}

	@Override
	public int getTestId() {
		return 6;
	}

	public void delete() {
		attentionTestTwo = null;
	}

	@Override
	public String getTestName() {
		return "Тест таблица Шульте";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1 - показатель 1: время в секундах на выполнение задания");
		strings.add("П1Л - показатель 1: лучшее значение времени выполнение задания в секундах");
		strings.add("П1Э - показатель 1: значение времени в секундах на выполнение задания в оригинальном тестировании");
		strings.add(BEST_AND_ORIGINAL_COMPARE_TEST_INFO);
		return strings;
	}

	@Override
	public LinkedList<String> getAllPersonData(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		testsCount = resultTableService.getTestCount(personId, getTestId());
		originalResults = getOriginalResults();
		otherBestResults = resultTableService.getOtherBest(getTestId());

		setTableAllTestsResult(personId, testsCount, strings, allResults);
		return strings;
	}

	private void setTableAllTestsResult(
		String personId,
		int testsCount,
		LinkedList<String> strings,
		LinkedList<Double> allResults)
	{
		LocalDate date = resultTableService.getTestDate(personId, 1, getTestId());
		for (int i = 1; i <= testsCount; i++) {
			strings.add(String.valueOf(i));
			strings.add(date.toString());
			LinkedList<Double> results = resultTableService.getResults(personId, date, getTestId());
			strings.add(String.valueOf(results.get(0))); //т.к. 1 показатель
			date = resultTableService.getTestDate(personId, i + 1, getTestId());
			allResults.add(results.get(0)); //1 показатель
		}
		double p1Avg = IntStream.range(0, testsCount).mapToDouble(i -> allResults.get(i * getParamsCount())).sum();
		p1Avg /= testsCount;

		strings.add("");
		strings.add("Среднее");
		strings.add(String.valueOf(p1Avg));
	}

	private LinkedList<Double> getOriginalResults() {
		String[] originalData = testTableService.getResultsById(getTestId()).split(";");
		LinkedList<Double> originalTests = new LinkedList<>();
		originalTests.add(Double.valueOf(originalData[0]));
		return originalTests;
	}

	@Override
	public LinkedList<String> getBestPersonDataAndCompareToOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		setTableBestAndOriginal(testsCount, allResults, strings, bestResults, originalResults);
		return strings;
	}

	private void setTableBestAndOriginal(
		int testsCount, LinkedList<Double> allResults,
		LinkedList<String> strings,
		LinkedList<Double> bestResults,
		LinkedList<Double> originalTests
	) {
		double p1Best = Integer.MAX_VALUE;
		for (int i = 0; i < testsCount; i++) {
			if (p1Best > allResults.get(i)) {
				p1Best = allResults.get(i);
			}
		}

		bestResults.add(p1Best);
		strings.add(String.valueOf(bestResults.get(0)));
		strings.add(String.valueOf(originalTests.get(0)));
	}

	@Override
	public int getParamsCount() {
		return 1;
	}
}
