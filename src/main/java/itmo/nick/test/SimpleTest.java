package itmo.nick.test;

import itmo.nick.test.attention.*;
import itmo.nick.test.memory.*;
import itmo.nick.test.processing.*;
import itmo.nick.test.reaction.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;

/**
 * Родительский класс тестов
 *
 * @author Николай Жмакин
 * @since 20.10.2024
 */
public class SimpleTest {
	/**
	 * Общее кол-во тестов
	 */
	public static final int TEST_AMOUNT = 8;
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

	public SimpleTest(@Value("0") int lastStage) {
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
	public static void deleteTestsData() {
		AttentionTestOne.getInstance().delete();
		AttentionTestTwo.getInstance().delete();
		MemoryTestOne.getInstance().delete();
		MemoryTestTwo.getInstance().delete();
		ReactionTestOne.getInstance().delete();
		ReactionTestTwo.getInstance().delete();
		ProcessingTestOne.getInstance().delete();
		ProcessingTestTwo.getInstance().delete();
	}

	/**
	 * Возвращает id теста для идентификации
	 */
	public int getTestId() {
		return 0;
	}

	/**
	 * Название теста
	 */
	public String getTestName() {
		return "null";
	}

	/**
	 * Полезная информация для теста
	 */
	public LinkedList<String> getTestInfo() {
		return new LinkedList<>();
	}

	/**
	 * Данные для таблицы:
	 * Номер, Дата, (Значение пользователя, Значение других) х Кол-во пунктов
	 */
	public LinkedList<String> getAllPersonDataAndCompareToOther(String personId) {
		return new LinkedList<>();
	}

	/**
	 * Данные для таблицы:
	 * (Значение пользователя, Значение эталонного) х Кол-во пунктов
	 */
	public LinkedList<String> getBestPersonDataAndCompareToOriginal(String personId) {
		return new LinkedList<>();
	}

	/**
	 * Данные для таблицы:
	 * (Значение пользователя в % от дургих, Значение пользователя в % от эталонного) х Кол-во пунктов
	 */
	public LinkedList<String> getPercentCompareToOtherAndOriginal(String personId) {
		return new LinkedList<>();
	}

	/**
	 * Обобщенный итог
	 */
	public LinkedList<String> getSummary(String personId) {
		return new LinkedList<>();
	}
}
