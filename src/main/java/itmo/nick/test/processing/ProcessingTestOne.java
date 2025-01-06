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
	private String[] correctAnswerSequence;

	/**
	 * Номер последнего этап теста
	 */
	public final static int LAST_STAGE = 2;

	protected ProcessingTestOne() {
		super(LAST_STAGE);
		numbersSequence = new String[] {"11111", "11111", "11112", "11112", "22222", "22222", "22222", "12313", "23131", "11232"};
		correctAnswerSequence = new String[] {"0", "1", "0", "1", "0", "1", "1", "0", "0", "0"};
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
		return "Ошбики в оригинальном тесте у здоровых людей: " + split[0] + "% ловушка, " + split[1] + "% простой. Время реакции " + split[2] + " мс.<br>" +
			"Ошбики в оригинальном тесте у людей с шизофренией: " + split[3] + "% ловушка, " + split[4] + "% простой. Время реакции " + split[5] + " мс.<br>" +
			"Ваш результат: " + getErrors() + " % ловушка, " + getSimpleErrors() + " % простой. Время реакции " + getReactionTime() + " мс.";
	}

	private String getReactionTime() {
		return "RT";
	}

	private String getSimpleErrors() {
		return "SIMPLE";
	}

	private String getErrors() {
		return "ERRORS";
	}

	public void delete() {
		processingTestOne = null;
	}

	public String getNextNumbers() {
		//Первый не учавствует в сравнении, т.к. нулевая позиция - пустой.
		if (currentNumbers < numbersSequence.length - 1) {
			return numbersSequence[currentNumbers++];
		}

		return "-1";
	}

	public void addData(ProcessingTestOneData data) {
		dataList.add(data);
	}


}
