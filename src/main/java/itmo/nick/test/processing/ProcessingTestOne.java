package itmo.nick.test.processing;

import itmo.nick.test.SimpleTest;
import itmo.nick.test.reaction.ReactionTestOneData;

import java.util.ArrayList;

/**
 * Тест на обработку информации 1
 *
 * @author Николай Жмакин
 * @since 03.01.2025
 */
public class ProcessingTestOne extends SimpleTest {

	static ProcessingTestOne processingTestOne;

	private int currentNumbers = 0;
	private String[] numbersSequence;
	private String[] correctAnswerSequence;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestOne() {
		super(LAST_STAGE);
		numbersSequence = new String[] {"11111", "11111", "11112", "11112", "22222"};
		correctAnswerSequence = new String[] {"0", "1", "0", "1", "0"};
	}

	public static ProcessingTestOne getInstance() {
		if (processingTestOne == null) {
			processingTestOne = new ProcessingTestOne();

			return processingTestOne;
		} else {
			return processingTestOne;
		}
	}

	public String result(String[] split) {
		return "null";
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextNumbers() {
		if (currentNumbers < numbersSequence.length) {
			return numbersSequence[currentNumbers];
		}
		return "-1";
	}

	public void addData(ProcessingTestOneData data) {
	}
}
