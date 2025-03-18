package itmo.nick.test.memory;

import itmo.nick.test.SimpleTest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Заучивание 10 слов - тест на память 2
 *
 * @author Николай Жмакин
 * @since 04.02.2025
 */
@Component
public class MemoryTestTwo extends SimpleTest {

	static MemoryTestTwo memoryTestTwo;
	/**
	 * Номер последнего этап теста
	 */
	public static final int LAST_STAGE = 6;

	private MemoryTestTwoData[] memoryTestTwoData;

	private LinkedHashMap<String, Integer> wordToCountMap;

	public Set<String> getWords() {
		return wordToCountMap.keySet();
	}

	public LinkedHashMap<String, Integer> getWordToCountMap() {
		return wordToCountMap;
	}

	private LinkedList<Integer> correctWords = new LinkedList<>();

	private String[] originalWords = {
		"Жук",
		"Мальчик",
		"Арка",
		"Колесо",
		"Индюк",
		"Носок",
		"Костер",
		"Официант",
		"Лампочка",
		"Ягода"
	};

	protected MemoryTestTwo() {
		super(LAST_STAGE);
		memoryTestTwoData = new MemoryTestTwoData[5];
		wordToCountMap = new LinkedHashMap<>();
		for (int i = 0; i < originalWords.length; i++) {
			wordToCountMap.put(originalWords[i], 0);
		}
	}

	public static MemoryTestTwo getInstance() {
		if (memoryTestTwo == null) {
			memoryTestTwo = new MemoryTestTwo();

			return memoryTestTwo;
		} else {
			return memoryTestTwo;
		}
	}

	@Override
	public String result(String[] original) {
		return "Результаты в оригинальном тестировании: " + original[0] + ", " + original[1] + ", " + original[2] + ", "
			+ original[3] + ", " + original[4] + "\n" + " правильно названных слов по этапамю.\n" +
			"Ваш результат: " + getResult() + " соответсвенно.";
	}

	private String getResult() {
		return correctWords.get(0) + ", " + correctWords.get(1) + ", " + correctWords.get(2) + ", "
			+ correctWords.get(3) + ", " + correctWords.get(4);
	}


	public void delete() {
		memoryTestTwo = null;
	}


	public void updateMap(MemoryTestTwoData data) {
		int count = 0;
		for (String word : data.mentionWordsSet) {
			wordToCountMap.put(word, wordToCountMap.getOrDefault(word, 0) + 1);
			if (Arrays.asList(originalWords).contains(word)) {
				count++;
			}
		}
		correctWords.add(count);
	}

	public LinkedList<Integer> getCorrectWords() {
		return correctWords;
	}

	@Override
	public int getTestId() {
		return 7;
	}

	@Override
	public LinkedList<String> getTestInfo() {
		LinkedList<String> strings = new LinkedList<>();
		strings.add("Первый");
		return strings;
	}
}