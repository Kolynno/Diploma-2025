package itmo.nick.test.processing;

/**
 * Данные для теста на обработку информации 2
 *
 * @author Николай Жмакин
 * @since 14.01.2025
 */
public class ProcessingTestTwoData {

	/**
	 * Время реакции. Если равно 1, то участник ничего не нажимал
	 */
	private double reactionTime;

	public ProcessingTestTwoData(double reactionTime) {
		this.reactionTime = reactionTime;
	}

	public ProcessingTestTwoData() {
	}

	public double getReactionTime() {
		return reactionTime;
	}

	public void setReactionTime(double reactionTime) {
		this.reactionTime = reactionTime;
	}

	@Override
	public String toString() {
		return "reactionTime=" + reactionTime;
	}
}
