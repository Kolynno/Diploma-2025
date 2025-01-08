package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttentionTestOne extends SimpleTest {

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

	public void delete() {
		attentionTestOne = null;
	}
}
