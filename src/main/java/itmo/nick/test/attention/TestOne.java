package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestOne extends SimpleTest {

	static TestOne testOne;

	private TestOneData[] testData = new TestOneData[5];
	private TestOne() {
		super(5);
	}

	public static TestOne getInstance() {
		if (testOne == null) {
			testOne = new TestOne();

			return testOne;
		} else {
			return testOne;
		}
	}

	/**
	 * Рассчет итогового результата (в секундах) в виде человеческого текста
	 * для отображения на этапе результата
	 * @param original оригинальное время тестов
	 * @return удобочитаемый текст результата
	 */
	public String resultSec(String[] original) {
		String stage1Text = getStageText(testOne.getTestData()[1].getTime(), original, 0);
		String stage2Text = getStageText(testOne.getTestData()[2].getTime(), original, 1);
		String stage3Text = getStageText(testOne.getTestData()[3].getTime()
			+ testOne.getTestData()[3].getErrors() * 2 * (testOne.getTestData()[3].getTime() / 100), original, 2);
		String stage4Text = getStageText(testOne.getTestData()[4].getTime()
			+ testOne.getTestData()[4].getErrors() * 2 * (testOne.getTestData()[4].getTime() / 100), original, 3);

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
}
