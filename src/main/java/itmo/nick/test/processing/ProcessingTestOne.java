package itmo.nick.test.processing;

import itmo.nick.test.SimpleTest;

import java.util.*;

/**
 * Тест на обработку информации 1
 *
 * @author Николай Жмакин
 * @since 03.01.2025
 */
public class ProcessingTestOne extends SimpleTest {

	static ProcessingTestOne processingTestOne;

	private ArrayList<ProcessingTestOneData> dataList;

	private int currentNumbers = 0;
	private String[] numbersSequence;
	/**
	 * 0  - простой
	 * 1  - нажатие на пробел (одинаковые числа)
	 * -1 - ловушка (только 1 цифра отличается)
	 */
	private String[] correctAnswerSequence;

	private int errors = 0;
	private int simpleErrors = 0;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestOne() {
		super(LAST_STAGE);
		numbersSequence = new String[] {"11111", "11111", "11112", "11112", "22222", "22222", "22222", "12313", "23131", "11232"};
		correctAnswerSequence = new String[] {"0", "1", "-1", "1", "0", "1", "1", "0", "0", "0"};
		dataList = new ArrayList<>();
	}

	public static ProcessingTestOne getInstance() {
		if (processingTestOne == null) {
			processingTestOne = new ProcessingTestOne();

			return processingTestOne;
		} else {
			return processingTestOne;
		}
	}

	public String result(String[] split) {
		calculateErrors();
		return "Ошбики в оригинальном тесте у здоровых людей: " + split[0] + "% ловушка, " + split[1] + "% простой. Время реакции " + split[2] + " мс.<br>" +
			"Ошбики в оригинальном тесте у людей с шизофренией: " + split[3] + "% ловушка, " + split[4] + "% простой. Время реакции " + split[5] + " мс.<br>" +
			"Ваш результат: " + getErrors() + " % ловушка, " + getSimpleErrors() + " % простой. Время реакции " + getReactionTime() + " мс.";
	}

	/**
	 * Подсчет ошибок в тесте
	 */
	private void calculateErrors() {
		for(int i = 1; i < dataList.size(); i++) {
			String correctAnswer = correctAnswerSequence[i];
			String userAnswer = dataList.get(i).getReactionTime() < 1000 ? "1" : "0";
			if ("1".equals(userAnswer)) {
				if ("0".equals(correctAnswer)) {
					simpleErrors++;
				} else if ("-1".equals(correctAnswer)) {
					errors++;
				}
			} else {
				//как я понял, если действия не было, но надо было, то это новый тип ошибок "Пропуск",
				//но он не учитывается при плсдчете реакции
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
	public String getErrors() {
		return String.valueOf(Math.round((double) errors/(dataList.size() - 1)*100.0));
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextNumbers() {
		//Первый не участвует в сравнении, т.к. нулевая позиция - пустой.
		if (currentNumbers < numbersSequence.length - 1) {
			return numbersSequence[currentNumbers++];
		}

		return "-1";
	}

	public void addData(ProcessingTestOneData data) {
		dataList.add(data);
	}


}
