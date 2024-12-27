package itmo.nick.test.memory;

/**
 * Данные для теста на память 1
 *
 * @author Николай Жмакин
 * @since 22.12.2024
 */
public class MemoryTestOneData {
	private final String[] pictureSequence;
	private final String[] correctAnswers;

	private int incorrectAnswers;
	private double answerTime;

	private int currentPict;

	public MemoryTestOneData() {
		pictureSequence = getSequence();
		correctAnswers = getCorrectAnswers();

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
	public String getCorrectAnswer() {
		System.out.println("Pict:" + currentPict);
		String answer = correctAnswers[currentPict];
		currentPict++;
		return answer;
	}

	/**
	 * Увеличить счетчик ошибки на 1
	 */
	public void increaseIncorrectAnswers() {
		incorrectAnswers++;
	}

	/**
	 * Прибавить время ответа
	 */
	public void increaseTime(double time) {
		answerTime += time;
	}

	public double getAnswerTime() {
		return answerTime;
	}

	public int getIncorrectAnswers() {
		return incorrectAnswers;
	}
}
