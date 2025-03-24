package itmo.nick.test.memory;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Заучивание 10 слов - тест на память 2
 *
 * @author Николай Жмакин
 * @since 04.02.2025
 */
@Component
public class MemoryTestTwo extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	static MemoryTestTwo memoryTestTwo;
	/**
	 * Номер последнего этап теста
	 */
	public static final int LAST_STAGE = 6;

	private MemoryTestTwoData[] memoryTestTwoData;

	private LinkedHashMap<String, Integer> wordToCountMap;

	public Set<String> getWords() {
		return wordToCountMap.keySet();
	}

	public LinkedHashMap<String, Integer> getWordToCountMap() {
		return wordToCountMap;
	}

	private LinkedList<Integer> correctWords = new LinkedList<>();

	private String[] originalWords = {
		"Жук",
		"Мальчик",
		"Арка",
		"Колесо",
		"Индюк",
		"Носок",
		"Костер",
		"Официант",
		"Лампочка",
		"Ягода"
	};

	protected MemoryTestTwo() {
		super(LAST_STAGE);
		memoryTestTwoData = new MemoryTestTwoData[5];
		wordToCountMap = new LinkedHashMap<>();
		for (int i = 0; i < originalWords.length; i++) {
			wordToCountMap.put(originalWords[i], 0);
		}
	}

	public static MemoryTestTwo getInstance() {
		if (memoryTestTwo == null) {
			memoryTestTwo = new MemoryTestTwo();

			return memoryTestTwo;
		} else {
			return memoryTestTwo;
		}
	}

	@Override
	public String result(String[] original) {
		return "Результаты в оригинальном тестировании: " + original[0] + ", " + original[1] + ", " + original[2] + ", "
			+ original[3] + ", " + original[4] + "\n" + " правильно названных слов по этапамю.\n" +
			"Ваш результат: " + getResult() + " соответсвенно.";
	}

	private String getResult() {
		return correctWords.get(0) + ", " + correctWords.get(1) + ", " + correctWords.get(2) + ", "
			+ correctWords.get(3) + ", " + correctWords.get(4);
	}


	public void delete() {
		memoryTestTwo = null;
	}


	public void updateMap(MemoryTestTwoData data) {
		int count = 0;
		for (String word : data.mentionWordsSet) {
			wordToCountMap.put(word, wordToCountMap.getOrDefault(word, 0) + 1);
			if (Arrays.asList(originalWords).contains(word)) {
				count++;
			}
		}
		correctWords.add(count);
	}

	public LinkedList<Integer> getCorrectWords() {
		return correctWords;
	}

	@Override
	public int getTestId() {
		return 7;
	}

	@Override
	public String getTestName() {
		return "Тест заучивание 10 слов";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1, П2, П3, П4, П5 – показатели теста. Кол-во выученных слов на каждый этап соответственно");
//		strings.add("П1С,П2С, П3С, П4С – " +
//			"значение в секундах показателей у других участников на каждый этап соответственно");
		strings.add("П1Л, П2Л, П3Л, П4Л, П5Л – лучшее значение. Кол-во выученных слов на каждый этап соответственно");
		strings.add("П1Э, П2Э, П3Э, П4Э, П5Э – " +
			"значение показателей оригинально теста. Кол-во выученных слов на каждый этап соответственно");
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
		originalTests.add(Double.valueOf(originalData[3]));
		originalTests.add(Double.valueOf(originalData[4]));
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
		double p4Avg = 0;
		double p5Avg = 0;
		for (int i = 0; i < testsCount; i++) {
			p1Avg += allResults.get(i * getParamsCount());
			p2Avg += allResults.get(i * getParamsCount() + 1);
			p3Avg += allResults.get(i * getParamsCount() + 2);
			p4Avg += allResults.get(i * getParamsCount() + 3);
			p5Avg += allResults.get(i * getParamsCount() + 4);
		}
		p1Avg /= testsCount;
		p2Avg /= testsCount;
		p3Avg /= testsCount;
		p4Avg /= testsCount;
		p5Avg /= testsCount;

		strings.add("");
		strings.add("Среднее");
		strings.add(String.valueOf(p1Avg));
		strings.add(String.valueOf(p2Avg));
		strings.add(String.valueOf(p3Avg));
		strings.add(String.valueOf(p4Avg));
		strings.add(String.valueOf(p5Avg));
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
		if (results.get(3) != null) {
			allResults.add(results.get(3));
		}
		if (results.get(4) != null) {
			allResults.add(results.get(3));
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
		if (results.get(3) != null) {
			strings.add(String.valueOf(results.get(3)));
		}
		if (results.get(4) != null) {
			strings.add(String.valueOf(results.get(3)));
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
		double p1Best = Integer.MIN_VALUE;
		double p2Best = Integer.MIN_VALUE;
		double p3Best = Integer.MIN_VALUE;
		double p4Best = Integer.MIN_VALUE;
		double p5Best = Integer.MIN_VALUE;
		for (int i = 0; i < testsCount; i++) {
			if (p1Best < allResults.get(i)) {
				p1Best = allResults.get(i);
			}
			if (p2Best < allResults.get(i + 1)) {
				p2Best = allResults.get(i + 1);
			}
			if (p3Best < allResults.get(i + 2)) {
				p3Best = allResults.get(i + 2);
			}
			if (p4Best < allResults.get(i + 3)) {
				p4Best = allResults.get(i + 3);
			}
			if (p5Best < allResults.get(i + 4)) {
				p5Best = allResults.get(i + 4);
			}
		}

		bestResults.add(p1Best);
		bestResults.add(p2Best);
		bestResults.add(p3Best);
		bestResults.add(p4Best);
		bestResults.add(p5Best);

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
		return 5;
	}
}