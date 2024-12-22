package itmo.nick.test.memory;

/**
 * Данные для теста на память 1
 *
 * @author Николай Жмакин
 * @since 22.12.2024
 */
public class MemoryTestOneData {
	private final String[] pictureSequence;

	private int currentPict = 0;

	public MemoryTestOneData() {
		pictureSequence = getNewSequence();
	}

	private String[] getNewSequence() {
		return new String[] {"25","1","19","5","15","12","2","6","20","3","23","4","5","6","14","7","8","17","9","24",
			"10","21","11","10","12","13","2","14","15","9","16","17","18","19","20","4","21","11","3","22","23","16",
			"24","1","25","7","8","13","18","22"};
	}

	/**
	 * Получить номер картинки
	 */
	public String getNextPicture() {
		if (currentPict >= pictureSequence.length) {
			currentPict = 0;
		}
		String picture = pictureSequence[currentPict];
		currentPict++;
		return picture;
	}
}
