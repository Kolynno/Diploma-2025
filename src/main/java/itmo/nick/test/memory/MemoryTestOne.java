package itmo.nick.test.memory;

import itmo.nick.test.SimpleTest;

/**
 * Memtrax - тест на память 1
 *
 * @author Николай Жмакин
 * @since 22.12.2024
 */
public class MemoryTestOne extends SimpleTest {

	static MemoryTestOne memoryTestOne;
	/**
	 * Номер последнего этап теста
	 */
	public static final int LAST_STAGE = 2;

	/**
	 * Время таймаута реакции
	 */
	public static final double TIMEOUT_IN_SEC = 3.0;

	private final String[] pictureSequence;
	private final String[] correctAnswers;

	private int incorrectAnswers;
	private double answerTime;

	private int currentPict;


	protected MemoryTestOne() {
		super(LAST_STAGE);
		pictureSequence = getSequence();
		correctAnswers = getCorrectAnswers();
	}

	public static MemoryTestOne getInstance() {
		if (memoryTestOne == null) {
			memoryTestOne = new MemoryTestOne();

			return memoryTestOne;
		} else {
			return memoryTestOne;
		}
	}

	public void addData(MemoryTestOneData data) {
		if (data.getReactionTime() > TIMEOUT_IN_SEC) {
			memoryTestOne.answer("0", 0);
		} else {
			//только время реации распознавания
			memoryTestOne.answer("1", data.getReactionTime());
		}
	}

	@Override
	public String result(String[] original) {
		return "Обычные люди: " + original[0] + " ошибок и до " + original[1] + " сек. в среднем скорость ответа<br>" +
			"Легкое когнитивное нарушение: " + original[2] + " ошибок и до " + original[3] + " сек. в среднем скорость ответа<br>" +
			"Легкая степень деменции: " + original[4] + " ошибок и до " + original[5] + " сек. в среднем скорость ответа<br>" +
			"Средняя и тяжка деменция: от " + original[4] + " ошибок и от " + original[5] + " сек. в среднем скорость ответа<br>" +
			"Ваш результат: " + getErrorPercent() + "% ошибок,<br>" +
			getAnswerMs() + " мс. среднее время реакции на повторную картинку";
	}

	public long getAnswerMs() {
		return Math.round(answerTime / 25.0 * 1000);
	}

	public double getErrorPercent() {
		return Math.round(incorrectAnswers / 52.0 * 1000) / 10.0;
	}

	private void answer(String type, double time) {
		if (!getCorrectAnswer().equals(type)) {
			increaseIncorrectAnswers();
		}
		increaseTime(time);
	}

	public void delete() {
		memoryTestOne = null;
	}

	private String[] getCorrectAnswers() {
		return new String[] {"0","0","0","0","0","0","0","0","0","0","0","0","1","1","0","0","0","0","0","0","0","0", "0",
			"1","1","0","1","1","1","1","0","1","0","1","1","1","1","1","1","0","1","1","1","1","1","1","1","1","1","1"};
	}

	//Пока одна и та же последовательность, а не случайная
	private String[] getSequence() {
		return new String[] {"25","1","19","5","15","12","2","6","20","3","23","4","5","6","14","7","8","17","9","24",
			"10","21","11","10","12","13","2","14","15","9","16","17","18","19","20","4","21","11","3","22","23","16",
			"24","1","25","7","8","13","18","22"};
	}

	/**
	 * Получить номер картинки
	 */
	public String getNextPicture() {
		if (currentPict >= pictureSequence.length) {
			return "-1";
		}
		return pictureSequence[currentPict];
	}

	/**
	 * Получить верный ответ.
	 * 0 - таймаут (ничего)
	 * 1 - нажатие на пробел (повторение картинки)
	 */
	private String getCorrectAnswer() {
		String answer = correctAnswers[currentPict];
		currentPict++;
		return answer;
	}

	/**
	 * Увеличить счетчик ошибки на 1
	 */
	private void increaseIncorrectAnswers() {
		incorrectAnswers++;
	}

	/**
	 * Прибавить время ответа
	 */
	private void increaseTime(double time) {
		answerTime += time;
	}

	@Override
	public int getTestId() {
		return 2;
	}
}