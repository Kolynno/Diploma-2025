package itmo.nick.test.reaction;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Тест на время реакции 1 - статичное
 *
 * @author Николай Жмакин
 * @since 29.12.2024
 */
@Component
public class ReactionTestOne extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	static ReactionTestOne reactionTestOne;
	private ArrayList<ReactionTestOneData> reactionTestOneDataList = new ArrayList<>();

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	private static int TEST_TIME_IN_SEC = 10;
	private static int TEST_TIME_IN_MS = TEST_TIME_IN_SEC * 1000;
	private static double ERROR_TIME_IN_SEC = 0.5;

	protected ReactionTestOne() {
		super(LAST_STAGE);
	}

	public void addData(ReactionTestOneData data) {
		reactionTestOneDataList.add(data);
	}

	public static ReactionTestOne getInstance() {
		if (reactionTestOne == null) {
			reactionTestOne = new ReactionTestOne();

			return reactionTestOne;
		} else {
			return reactionTestOne;
		}
	}

	@Override
	public String result(String[] original) {
		return "Среднее время реакции бодрого человека " + original[0] + " мс. <br>при " + original[1] +
			" пропусков (реакция более 500мс) и " + original[2] + " преждевременных нажатий <br>У человека " +
			"с недостатком сна среднее время реакции " + original[3] + " мс.<br>при " + original[4] +
			" пропусков и " + original[5] + " преждевременных нажатий<br>Ваш результат " +
			String.valueOf(getReactionTime()).substring(0,5) + " c. - скорость реакции, количество ошибок = "
			+ getErrors() + ", ложных нажатий = " + getFalseStarts();
	}

	public String getFalseStarts() {
		return String.valueOf(reactionTestOneDataList.stream()
			.mapToDouble(ReactionTestOneData::getDelay)
			.filter(time -> time == 0).count());
	}

	public long getErrors() {
		return reactionTestOneDataList.stream()
			.mapToDouble(ReactionTestOneData::getReactionTime)
			.filter(time -> time > ERROR_TIME_IN_SEC).count();
	}

	public double getReactionTime() {
		double total = reactionTestOneDataList.stream().mapToDouble(ReactionTestOneData::getReactionTime).sum();
		return total/reactionTestOneDataList.stream().filter(time -> time.getReactionTime() != 0).count();
	}

	public void delete() {
		reactionTestOne = null;
	}

	public boolean timeIsUp() {
		double totalTime = reactionTestOneDataList.stream()
			.mapToDouble(data -> data.getReactionTime() + data.getDelay()).sum();
		return totalTime > TEST_TIME_IN_MS;
	}

	public ArrayList<ReactionTestOneData> getReactionTestOneDataList() {
		return reactionTestOneDataList;
	}


	@Override
	public String getTestName() {
		return "Тест скорости реакции (статичный)";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1, П2, П3 – показатели теста. Время в секундах средней реакции, " +
			"кол-во ошибок, кол-во пропусков соответственно");
		strings.add("П1Л, П2Л, П3Л – лучшее значение показателей (наименьшее время, наименьшее кол-во ошибок и пропусков");
		strings.add("П1Э, П2Э, П3Э – " +
			"значение в секундах показателя оригинально теста, кол-во ошибок и пропусков соответственно");
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
	public int getTestId() {
		return 3;
	}

	@Override
	public int getParamsCount() {
		return 3;
	}
}
