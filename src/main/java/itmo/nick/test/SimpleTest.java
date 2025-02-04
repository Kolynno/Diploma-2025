package itmo.nick.test;

import itmo.nick.test.attention.*;
import itmo.nick.test.memory.*;
import itmo.nick.test.processing.*;
import itmo.nick.test.reaction.*;

/**
 *
 * @author Николай Жмакин
 * @since 20.10.2024
 */
public class SimpleTest {
	/**
	 * Общее кол-во тестов
	 */
	public static final int TEST_AMOUNT = 7;
	/**
	 * Номер первого этапа - описание теста
	 */
	public static final int DESCRIPTION_STAGE = 0;
	/**
	 * Текущий этап участника
	 */
	int currentStage;
	/**
	 * Номер последнего этапа
	 */
	public final int lastStage;
	/**
	 * Завершен ли тест участником
	 */
	boolean isFinished;

	protected SimpleTest(int lastStage) {
		this.lastStage = lastStage;
	}

	/**
	 * Получает этап, на которых хочет перейти
	 * Если этап доступен, то возвращает,
	 * иначе корректирует его и возвращает доступный
	 */
	public int CorrectNextStage(int stage) {
		if (isFinished) {
			return lastStage;
		}
		if (currentStage + 1 == stage && stage <= lastStage) {
			return nextStage(stage);
		} else if (currentStage - 1 == stage && stage >= 0) {
			return previousStage(stage);
		} else {
			return currentStage;
		}
	}

	private int previousStage(int stage) {
		currentStage--;
		return stage;
	}

	private int nextStage(int stage) {
		currentStage++;

		return stage;
	}

	public void setFinished(boolean finished) {
		isFinished = finished;
		currentStage = 0;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public String result(String[] original) {
		return "SimpleTest";
	}

	/**
	 * Очистка данных
	 */
	public static void deleteTestAndData() {
		AttentionTestOne.getInstance().delete();
		AttentionTestTwo.getInstance().delete();
		MemoryTestOne.getInstance().delete();
		MemoryTestTwo.getInstance().delete();
		ReactionTestOne.getInstance().delete();

		ProcessingTestOne.getInstance().delete();
		ProcessingTestTwo.getInstance().delete();
	}
}
