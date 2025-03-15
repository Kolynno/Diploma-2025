package itmo.nick.test.processing;

import itmo.nick.test.SimpleTest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Тест на обработку информации 2 - звуковой
 *
 * @author Николай Жмакин
 * @since 14.01.2025
 */
@Component
public class ProcessingTestTwo extends SimpleTest {

	private final static String GOAL = "1";
	private final static String NOT_GOAL = "0";

	static ProcessingTestTwo processingTestOne;

	private ArrayList<ProcessingTestTwoData> dataList;

	private int currentTone = 0;
	/**
	 * 1 - нажатие на пробел (целевой звук)
	 * 0 - ошибка (нецелевой звук)
	 */
	private String[] toneSequence;
	private int goalTones;

	private int skipErrors = 0;
	private int simpleErrors = 0;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestTwo() {
		super(LAST_STAGE);
		toneSequence = new String[] {GOAL, GOAL, GOAL, NOT_GOAL, NOT_GOAL, GOAL, GOAL, GOAL, NOT_GOAL};
		goalTones = (int) Arrays.stream(toneSequence).filter(tone -> tone.equals(GOAL)).count();
		dataList = new ArrayList<>();
	}

	public static ProcessingTestTwo getInstance() {
		if (processingTestOne == null) {
			processingTestOne = new ProcessingTestTwo();
			return processingTestOne;
		} else {
			return processingTestOne;
		}
	}

	@Override
	public String result(String[] original) {
		calculateErrors();
		return "Ошибки в оригинальном тесте у здоровых людей: " + original[0]
			+ "% пропусков целевого звука, " + original[1] + "% долгой реакции на целевой звук. Время реакции " + original[2] + " мс.<br>" +
			"Ошибки в оригинальном тесте у людей с шизофренией: " + original[3]
			+ "% пропусков целевого звука, " + original[4] + "% долгой реакции на целевой звук. Время реакции " + original[5] + " мс.<br>" +
			"Ваш результат: " + getSkipErrors() + "% пропусков целевого звука, " + getErrors()
			+ "% долгой реакции на целевой звук. Время реакции " + getReactionTime() + " мс.";
	}

	/**
	 * Подсчет ошибок в тесте
	 */
	private void calculateErrors() {
		for (int i = 0; i < dataList.size(); i++) {
			String correctAnswer = toneSequence[i];
			double answerTime = dataList.get(i).getReactionTime();
			if (correctAnswer.equals(GOAL)) {
				//целевой
				if (answerTime < 600) {
					//успел
				} else if (answerTime >= 600 && answerTime < 1900){
					//опоздание ответа
					skipErrors++;
				} else if (answerTime >= 1900) {
					//пропустил целевой
					simpleErrors++;
					dataList.get(i).setReactionTime(0);
					//то убрать время реакций на пропуск
				}
			} else {
				//нецелевой
				if (answerTime < 2000) {
					//и был ответ
					dataList.get(i).setReactionTime(0);
					//то убрать время реакций на неверный
				}
			}
		}

	}

	/**
	 * Время реакции в мс
	 */
	public String getReactionTime() {
		Double value = dataList
			.stream()
			.mapToDouble(ProcessingTestTwoData::getReactionTime)
			.filter(time -> time > 0)
			.average()
			.getAsDouble();
		return value == null ? "0" : String.valueOf(Math.round(value));
	}

	/**
	 * Пропуск целевого
	 */
	public String getErrors() {
		return String.valueOf(Math.round((double) simpleErrors/(goalTones)*100.0));
	}

	/**
	 * Долгий ответ на целевой
	 */
	public String getSkipErrors() {
		return String.valueOf(Math.round((double) skipErrors /(goalTones)*100.0));
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextTone() {
		if (currentTone < toneSequence.length) {
			int index= currentTone;
			currentTone++;
			return toneSequence[index];
		}
		return "-1";
	}

	public void addData(ProcessingTestTwoData data) {
		dataList.add(data);
	}

	@Override
	public int getTestId() {
		return 5;
	}
}
