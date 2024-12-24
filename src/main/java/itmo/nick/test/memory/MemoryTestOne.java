package itmo.nick.test.memory;

import itmo.nick.test.SimpleTest;
import itmo.nick.test.attention.AttentionTestOne;

/**
 * Memtrax - тест на память 1
 *
 * @author Николай Жмакин
 * @since 22.12.2024
 */
public class MemoryTestOne extends SimpleTest {

	static MemoryTestOne memoryTestOne;
	private final MemoryTestOneData memoryTestOneData;

	protected MemoryTestOne() {
		super(3);
		memoryTestOneData = new MemoryTestOneData();
	}

	public static MemoryTestOne getInstance() {
		if (memoryTestOne == null) {
			memoryTestOne = new MemoryTestOne();

			return memoryTestOne;
		} else {
			return memoryTestOne;
		}
	}

	public String getNextPicture() {
		return memoryTestOneData.getNextPicture();
	}

	public String result(String[] split) {

		System.out.println(memoryTestOneData.getIncorrectAnswers() + "- Incorrect");
		System.out.println(Math.round((double) memoryTestOneData.getIncorrectAnswers() /50.0 * 100)/100 + "- % Incorrect");
		System.out.println(memoryTestOneData.getAnswerTime() + "- Sec total");
		System.out.println(Math.round(memoryTestOneData.getAnswerTime()/25.0 * 10) / 10 + "- Sec for each");

		return "Обычные люди: " + split[0] + " ошибок и до " + split[1] + " сек. в среднем скорость ответа<br>" +
			"Легкое когнитивное нарушение: " + split[2] + " ошибок и до " + split[3] + " сек. в среднем скорость ответа<br>" +
			"Легкая степень деменции: " + split[4] + " ошибок и до " + split[5] + " сек. в среднем скорость ответа<br>" +
			"Средняя и тяжка деменция: от " + split[4] + " ошибок и от " + split[5] + " сек. в среднем скорость ответа<br>" +
			"Ваш результат: " + Math.round((double) memoryTestOneData.getIncorrectAnswers() /50 * 100) / 100  + "%, " +
			Math.round(memoryTestOneData.getAnswerTime()/25 * 10) / 10 + " сек.";
	}

	public void answer(String type, double time) {
		if (!memoryTestOneData.getCorrectAnswer().equals(type)) {
			memoryTestOneData.increaseIncorrectAnswers();
		}
		memoryTestOneData.increaseTime(time);
	}
}
