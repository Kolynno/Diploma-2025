package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;

/**
 * Тест внимания 2 - Таблица Шульте
 *
 * @author Николай Жмакин
 * @since 02.02.2025
 */
@Getter
@Setter
public class AttentionTestTwo extends SimpleTest {

	static AttentionTestTwo attentionTestTwo;
	AttentionTestTwoData data = new AttentionTestTwoData();

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	private AttentionTestTwo() {
		super(LAST_STAGE);
	}

	public static AttentionTestTwo getInstance() {
		if (attentionTestTwo == null) {
			attentionTestTwo = new AttentionTestTwo();

			return attentionTestTwo;
		} else {
			return attentionTestTwo;
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
		return "Среднее время - " + original[1] + " сек. \n" +
			"Ваше время - " + Math.round(data.getTime()) + " сек.";
	}

	public void setData(AttentionTestTwoData data) {
		this.data = data;
	}

	public void delete() {
		attentionTestTwo = null;
	}
}
