package itmo.nick.test.attention;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

@Getter
@Setter
/**
 * Тест на внимание 1 - Тест Струпа
 */
@Component
public class AttentionTestOne extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	static AttentionTestOne attentionTestOne;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 5;

	private AttentionTestOneData[] testData = new AttentionTestOneData[5];
	private AttentionTestOne() {
		super(LAST_STAGE);
	}

	public static AttentionTestOne getInstance() {
		if (attentionTestOne == null) {
			attentionTestOne = new AttentionTestOne();

			return attentionTestOne;
		} else {
			return attentionTestOne;
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
		String stage1Text = getStageText(attentionTestOne.getTestData()[1].getTime(), original, 0);
		String stage2Text = getStageText(attentionTestOne.getTestData()[2].getTime(), original, 1);
		String stage3Text = getStageText(attentionTestOne.getTestData()[3].getTime()
			+ attentionTestOne.getTestData()[3].getErrors() * 2 * (attentionTestOne.getTestData()[3].getTime() / 100), original, 2);
		String stage4Text = getStageText(attentionTestOne.getTestData()[4].getTime()
			+ attentionTestOne.getTestData()[4].getErrors() * 2 * (attentionTestOne.getTestData()[4].getTime() / 100), original, 3);

		return "Этап 1: " + stage1Text + "<br>Этап 2: " + stage2Text + "<br>Этап 3: " + stage3Text + "<br>Этап 4: " + stage4Text;
	}

	private String getStageText(Double testOne, String[] original, int originalStage) {
		double stage = getStageTime(testOne, original, originalStage);
		return stage < 0 ?
			"вы быстрее на " + Math.abs(stage) + "с."
			: "вы медленне на " + stage + "с.";
	}

	private double getStageTime(Double testOne, String[] original, int originalStage) {
		return (double) Math.round((testOne - Double.parseDouble(original[originalStage])) * 100) / 100;
	}

	@Override
	public int getTestId() {
		return 1;
	}

	public void delete() {
		attentionTestOne = null;
	}

	@Override
	public String getTestName() {
		return "Тест Струпа";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("№ – номер попытки");
		strings.add("Дата – дата тестирования");
		strings.add("П1, П2, П3, П4 – показатели теста. Время в секундах на каждый этап соответственно");
		strings.add("П1С,П2С, П3С, П4С – " +
			"значение в секундах показателей у других участников на каждый этап соответственно");
		strings.add("П1Л, П2Л, П3Л, П4Л – лучшее значение в секунлах на каждый этап соответственно");
		strings.add("П1Э, П2Э, П3Э, П4Э – " +
			"значение в секундах показателей оригинально теста на каждый этап соответственно");
		strings.add("Показатели со знаком процента (П*С%, П*Э%) – " +
			"процент разницы между лучших показателем участника (П*) " +
			"и лучшего других участников и оригинального тестирования");
		return strings;
	}

	@Override
	public LinkedList<String> getAllPersonDataAndCompareToOther(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		int testsCount = resultTableService.getTestCount(personId);
		LinkedList<Double> allResults = new LinkedList<>();
		LinkedList<Double> bestResults = new LinkedList<>();
		LinkedList<Double> originalResults = getOriginalResults();
		LinkedList<Double> otherBestResults = resultTableService.getOtherBest(1);

		setTableAllTestsResult(personId, testsCount, strings, allResults);
		setTableBestAndOriginal(testsCount, allResults, strings, bestResults, originalResults);
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

	private LinkedList<Double> getOriginalResults() {
		String[] originalData = testTableService.getResultsById(1).split(";");
		LinkedList<Double> originalTests = new LinkedList<>();
		originalTests.add(Double.valueOf(originalData[0]));
		originalTests.add(Double.valueOf(originalData[1]));
		originalTests.add(Double.valueOf(originalData[2]));
		originalTests.add(Double.valueOf(originalData[3]));
		return originalTests;
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
		double p4Best = Integer.MAX_VALUE;
		double p5Best = Integer.MAX_VALUE;
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
			if (p4Best > allResults.get(i + 3)) {
				p4Best = allResults.get(i + 3);
			}
			if (p5Best > allResults.get(i + 4)) {
				p5Best = allResults.get(i + 4);
			}
		}


		bestResults.add(p1Best);
		bestResults.add(p2Best);
		bestResults.add(p3Best);
		bestResults.add(p4Best);

		for (int i = 0; i < originalTests.size(); i++) {
			strings.add(String.valueOf(bestResults.get(i)));
			strings.add(String.valueOf(originalTests.get(i)));
		}
	}

	private void setTableAllTestsResult(String personId, int testsCount, LinkedList<String> strings, LinkedList<Double> allResults) {
		LocalDate date = resultTableService.getFirstDateResult();
		for (int i = 1; i <= testsCount; i++) {
			strings.add(String.valueOf(i));
			strings.add(date.toString());
			LinkedList<Double> results = resultTableService.getResults(personId, date);
			addAll(strings, results);
			date = resultTableService.getTestDate(personId, i+1);
			addAllToResults(allResults, results);
		}
		double p1Avg = 0;
		double p2Avg = 0;
		double p3Avg = 0;
		double p4Avg = 0;
		double p5Avg = 0;
		for (int i = 0; i < testsCount; i++) {
			p1Avg += allResults.get(i*4);
			p2Avg += allResults.get(i*4 + 1);
			p3Avg += allResults.get(i*4 + 2);
			p4Avg += allResults.get(i*4 + 3);
			p5Avg += allResults.get(i*4 + 4);
		}
		p1Avg /= testsCount;
		p2Avg /= testsCount;
		p3Avg /= testsCount;
		p4Avg /= testsCount;

		strings.add("");
		strings.add("Среднее");
		strings.add(String.valueOf(p1Avg));
		strings.add(String.valueOf(p2Avg));
		strings.add(String.valueOf(p3Avg));
		strings.add(String.valueOf(p4Avg));
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
			allResults.add(results.get(4));
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
			strings.add(String.valueOf(results.get(4)));
		}
	}

	@Override
	public LinkedList<String> getBestPersonDataAndCompareToOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		return strings;
	}

	@Override
	public LinkedList<String> getPercentCompareToOtherAndOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		return strings;
	}

	@Override
	public LinkedList<String> getSummary(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		return strings;
	}
}
