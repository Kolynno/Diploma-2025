package itmo.nick.test.memory;

import itmo.nick.test.SimpleTest;
import itmo.nick.test.attention.AttentionTestOne;

/**
 * Memtrax - тест на память 1
 *
 * @author Николай Жмакин
 * @since 22.12.2024
 */
public class MemoryTestOne extends SimpleTest {

	static MemoryTestOne memoryTestOne;
	private final MemoryTestOneData memoryTestOneData;

	protected MemoryTestOne() {
		super(3);
		memoryTestOneData = new MemoryTestOneData();
	}

	public static MemoryTestOne getInstance() {
		if (memoryTestOne == null) {
			memoryTestOne = new MemoryTestOne();

			return memoryTestOne;
		} else {
			return memoryTestOne;
		}
	}

	public String getNextPicture() {
		return memoryTestOneData.getNextPicture();
	}
}
