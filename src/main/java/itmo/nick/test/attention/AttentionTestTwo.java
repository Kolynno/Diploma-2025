package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Тест внимания 2 - Таблица Шульте
 *
 * @author Николай Жмакин
 * @since 02.02.2025
 */
@Getter
@Setter
@Component
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

	@Override
	public int getTestId() {
		return 6;
	}

	public void delete() {
		attentionTestTwo = null;
	}

	@Override
	public String getTestName() {
		return "Тест таблица Шульте";
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("Первый - 11");
		strings.add("Второй - 22");
		strings.add("Третий - 33");

		return strings;
	}

	@Override
	public LinkedList<String> getAllPersonDataAndCompareToOther(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("21.05.2023");
		strings.add("1");
		strings.add("2");
		strings.add("3");
		strings.add("4");

		return strings;
	}

	@Override
	public LinkedList<String> getBestPersonDataAndCompareToOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("11");
		strings.add("22");
		strings.add("33");
		strings.add("44");

		return strings;
	}

	@Override
	public LinkedList<String> getPercentCompareToOtherAndOriginal(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("1%");
		strings.add("2%");
		strings.add("3%");
		strings.add("4%");
		return strings;
	}

	@Override
	public LinkedList<String> getSummary(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("Сравнение с другими участниками в среднем: + 111.3%");
		strings.add("Сравнение с эталонным результатом в среднем: -111.1%%");
		return strings;
	}
}
