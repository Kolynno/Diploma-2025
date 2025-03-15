package itmo.nick.test.reaction;

import itmo.nick.test.SimpleTest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Тест на время реакции 2 - движение
 *
 * @author Николай Жмакин
 * @since 06.02.2025
 */
@Component
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
		return "Ваше среднее отклонение при периоде вращения круга<br>" +
			"в 2.0с: " + reactionTestTwoDataList.get(0).getAverageReactionTime() + " мс.<br>" +
			"в 1.5с: " + reactionTestTwoDataList.get(1).getAverageReactionTime() + " мс.<br>" +
			"в 1.1с: " + reactionTestTwoDataList.get(2).getAverageReactionTime() + " мс.<br>" +
			"в 0.9с: " + reactionTestTwoDataList.get(3).getAverageReactionTime() + " мс.<br>" +
			"в 0.7с: " + reactionTestTwoDataList.get(4).getAverageReactionTime() + " мс.<br>";
	}

	public void delete() {
		reactionTestTwo = null;
	}

	@Override
	public int getTestId() {
		return 8;
	}
}
