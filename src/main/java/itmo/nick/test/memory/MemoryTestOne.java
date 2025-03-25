package itmo.nick.test.memory;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;

/**
 * Memtrax - тест на память 1
 *
 * @author Николай Жмакин
 * @since 22.12.2024
 */
@Component
public class MemoryTestOne extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	static MemoryTestOne memoryTestOne;
	/**
	 * Номер последнего этап теста
	 */
	public static final int LAST_STAGE = 2;

	/**
	 * Время таймаута реакции
	 */
	public static final double TIMEOUT_IN_SEC = 3.0;

	private final String[] pictureSequence;
	private final String[] correctAnswers;

	private int incorrectAnswers;
	private double answerTime;

	private int currentPict;


	protected MemoryTestOne() {
		super(LAST_STAGE);
		pictureSequence = getSequence();
		correctAnswers = getCorrectAnswers();
	}

	public static MemoryTestOne getInstance() {
		if (memoryTestOne == null) {
			memoryTestOne = new MemoryTestOne();

			return memoryTestOne;
		} else {
			return memoryTestOne;
		}
	}

	public void addData(MemoryTestOneData data) {
		if (data.getReactionTime() > TIMEOUT_IN_SEC) {
			memoryTestOne.answer("0", 0);
		} else {
			//только время реации распознавания
			memoryTestOne.answer("1", data.getReactionTime());
		}
	}

	@Override
	public String result(String[] original) {
		return "Обычные люди: " + original[0] + "% ошибок и до " + original[1] + " сек. в среднем скорость ответа<br>" +
			"Легкое когнитивное нарушение: " + original[2] + "% ошибок и до " + original[3] + " сек. в среднем скорость ответа<br>" +
			"Легкая степень деменции: " + original[4] + "% ошибок и до " + original[5] + " сек. в среднем скорость ответа<br>" +
			"Средняя и тяжка деменция: от " + original[4] + "% ошибок и от " + original[5] + " сек. в среднем скорость ответа<br>" +
			"Ваш результат: " + getErrorPercent() + "% ошибок,<br>" +
			getAnswerMs() + " мс. среднее время реакции на повторную картинку";
	}

	public long getAnswerMs() {
		return Math.round(answerTime / 25.0 * 1000);
	}

	public double getErrorPercent() {
		return Math.round(incorrectAnswers / 52.0 * 1000) / 10.0;
	}

	private void answer(String type, double time) {
		if (!getCorrectAnswer().equals(type)) {
			increaseIncorrectAnswers();
		}
		increaseTime(time);
	}

	public void delete() {
		memoryTestOne = null;
	}

	private String[] getCorrectAnswers() {
		return new String[] {"0","0","0","0","0","0","0","0","0","0","0","0","1","1","0","0","0","0","0","0","0","0", "0",
			"1","1","0","1","1","1","1","0","1","0","1","1","1","1","1","1","0","1","1","1","1","1","1","1","1","1","1"};
	}

	//Пока одна и та же последовательность, а не случайная
	private String[] getSequence() {
		return new String[] {"25","1","19","5","15","12","2","6","20","3","23","4","5","6","14","7","8","17","9","24",
			"10","21","11","10","12","13","2","14","15","9","16","17","18","19","20","4","21","11","3","22","23","16",
			"24","1","25","7","8","13","18","22"};
	}

	/**
	 * Получить номер картинки
	 */
	public String getNextPicture() {
		if (currentPict >= pictureSequence.length) {
			return "-1";
		}
		return pictureSequence[currentPict];
	}

	/**
	 * Получить верный ответ.
	 * 0 - таймаут (ничего)
	 * 1 - нажатие на пробел (повторение картинки)
	 */
	private String getCorrectAnswer() {
		String answer = correctAnswers[currentPict];
		currentPict++;
		return answer;
	}

	/**
	 * Увеличить счетчик ошибки на 1
	 */
	private void increaseIncorrectAnswers() {
		incorrectAnswers++;
	}

	/**
	 * Прибавить время ответа
	 */
	private void increaseTime(double time) {
		answerTime += time;
	}

	@Override
	public int getTestId() {
		return 2;
	}

	@Override
	public String getTestName() {
		return "Тест Memtrax";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1, П2 – показатели теста. % ошибок и среднее время ответа в секундах");
		strings.add(BEST_AND_ORIGINAL_COMPARE_TEST_INFO);
		strings.add("П1Л, П2Л – лучшее значение. % ошибок и среднее время ответа в секундах");
		strings.add("П1Э, П2Э – " +
			"значение показателей оригинально теста. % ошибок и среднее время ответа в секундах");
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
		for (int i = 0; i < testsCount; i++) {
			p1Avg += allResults.get(i * getParamsCount());
			p2Avg += allResults.get(i * getParamsCount() + 1);
		}
		p1Avg /= testsCount;
		p2Avg /= testsCount;

		strings.add("");
		strings.add("Среднее");
		strings.add(String.valueOf(p1Avg));
		strings.add(String.valueOf(p2Avg));
	}

	private void addAllToResults(LinkedList<Double> allResults, LinkedList<Double> results) {
		if (results.get(0) != null) {
			allResults.add(results.get(0));
		}
		if (results.get(1) != null) {
			allResults.add(results.get(1));
		}
	}

	private void addAll(LinkedList<String> strings, LinkedList<Double> results) {
		if (results.get(0) != null) {
			strings.add(String.valueOf(results.get(0)));
		}
		if (results.get(1) != null) {
			strings.add(String.valueOf(results.get(1)));
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
		for (int i = 0; i < testsCount; i++) {
			if (p1Best > allResults.get(i)) {
				p1Best = allResults.get(i);
			}
			if (p2Best > allResults.get(i + 1)) {
				p2Best = allResults.get(i + 1);
			}
		}

		bestResults.add(p1Best);
		bestResults.add(p2Best);

		for (int i = 0; i < originalTests.size(); i++) {
			strings.add(String.valueOf(bestResults.get(i)));
			strings.add(String.valueOf(originalTests.get(i)));
		}
	}

	@Override
	public int getParamsCount() {
		return 2;
	}

}