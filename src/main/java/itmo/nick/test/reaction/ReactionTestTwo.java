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

	private static int ATTEMPTS_PER_INTERVAL = 5;

	private int intervalsCount = 0;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 6;


	protected ReactionTestTwo() {
		super(LAST_STAGE);
		for (int i = 0; i < ATTEMPTS_PER_INTERVAL; i++) {
			reactionTestTwoDataList.add(new ReactionTestTwoData());
		}
	}

	public void addData(Double[] reactionTime) {
		reactionTestTwoDataList.get(intervalsCount).addReaction(reactionTime);
		intervalsCount++;
	}

	public ArrayList<ReactionTestTwoData> getReactionTestTwoDataList() {
		return reactionTestTwoDataList;
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
		return "2.0: " + reactionTestTwoDataList.get(0).getAverageReactionTime() + " c.\n" +
			"1.5: " + reactionTestTwoDataList.get(1).getAverageReactionTime() + " c.\n" +
			"1.1: " + reactionTestTwoDataList.get(2).getAverageReactionTime() + " c.\n" +
			"0.9: " + reactionTestTwoDataList.get(3).getAverageReactionTime() + " c.\n" +
			"0.7: " + reactionTestTwoDataList.get(4).getAverageReactionTime() + " c.\n";
	}

	public void delete() {
		reactionTestTwo = null;
	}

}
