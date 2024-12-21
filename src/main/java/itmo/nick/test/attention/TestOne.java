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

	public String resultSec(String[] original) {
		double stage1 = (double) Math.round((testOne.getTestData()[1].getTime() - Double.parseDouble(original[0])) * 100) /100;
		String stage1Text = stage1 < 0 ?
			"вы быстрее на " + Math.abs(stage1) + "с."
			: "вы медленне на " + stage1 + "с.";

		double stage2 = (double) Math.round((testOne.getTestData()[2].getTime() - Double.parseDouble(original[1])) * 100) /100;
		String stage2Text = stage2 < 0 ?
			"вы быстрее на " + Math.abs(stage2) + "с."
			: "вы медленне на " + stage2 + "с.";

//todo удвоенное среднее время, а не просто на 2
		double stage3 = (double) Math.round((testOne.getTestData()[3].getTime() + testOne.getTestData()[3].getErrors() * 2 - Double.parseDouble(original[2])) * 100) /100;
		String stage3Text = stage3 < 0 ?
			"вы быстрее на " + Math.abs(stage3) + "с."
			: "вы медленне на " + stage3 + "с.";

		double stage4 = (double) Math.round((testOne.getTestData()[4].getTime() + testOne.getTestData()[4].getErrors() * 2 - Double.parseDouble(original[3])) * 100) /100;
		String stage4Text = stage4 < 0 ?
			"вы быстрее на " + Math.abs(stage4) + "с."
			: "вы медленне на " + stage4 + "с.";

		return "Этап 1: " + stage1Text + "<br>Этап 2: " + stage2Text + "<br>Этап 3: " + stage3Text + "<br>Этап 4: " + stage4Text;
	}
}
