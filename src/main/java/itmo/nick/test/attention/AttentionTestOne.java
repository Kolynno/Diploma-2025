package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Тест на внимание 1 - Тест Струпа
 */
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

	@Override
	public int getTestId() {
		return 1;
	}

	public void delete() {
		attentionTestOne = null;
	}

	@Override
	public String reportInfo() {
		return "Обозначения:\n" +
			"- № – номер попытки\n" +
			"- Дата – дата тестирования\n" +
			"- П1 – показатель 1: время в секундах на ответ на этапе 1 у данного участника\n" +
			"- П1С – показатель 1: время в секундах на ответ на этапе 1 у других участников\n" +
			"- П2 – показатель 2: время в секундах на ответ на этапе 2\n" +
			"- П2С – показатель 2: время в секундах на ответ на этапе 2 у других участников\n" +
			"- П3 – показатель 3: время в секундах на ответ на этапе 3\n" +
			"- П3С – показатель 3: время в секундах на ответ на этапе 3 у других участников\n" +
			"- П4 – показатель 4: время в секундах на ответ на этапе 4\n" +
			"- П4С – показатель 4: время в секундах на ответ на этапе 4 у других участников\n" +
			"- Среднее – значение в секундах в среднем\n" +
			"- П1Л – показатель 1: лучшее время в секундах на ответ на этапе 1 у данного участника\n" +
			"- П1Э – показатель 1: эталонное время из оригинального тестирования на этапе 1\n" +
			"- П2Л – показатель 2: лучшее время в секундах на ответ на этапе 2 у данного участника\n" +
			"- П2Э – показатель 2: эталонное время из оригинального тестирования на этапе 2\n" +
			"- П3Л – показатель 3: лучшее время в секундах на ответ на этапе 3 у данного участника\n" +
			"- П3Э – показатель 3: эталонное время из оригинального тестирования на этапе 3\n" +
			"- П4Л – показатель 4: лучшее время в секундах на ответ на этапе 4 у данного участника\n" +
			"- П4Э – показатель 4: эталонное время из оригинального тестирования на этапе 4\n" +
			"- П1С% – сравнение лучшего показателя 1 с другими участниками в процентах\n" +
			"- П1Э% – сравнение лучшего показателя 1 с эталонным в процентах\n" +
			"- П2С% – сравнение лучшего показателя 2 с другими участниками в процентах\n" +
			"- П2Э% – сравнение лучшего показателя 2 с эталонным в процентах\n" +
			"- П3С% – сравнение лучшего показателя 3 с другими участниками в процентах\n" +
			"- П3Э% – сравнение лучшего показателя 3 с эталонным в процентах\n" +
			"- П4С% – сравнение лучшего показателя 4 с другими участниками в процентах\n" +
			"- П4Э% – сравнение лучшего показателя 4 с эталонным в процентах" ;
	}
}
