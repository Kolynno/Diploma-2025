package itmo.nick.test.memory;

import itmo.nick.test.SimpleTest;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Заучивание 10 слов - тест на память 2
 *
 * @author Николай Жмакин
 * @since 04.02.2025
 */
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

	protected MemoryTestTwo() {
		super(LAST_STAGE);
		memoryTestTwoData = new MemoryTestTwoData[5];
		wordToCountMap = new LinkedHashMap<>();
		wordToCountMap.put("Жук", 0);
		wordToCountMap.put("Мальчик", 0);
		wordToCountMap.put("Арка", 0);
		wordToCountMap.put("Колесо", 0);
		wordToCountMap.put("Индюк", 0);
		wordToCountMap.put("Носок", 0);
		wordToCountMap.put("Костер", 0);
		wordToCountMap.put("Официант", 0);
		wordToCountMap.put("Лампочка", 0);
		wordToCountMap.put("Ягода", 0);
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
		return wordToCountMap.toString();
	}



	public void delete() {
		memoryTestTwo = null;
	}


	public void updateMap(MemoryTestTwoData data) {
		for (String word : data.mentionWordsSet) {
			wordToCountMap.put(word, wordToCountMap.getOrDefault(word, 0) + 1);
		}
	}

}