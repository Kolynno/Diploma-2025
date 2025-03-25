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
	/**
	 * Все результаты теста
	 */
	protected LinkedList<Double> allResults = new LinkedList<>();
	/**
	 * Лучшие результаты теста
	 */
	protected LinkedList<Double> bestResults = new LinkedList<>();
	/**
	 * Результаты теста оригинального исследования
	 */
	protected LinkedList<Double> originalResults = new LinkedList<>();
	/**
	 * Лучшие результаты теста других участников
	 */
	protected LinkedList<Double> otherBestResults = new LinkedList<>();
	/**
	 * Кол-во результатов теста
	 */
	protected int testsCount = 0;
	/**
	 * На сколько % может быть различие допустимо. Если перейти порог, то обнуляется.
	 */
	public static final int MAX_CORRECT_DIFFERENCE_IN_PERCENTS = 1000;

	/**
	 * Итоговый процент расхождения от остальных
	 */
	public double otherPercentAvgSummary = 0;
	/**
	 * Итоговый процент расхождения от оригинальных
	 */
	public double originalPercentAvgSummary = 0;

	public static String BEST_AND_ORIGINAL_COMPARE_TEST_INFO =
		"П*С%,П*Э% – процент разности по параметрам между другими участниками (С) и оригинальным (Э) соответственно";

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
	public LinkedList<String> getAllPersonData(String personId) {
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
		LinkedList<String> strings = new LinkedList<>();
		setTablePercentCompare(originalResults, bestResults, otherBestResults, strings);
		return strings;
	}

	private void setTablePercentCompare(
		LinkedList<Double> originalResults,
		LinkedList<Double> bestResults,
		LinkedList<Double> otherBestResults,
		LinkedList<String> strings
	) {
		double otherPercentAvg = 0;
		double originalPercentAvg = 0;
		for (int i = 0; i < originalResults.size(); i++) {
			double percentOther = Math.round((bestResults.get(i) / otherBestResults.get(i) - 1) * 100) ;
			double percentOriginal = Math.round((bestResults.get(i) / originalResults.get(i) - 1) * 100);
			if (Math.abs(percentOther) > MAX_CORRECT_DIFFERENCE_IN_PERCENTS) {
				percentOther = 0;
			}
			if (Math.abs(percentOriginal) > MAX_CORRECT_DIFFERENCE_IN_PERCENTS) {
				percentOriginal = 0;
			}
			otherPercentAvg += percentOther;
			originalPercentAvg += percentOriginal;
			strings.add(percentOther > 0 ? "+" + percentOther : String.valueOf(percentOther));
			strings.add(percentOriginal > 0 ? "+" + percentOriginal : String.valueOf(percentOriginal));
		}

		otherPercentAvg /= originalResults.size();
		originalPercentAvg /= originalResults.size();

		if (Math.abs(otherPercentAvg) > MAX_CORRECT_DIFFERENCE_IN_PERCENTS) {
			otherPercentAvg = 0;
		}
		if (Math.abs(originalPercentAvg) > MAX_CORRECT_DIFFERENCE_IN_PERCENTS) {
			originalPercentAvg = 0;
		}

		otherPercentAvgSummary  = otherPercentAvg;
		originalPercentAvgSummary = originalPercentAvg;

		strings.add(otherPercentAvg > 0 ? "+" + otherPercentAvg : String.valueOf(otherPercentAvg));
		strings.add(originalPercentAvg > 0 ? "+" + originalPercentAvg : String.valueOf(originalPercentAvg));
	}

	/**
	 * Обобщенный итог
	 */
	public LinkedList<String> getSummary(String personId) {
		LinkedList<String> strings = new LinkedList<>();
		strings.add(otherPercentAvgSummary > 0 ? "+" + otherPercentAvgSummary : String.valueOf(otherPercentAvgSummary));
		strings.add(originalPercentAvgSummary > 0 ? "+" + originalPercentAvgSummary : String.valueOf(originalPercentAvgSummary));
		return strings;
	}

	public int getParamsCount() {
		return 0;
	}
}
