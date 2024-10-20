package itmo.nick.test;

/**
 *
 * @author Николай Жмакин
 * @since 20.10.2024
 */
public class SimpleTest {
	/**
	 * Текущий этап участника
	 */
	int currentStage;
	/**
	 * Номер последнего этапа
	 */
	final int lastStage;
	/**
	 * Дошел ли участник до результата
	 */
	//TODO брать это поле из БД по id участника
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
		isLastStage(stage);
		return stage;
	}

	private void isLastStage(int stage) {
		if (stage == lastStage) {
			isFinished = true;
		}
	}
}
