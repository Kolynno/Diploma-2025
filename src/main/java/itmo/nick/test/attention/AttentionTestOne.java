package itmo.nick.test.attention;

import itmo.nick.database.ResultTableService;
import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

@Getter
@Setter
/**
 * Тест на внимание 1 - Тест Струпа
 */
public class AttentionTestOne extends SimpleTest {

	@Autowired
	private ResultTableService resultTableService;

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
		strings.add("П1, П2, П3, П4 - показатели теста. Время в секундах на каждый этап соответственно");
		strings.add("П1С,П2С, П3С, П4С - " +
			"значение в секундах показателей у других участников на каждый этап соответственно");
		strings.add("П1Л, П2Л, П3Л, П4Л - лучшее значение в секунлах на каждый этап соответственно");
		strings.add("П1Э, П2Э, П3Э, П4Э - " +
			"значение в секундах показателей оригинально теста на каждый этап соответственно");
		strings.add("Показатели со знаком процента (П*С%, П*Э%) - " +
			"процент разницы между лучших показателем участника (П*) " +
			"и лучшего других участников и оригинального тестирования");
		return strings;
	}

	@Override
	public LinkedList<String> getAllPersonDataAndCompareToOther(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		int testsCount = resultTableService.getTestCount(personId);
		ArrayList<String> allResults = new ArrayList<>();

		LocalDate date = resultTableService.getFirstDateResult();
		for (int i = 1; i <= testsCount; i++) {
			strings.add(String.valueOf(i));
			strings.add(date.toString());
			LinkedList<String> results = resultTableService.getResults(personId, date);
			strings.addAll(results);
			date = resultTableService.getTestDate(personId, i+1);
			allResults.addAll(results);
		}

		//calc avg data and then add there
		for (int i = 0; i < testsCount; i++) {

		}

		strings.add("21.05.2003");
		strings.add("42");
		strings.add("55");
		strings.add("62");
		strings.add("52");

		return strings;
	}

	@Override
	public LinkedList<String> getBestPersonDataAndCompareToOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("61");
		strings.add("42");
		strings.add("55");
		strings.add("62");

		return strings;
	}

	@Override
	public LinkedList<String> getPercentCompareToOtherAndOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("61%");
		strings.add("42%");
		strings.add("55%");
		strings.add("62%");
		return strings;
	}

	@Override
	public LinkedList<String> getSummary(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("Сравнение с другими участниками в среднем: + 4.3%");
		strings.add("Сравнение с эталонным результатом в среднем: -0.1%%");
		return strings;
	}
}
