package itmo.nick.test.processing;

import itmo.nick.test.SimpleTest;

import java.util.ArrayList;

/**
 * Тест на обработку информации 2
 *
 * @author Николай Жмакин
 * @since 14.01.2025
 */
public class ProcessingTestTwo extends SimpleTest {

	static ProcessingTestTwo processingTestOne;

	private ArrayList<ProcessingTestOneData> dataList;

	private int currentTone = 0;
	/**
	 * 1 - нажатие на пробел (целевой звук)
	 * 0 - ошибка (нецелевой звук)
	 */
	private String[] toneSequence;

	private int skipErrors = 0;
	private int simpleErrors = 0;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestTwo() {
		super(LAST_STAGE);
		toneSequence = new String[] {"1", "1", "1", "0", "0", "1", "1", "1", "0", "0"};
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
			+ "% ловушка, " + original[1] + "% простой. Время реакции " + original[2] + " мс.<br>" +
			"Ошибки в оригинальном тесте у людей с шизофренией: " + original[3]
			+ "% ловушка, " + original[4] + "% простой. Время реакции " + original[5] + " мс.<br>" +
			"Ваш результат: " + getSkipErrors() + " % ловушка, " + getSimpleErrors()
			+ " % простой. Время реакции " + getReactionTime() + " мс.";
	}

	/**
	 * Подсчет ошибок в тесте
	 */
	private void calculateErrors() {
		for (int i = 1; i < dataList.size(); i++) {
			String correctAnswer = toneSequence[i];
			double answerTime = dataList.get(i).getReactionTime();
			if (answerTime <= 600) {
				if ("1".equals(correctAnswer)) {
					//correct
				} else {
					simpleErrors++;
				}
			} else {
				if (answerTime > 2000) {
					if ("1".equals(correctAnswer)) {
						skipErrors++;
					} else {
						//correct
					}
				}
			}
		}
	}

	/**
	 * Время реакции в мс
	 */
	public String getReactionTime() {
		Double value = dataList.stream().mapToDouble(ProcessingTestOneData::getReactionTime).average().getAsDouble();
		return value == null ? "0" : String.valueOf(Math.round(value));
	}

	/**
	 * Кол-во простых ошибок, процент без знака процента
	 */
	public String getSimpleErrors() {
		return String.valueOf(Math.round((double) simpleErrors/(dataList.size() - 1)*100.0));
	}

	/**
	 * Кол-во ошибок, процент без знака процента
	 */
	public String getSkipErrors() {
		return String.valueOf(Math.round((double) skipErrors /(dataList.size() - 1)*100.0));
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextTone() {
		//Первый не участвует в сравнении, т.к. нулевая позиция - пустой.
		if (currentTone < toneSequence.length - 1) {
			return toneSequence[currentTone++];
		}

		return "-1";
	}

	public void addData(ProcessingTestOneData data) {
		dataList.add(data);
	}


}
