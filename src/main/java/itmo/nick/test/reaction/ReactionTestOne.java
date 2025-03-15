package itmo.nick.test.reaction;

import itmo.nick.test.SimpleTest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Тест на время реакции 1 - статичное
 *
 * @author Николай Жмакин
 * @since 29.12.2024
 */
@Component
public class ReactionTestOne extends SimpleTest {

	static ReactionTestOne reactionTestOne;
	private ArrayList<ReactionTestOneData> reactionTestOneDataList = new ArrayList<>();

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	private static int TEST_TIME_IN_SEC = 10;
	private static int TEST_TIME_IN_MS = TEST_TIME_IN_SEC * 1000;
	private static double ERROR_TIME_IN_SEC = 0.5;

	protected ReactionTestOne() {
		super(LAST_STAGE);
	}

	public void addData(ReactionTestOneData data) {
		reactionTestOneDataList.add(data);
	}

	public static ReactionTestOne getInstance() {
		if (reactionTestOne == null) {
			reactionTestOne = new ReactionTestOne();

			return reactionTestOne;
		} else {
			return reactionTestOne;
		}
	}

	@Override
	public String result(String[] original) {
		return "Среднее время реакции бодрого человека " + original[0] + " мс. <br>при " + original[1] +
			" пропусков (реакция более 500мс) и " + original[2] + " преждевременных нажатий <br>У человека " +
			"с недостатком сна среднее время реакции " + original[3] + " мс.<br>при " + original[4] +
			" пропусков и " + original[5] + " преждевременных нажатий<br>Ваш результат " +
			String.valueOf(getReactionTime()).substring(0,5) + " c. - скорость реакции, количество ошибок = "
			+ getErrors() + ", ложных нажатий = " + getFalseStarts();
	}

	public String getFalseStarts() {
		return String.valueOf(reactionTestOneDataList.stream()
			.mapToDouble(ReactionTestOneData::getDelay)
			.filter(time -> time == 0).count());
	}

	public long getErrors() {
		return reactionTestOneDataList.stream()
			.mapToDouble(ReactionTestOneData::getReactionTime)
			.filter(time -> time > ERROR_TIME_IN_SEC).count();
	}

	public double getReactionTime() {
		double total = reactionTestOneDataList.stream().mapToDouble(ReactionTestOneData::getReactionTime).sum();
		return total/reactionTestOneDataList.stream().filter(time -> time.getReactionTime() != 0).count();
	}

	public void delete() {
		reactionTestOne = null;
	}

	public boolean timeIsUp() {
		double totalTime = reactionTestOneDataList.stream()
			.mapToDouble(data -> data.getReactionTime() + data.getDelay()).sum();
		return totalTime > TEST_TIME_IN_MS;
	}

	public ArrayList<ReactionTestOneData> getReactionTestOneDataList() {
		return reactionTestOneDataList;
	}

	@Override
	public int getTestId() {
		return 3;
	}
}
