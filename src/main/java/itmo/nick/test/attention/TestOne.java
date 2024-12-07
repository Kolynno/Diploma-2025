package itmo.nick.test.attention;

import itmo.nick.test.SimpleTest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestOne extends SimpleTest {

	static TestOne testOne;

	private TestOneData[] testData = new TestOneData[5];
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
