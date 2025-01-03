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

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestOne() {
		super(LAST_STAGE);
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
		return null;
	}

	public void delete() {
		processingTestOne = null;
	}
}
