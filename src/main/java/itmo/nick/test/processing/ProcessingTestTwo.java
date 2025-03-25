package itmo.nick.test.processing;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Тест на обработку информации 2 - звуковой
 *
 * @author Николай Жмакин
 * @since 14.01.2025
 */
@Component
public class ProcessingTestTwo extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	private final static String GOAL = "1";
	private final static String NOT_GOAL = "0";

	static ProcessingTestTwo processingTestOne;

	private ArrayList<ProcessingTestTwoData> dataList;

	private int currentTone = 0;
	/**
	 * 1 - нажатие на пробел (целевой звук)
	 * 0 - ошибка (нецелевой звук)
	 */
	private String[] toneSequence;
	private int goalTones;

	private int skipErrors = 0;
	private int simpleErrors = 0;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestTwo() {
		super(LAST_STAGE);
		toneSequence = new String[] {GOAL, GOAL, GOAL, NOT_GOAL, NOT_GOAL, GOAL, GOAL, GOAL, NOT_GOAL};
		goalTones = (int) Arrays.stream(toneSequence).filter(tone -> tone.equals(GOAL)).count();
		dataList = new ArrayList<>();
	}

	public static ProcessingTestTwo getInstance() {
		if (processingTestOne == null) {
			processingTestOne = new ProcessingTestTwo();
			return processingTestOne;
		} else {
			return processingTestOne;
		}
	}

	@Override
	public String result(String[] original) {
		calculateErrors();
		return "Ошибки в оригинальном тесте у здоровых людей: " + original[0]
			+ "% пропусков целевого звука, " + original[1] + "% долгой реакции на целевой звук. Время реакции " + original[2] + " мс.<br>" +
			"Ошибки в оригинальном тесте у людей с шизофренией: " + original[3]
			+ "% пропусков целевого звука, " + original[4] + "% долгой реакции на целевой звук. Время реакции " + original[5] + " мс.<br>" +
			"Ваш результат: " + getSkipErrors() + "% пропусков целевого звука, " + getErrors()
			+ "% долгой реакции на целевой звук. Время реакции " + getReactionTime() + " мс.";
	}

	/**
	 * Подсчет ошибок в тесте
	 */
	private void calculateErrors() {
		for (int i = 0; i < dataList.size(); i++) {
			String correctAnswer = toneSequence[i];
			double answerTime = dataList.get(i).getReactionTime();
			if (correctAnswer.equals(GOAL)) {
				//целевой
				if (answerTime < 600) {
					//успел
				} else if (answerTime >= 600 && answerTime < 1900){
					//опоздание ответа
					skipErrors++;
				} else if (answerTime >= 1900) {
					//пропустил целевой
					simpleErrors++;
					dataList.get(i).setReactionTime(0);
					//то убрать время реакций на пропуск
				}
			} else {
				//нецелевой
				if (answerTime < 2000) {
					//и был ответ
					dataList.get(i).setReactionTime(0);
					//то убрать время реакций на неверный
				}
			}
		}

	}

	/**
	 * Время реакции в мс
	 */
	public String getReactionTime() {
		Double value = dataList
			.stream()
			.mapToDouble(ProcessingTestTwoData::getReactionTime)
			.filter(time -> time > 0)
			.average()
			.getAsDouble();
		return value == null ? "0" : String.valueOf(Math.round(value));
	}

	/**
	 * Пропуск целевого
	 */
	public String getErrors() {
		return String.valueOf(Math.round((double) simpleErrors/(goalTones)*100.0));
	}

	/**
	 * Долгий ответ на целевой
	 */
	public String getSkipErrors() {
		return String.valueOf(Math.round((double) skipErrors /(goalTones)*100.0));
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextTone() {
		if (currentTone < toneSequence.length) {
			int index= currentTone;
			currentTone++;
			return toneSequence[index];
		}
		return "-1";
	}

	public void addData(ProcessingTestTwoData data) {
		dataList.add(data);
	}

	@Override
	public String getTestName() {
		return "Тест обработки информации (аудио)";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1, П2, П3 – показатели теста. % пропусков целевого звука, % долгой реакции на целевой звук " +
			"и время реакции");
		strings.add(BEST_AND_ORIGINAL_COMPARE_TEST_INFO);
		strings.add("П1Л, П2Л, П3Л – лучшее значение. Минимальный % пропусков целевого звука, % долгой реакции " +
			"и времени реации");
		strings.add("П1Э, П2Э, П3Э – " +
			"значение показателей оригинально теста. Минимальный % пропусков целевого звука, % долгой реакции " +
			"и времени реации");
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
	public int getParamsCount() {
		return 3;
	}

	@Override
	public int getTestId() {
		return 5;
	}
}
