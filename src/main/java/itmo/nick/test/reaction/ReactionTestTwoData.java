package itmo.nick.test.reaction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Данные для теста на реакцию 2 - движение
 *
 * @author Николай Жмакин
 * @since 06.02.2025
 */
public class ReactionTestTwoData {
	private ArrayList<Double> reactions;

	public ReactionTestTwoData() {
		reactions = new ArrayList<>();
	}

	public void addReaction(Double[] reactionTime) {
		reactions.addAll(Arrays.asList(reactionTime));
		System.out.println(reactions);
	}

	public double getAverageReactionTime() {
		return reactions.stream().mapToDouble(reactionTime -> reactionTime).sum() / reactions.size();
	}

}
