package itmo.nick.test.processing;

/**
 * Данные для теста на обработку информации 1
 *
 * @author Николай Жмакин
 * @since 05.01.2025
 */
public class ProcessingTestOneData {

	/**
	 * Время реакции. Если равно 1, то участник ничего не нажимал
	 */
	private double reactionTime;

	public ProcessingTestOneData(double reactionTime) {
		this.reactionTime = reactionTime;
	}

	public ProcessingTestOneData() {
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
