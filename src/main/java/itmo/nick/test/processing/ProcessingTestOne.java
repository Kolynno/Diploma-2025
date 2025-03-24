package itmo.nick.test.processing;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

/**
 * Тест на обработку информации 1 - визуальный
 *
 * @author Николай Жмакин
 * @since 03.01.2025
 */
@Component
public class ProcessingTestOne extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	static ProcessingTestOne processingTestOne;

	private ArrayList<ProcessingTestOneData> dataList;

	private int currentNumbers = 0;
	private String[] numbersSequence;
	/**
	 * 0  - простой
	 * 1  - нажатие на пробел (одинаковые числа)
	 * -1 - ловушка (только 1 цифра отличается)
	 */
	private String[] correctAnswerSequence;

	private int errors = 0;
	private int simpleErrors = 0;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestOne() {
		super(LAST_STAGE);
		numbersSequence = new String[] {"11111", "11111", "11112", "11112", "22222", "22222", "22222", "12313", "23131", "11232"};
		correctAnswerSequence = new String[] {"0", "1", "-1", "1", "0", "1", "1", "0", "0", "0"};
		dataList = new ArrayList<>();
	}

	public static ProcessingTestOne getInstance() {
		if (processingTestOne == null) {
			processingTestOne = new ProcessingTestOne();

			return processingTestOne;
		} else {
			return processingTestOne;
		}
	}

	@Override
	public String result(String[] original) {
		calculateErrors();
		return "Ошибки в оригинальном тесте у здоровых людей: " + original[0]
			+ "% ловушка, " + original[1] + "% простой. Время реакции " + original[2] + " мс.<br>" +
			"Ошибки в оригинальном тесте у людей с шизофренией: " + original[3]
			+ "% ловушка, " + original[4] + "% простой. Время реакции " + original[5] + " мс.<br>" +
			"Ваш результат: " + getErrors() + " % ловушка, " + getSimpleErrors()
			+ " % простой. Время реакции " + getReactionTime() + " мс.";
	}

	/**
	 * Подсчет ошибок в тесте
	 */
	private void calculateErrors() {
		for(int i = 1; i < dataList.size(); i++) {
			String correctAnswer = correctAnswerSequence[i];
			String userAnswer = dataList.get(i).getReactionTime() < 1000 ? "1" : "0";
			if ("1".equals(userAnswer)) {
				if ("0".equals(correctAnswer)) {
					simpleErrors++;
				} else if ("-1".equals(correctAnswer)) {
					errors++;
				}
			} else {
				//как я понял, если действия не было, но надо было, то это новый тип ошибок "Пропуск",
				//но он не учитывается при плсдчете реакции
			}
		}
	}

	/**
	 * Время реакции в мс
	 */
	public String getReactionTime() {
		Double value = dataList.stream().mapToDouble(ProcessingTestOneData::getReactionTime).average().getAsDouble();
		return value == null ? "0" : String.valueOf(Math.round(value));
	}

	/**
	 * Кол-во простых ошибок, процент без знака процента
	 */
	public String getSimpleErrors() {
		return String.valueOf(Math.round((double) simpleErrors/(dataList.size() - 1)*100.0));
	}

	/**
	 * Кол-во ошибок, процент без знака процента
	 */
	public String getErrors() {
		return String.valueOf(Math.round((double) errors/(dataList.size() - 1)*100.0));
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextNumbers() {
		//Первый не участвует в сравнении, т.к. нулевая позиция - пустой.
		if (currentNumbers < numbersSequence.length - 1) {
			return numbersSequence[currentNumbers++];
		}

		return "-1";
	}

	public void addData(ProcessingTestOneData data) {
		dataList.add(data);
	}

	@Override
	public String getTestName() {
		return "Тест обработки информации (визуальный)";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1, П2, П3 – показатели теста. Кол-во ошибок, % ловушек и % простоя");
//		strings.add("П1С,П2С, П3С, П4С – " +
//			"значение в секундах показателей у других участников на каждый этап соответственно");
		strings.add("П1Л, П2Л, П3Л – лучшее значение. Минимальное кол-во ошибок, % ловушек и % простоя");
		strings.add("П1Э, П2Э, П3Э – " +
			"значение показателей оригинально теста. Кол-во ошибок, % ловушек и % простоя");
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

	private LinkedList<Double> getOriginalResults() {
		String[] originalData = testTableService.getResultsById(getTestId()).split(";");
		LinkedList<Double> originalTests = new LinkedList<>();
		originalTests.add(Double.valueOf(originalData[0]));
		originalTests.add(Double.valueOf(originalData[1]));
		originalTests.add(Double.valueOf(originalData[2]));
		return originalTests;
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
			addAll(strings, results);
			date = resultTableService.getTestDate(personId, i + 1, getTestId());
			addAllToResults(allResults, results);
		}

		double p1Avg = 0;
		double p2Avg = 0;
		double p3Avg = 0;
		for (int i = 0; i < testsCount; i++) {
			p1Avg += allResults.get(i * getParamsCount());
			p2Avg += allResults.get(i * getParamsCount() + 1);
			p3Avg += allResults.get(i * getParamsCount() + 2);
		}
		p1Avg /= testsCount;
		p2Avg /= testsCount;
		p3Avg /= testsCount;

		strings.add("");
		strings.add("Среднее");
		strings.add(String.valueOf(p1Avg));
		strings.add(String.valueOf(p2Avg));
		strings.add(String.valueOf(p3Avg));
	}

	private void addAllToResults(LinkedList<Double> allResults, LinkedList<Double> results) {
		if (results.get(0) != null) {
			allResults.add(results.get(0));
		}
		if (results.get(1) != null) {
			allResults.add(results.get(1));
		}
		if (results.get(2) != null) {
			allResults.add(results.get(2));
		}
	}

	private void addAll(LinkedList<String> strings, LinkedList<Double> results) {
		if (results.get(0) != null) {
			strings.add(String.valueOf(results.get(0)));
		}
		if (results.get(1) != null) {
			strings.add(String.valueOf(results.get(1)));
		}
		if (results.get(2) != null) {
			strings.add(String.valueOf(results.get(2)));
		}
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
		double p2Best = Integer.MAX_VALUE;
		double p3Best = Integer.MAX_VALUE;
		for (int i = 0; i < testsCount; i++) {
			if (p1Best > allResults.get(i)) {
				p1Best = allResults.get(i);
			}
			if (p2Best > allResults.get(i + 1)) {
				p2Best = allResults.get(i + 1);
			}
			if (p3Best > allResults.get(i + 2)) {
				p3Best = allResults.get(i + 2);
			}
		}

		bestResults.add(p1Best);
		bestResults.add(p2Best);
		bestResults.add(p3Best);

		for (int i = 0; i < originalTests.size(); i++) {
			strings.add(String.valueOf(bestResults.get(i)));
			strings.add(String.valueOf(originalTests.get(i)));
		}
	}

	@Override
	public LinkedList<String> getPercentCompareToOtherAndOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		setTablePercentCompare(originalResults, bestResults, otherBestResults, strings);
		return strings;
	}

	private void setTablePercentCompare(
		LinkedList<Double> originalResults,
		LinkedList<Double> bestResults,
		LinkedList<Double> otherBestResults,
		LinkedList<String> strings
	) {
		double otherPercentAvg = 0;
		double originalPercentAvg = 0;
		for (int i = 0; i < originalResults.size(); i++) {
			double percentOther = Math.round((bestResults.get(i) / otherBestResults.get(i) - 1) * 100) ;
			double percentOriginal = Math.round((bestResults.get(i) / originalResults.get(i) - 1) * 100);
			otherPercentAvg += percentOther;
			originalPercentAvg += percentOriginal;
			strings.add(String.valueOf(percentOther));
			strings.add(String.valueOf(percentOriginal));
		}

		otherPercentAvg /= originalResults.size();
		originalPercentAvg /= originalResults.size();

		strings.add(String.valueOf(otherPercentAvg));
		strings.add(String.valueOf(originalPercentAvg));
	}

	@Override
	public LinkedList<String> getSummary(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		return strings;
	}

	@Override
	public int getParamsCount() {
		return 3;
	}

	@Override
	public int getTestId() {
		return 4;
	}
}
