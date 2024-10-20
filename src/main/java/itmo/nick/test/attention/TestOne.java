package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;

/**
 * Внимательность
 * Тест 1
 *
 * @author Николай Жмакин
 * @since 20.10.2024
 */
@SuppressWarnings("StaticMethod")
public class TestOne extends SimpleTest {

	static TestOne testOne;

	private TestOne() {
		super(5);
	}

	public static TestOne getInstance() {
		if (testOne == null) {
			testOne = new TestOne();
			return testOne;
		} else {
			return testOne;
		}
	}

}
