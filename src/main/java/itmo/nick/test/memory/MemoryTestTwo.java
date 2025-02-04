package itmo.nick.test.memory;

import itmo.nick.test.SimpleTest;

/**
 * Заучивание 10 слов - тест на память 2
 *
 * @author Николай Жмакин
 * @since 04.02.2025
 */
public class MemoryTestTwo extends SimpleTest {

	static MemoryTestTwo memoryTestTwo;
	/**
	 * Номер последнего этап теста
	 */
	public static final int LAST_STAGE = 6;


	private final String[] pictureSequence;
	private final String[] correctAnswers;

	private int incorrectAnswers;
	private double answerTime;

	private int currentPict;


	protected MemoryTestTwo() {
		super(LAST_STAGE);
		pictureSequence = getSequence();
		correctAnswers = getCorrectAnswers();
	}

	public static MemoryTestTwo getInstance() {
		if (memoryTestTwo == null) {
			memoryTestTwo = new MemoryTestTwo();

			return memoryTestTwo;
		} else {
			return memoryTestTwo;
		}
	}

	public void addData(MemoryTestTwoData data) {
		memoryTestTwo.answer("1", data.getReactionTime());
	}

	@Override
	public String result(String[] original) {
		return "res";
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
		memoryTestTwo = null;
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
}