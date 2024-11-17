package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;

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
