package itmo.nick.test.reaction;

import itmo.nick.test.SimpleTest;

import java.util.ArrayList;

/**
 * Тест на время реакции 2 - движение
 *
 * @author Николай Жмакин
 * @since 06.02.2025
 */
public class ReactionTestTwo extends SimpleTest {

	static ReactionTestTwo reactionTestTwo;
	private ArrayList<ReactionTestTwoData> reactionTestTwoDataList = new ArrayList<>();

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 6;

	private static int TEST_TIME_IN_SEC = 10;
	private static int TEST_TIME_IN_MS = TEST_TIME_IN_SEC * 1000;
	private static double ERROR_TIME_IN_SEC = 0.5;

	protected ReactionTestTwo() {
		super(LAST_STAGE);
	}

	public boolean attentionsIsUp() {
		//3 att
		return false;
	}

	public void addData(ReactionTestTwoData data) {
		reactionTestTwoDataList.add(data);
	}

	public static ReactionTestTwo getInstance() {
		if (reactionTestTwo == null) {
			reactionTestTwo = new ReactionTestTwo();

			return reactionTestTwo;
		} else {
			return reactionTestTwo;
		}
	}

	@Override
	public String result(String[] original) {
		return "RES:";
	}

	public void delete() {
		reactionTestTwo = null;
	}

}
