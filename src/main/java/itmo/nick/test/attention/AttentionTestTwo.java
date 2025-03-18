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

	/**
	 * Номер теста в системе
	 */
	public static final int TEST_NUMBER = 6;

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
		strings.add("Показатели со знаком процента (П*С%, П*Э%) - процент разницы между лучших показателем " +
			"участника (П*) и лучшего других участников и оригинального тестирования");
		return strings;
	}

	@Override
	public LinkedList<String> getAllPersonData(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		testsCount = resultTableService.getTestCount(personId, TEST_NUMBER);
		originalResults = getOriginalResults();
		otherBestResults = resultTableService.getOtherBest(TEST_NUMBER);

		setTableAllTestsResult(personId, testsCount, strings, allResults);
		return strings;
	}

	private void setTableAllTestsResult(
		String personId,
		int testsCount,
		LinkedList<String> strings,
		LinkedList<Double> allResults)
	{
		LocalDate date = resultTableService.getFirstDateResult();
		for (int i = 1; i <= testsCount; i++) {
			strings.add(String.valueOf(i));
			strings.add(date.toString());
			LinkedList<Double> results = resultTableService.getResults(personId, date);
			strings.add(String.valueOf(results.get(0))); //т.к. 1 показатель
			date = resultTableService.getTestDate(personId, i+ 1);
			allResults.add(results.get(0)); //1 показатель
		}
		double p1Avg = 0;
		for (int i = 0; i < testsCount; i++) {
			p1Avg += allResults.get(i*4);
		}
		p1Avg /= testsCount;

		strings.add("");
		strings.add("Среднее");
		strings.add(String.valueOf(p1Avg));
	}

	private LinkedList<Double> getOriginalResults() {
		String[] originalData = testTableService.getResultsById(TEST_NUMBER).split(";");
		LinkedList<Double> originalTests = new LinkedList<>();
		originalTests.add(Double.valueOf(originalData[0]));
		return originalTests;
	}

	@Override
	public LinkedList<String> getBestPersonDataAndCompareToOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("11");
		strings.add("22");
		strings.add("33");
		strings.add("44");
		strings.add("55");
		strings.add("66");
		strings.add("77");
		strings.add("88");
		return strings;
	}

	@Override
	public LinkedList<String> getPercentCompareToOtherAndOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("1%");
		strings.add("2%");
		strings.add("3%");
		strings.add("4%");
		strings.add("5%");
		strings.add("6%");
		strings.add("7%");
		strings.add("8%");
		return strings;
	}

	@Override
	public LinkedList<String> getSummary(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("Сравнение с другими участниками в среднем: + 111.3%");
		strings.add("Сравнение с эталонным результатом в среднем: -111.1%%");
		return strings;
	}

	@Override
	public int getParamsCount() {
		return 2;
	}
}
