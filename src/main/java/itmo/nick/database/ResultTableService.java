package itmo.nick.database;

import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.entities.ResultTable;
import itmo.nick.database.repositories.PersonTableRepository;
import itmo.nick.database.repositories.ResultTableRepository;
import itmo.nick.test.attention.AttentionTestOneData;
import itmo.nick.test.attention.AttentionTestTwo;
import itmo.nick.test.processing.ProcessingTestOne;
import itmo.nick.test.processing.ProcessingTestTwo;
import itmo.nick.test.reaction.ReactionTestTwoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Сервис участника
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */
@Service
public class ResultTableService {

	private final ResultTableRepository resultTableRepository;
	private final PersonTableRepository personTableRepository;

	@Autowired
	public ResultTableService(ResultTableRepository resultTableRepository, PersonTableRepository personTableRepository) {
		this.resultTableRepository = resultTableRepository;
		this.personTableRepository = personTableRepository;
	}

	/**
	 * Сохранение результатов.
	 * Для 1 и 2: Время
	 * Для 3 и 4: Время + ошибки на удвоенное среднее время прочтения слова
	 * @param attentionTestOneData данные этапов
	 */
	public void saveTestOne(AttentionTestOneData[] attentionTestOneData) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("1");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(String.valueOf(attentionTestOneData[1].getTime()));
		resultTable.setP2(String.valueOf(attentionTestOneData[2].getTime()));
		resultTable.setP3(String.valueOf(attentionTestOneData[3].getTime() + attentionTestOneData[3].getErrors() * 2 * attentionTestOneData[3].getTime()/100));
		resultTable.setP4(String.valueOf(attentionTestOneData[4].getTime() + attentionTestOneData[4].getErrors() * 2 * attentionTestOneData[4].getTime()/100));

		resultTableRepository.save(resultTable);
	}

	public void saveTestTwo(double errors, double time) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("2");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(String.valueOf(errors));
		resultTable.setP2(String.valueOf(time));

		resultTableRepository.save(resultTable);
	}

	public void saveTestThree(String reactionTime, String errors, String falseStarts) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("3");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(reactionTime);
		resultTable.setP2(errors);
		resultTable.setP3(falseStarts);

		resultTableRepository.save(resultTable);
	}

	public void saveTestFourth(ProcessingTestOne processingTestOne) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("4");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(processingTestOne.getReactionTime());
		resultTable.setP2(processingTestOne.getErrors());
		resultTable.setP3(processingTestOne.getSimpleErrors());

		resultTableRepository.save(resultTable);
	}

	public void saveTestFive(ProcessingTestTwo processingTestTwo) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("5");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(processingTestTwo.getReactionTime());
		resultTable.setP2(processingTestTwo.getSkipErrors());
		resultTable.setP3(processingTestTwo.getErrors());

		resultTableRepository.save(resultTable);
	}

	public void saveTestSix(AttentionTestTwo attentionTestTwo) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("6");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(String.valueOf(Math.round(attentionTestTwo.getData().getTime())));

		resultTableRepository.save(resultTable);
	}

	public void saveTestSeven(LinkedList<Integer> correctWords) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("7");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(String.valueOf(correctWords.get(0)));
		resultTable.setP2(String.valueOf(correctWords.get(1)));
		resultTable.setP3(String.valueOf(correctWords.get(2)));
		resultTable.setP4(String.valueOf(correctWords.get(3)));
		resultTable.setP5(String.valueOf(correctWords.get(4)));

		resultTableRepository.save(resultTable);
	}

	public void saveTestEight(ArrayList<ReactionTestTwoData> reactionTestTwoDataList) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("8");
		resultTable.setPerson_id(getCurrentPersonId());

		resultTable.setP1(String.valueOf(reactionTestTwoDataList.get(0).getAverageReactionTime()));
		resultTable.setP2(String.valueOf(reactionTestTwoDataList.get(1).getAverageReactionTime()));
		resultTable.setP3(String.valueOf(reactionTestTwoDataList.get(2).getAverageReactionTime()));
		resultTable.setP4(String.valueOf(reactionTestTwoDataList.get(3).getAverageReactionTime()));
		resultTable.setP5(String.valueOf(reactionTestTwoDataList.get(4).getAverageReactionTime()));

		resultTableRepository.save(resultTable);
	}

	/**
	 * Статус прохождения теста
	 * @param testId идентификтор теста
	 * @return если есть прохождение, то стаутс true, иначе false
	 */
	public boolean getStatus(int testId) {
		if ("0".equals(resultTableRepository.getStatus(getCurrentPersonId(), String.valueOf(testId)))) {
			return false;
		}
		return true;
	}

	private String getCurrentPersonId() {
		return String.valueOf(PersonTable.getInstance().getPerson_id());
	}
}
