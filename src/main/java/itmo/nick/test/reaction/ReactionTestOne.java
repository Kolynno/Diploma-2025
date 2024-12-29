package itmo.nick.test.reaction;

import itmo.nick.test.SimpleTest;
import itmo.nick.test.memory.MemoryTestOneData;

/**
 * Тест на время реакции 1
 *
 * @author Николай Жмакин
 * @since 29.12.2024
 */
public class ReactionTestOne extends SimpleTest {

	static ReactionTestOne reactionTestOne;


	protected ReactionTestOne() {
		super(3);
	}

	public static ReactionTestOne getInstance() {
		if (reactionTestOne == null) {
			reactionTestOne = new ReactionTestOne();

			return reactionTestOne;
		} else {
			return reactionTestOne;
		}
	}

	public String result(String[] split) {
		return "Среднее время реакции бодрого человека " + split[0] + " мс. <br>при " + split[1] +
			" кол-ве пропусков (реакция более 500мс) и " + split[2] + " кол-ве преждевременных нажатий <br>У человека " +
			"с недостатком сна среднее время реакции " + split[3] + " мс.<br>при " + split[4] +
			" кол-ве пропусков и " + split[5] + "кол-ве преждевременных нажатий<br>Ваш результат " +
			"NUMBER мс. - скорость реакции при NUBMER кол-ве ошибок и NUMBER кол-ве ложных нажатий.";
	}

	public void delete() {
		reactionTestOne = null;
	}
}
