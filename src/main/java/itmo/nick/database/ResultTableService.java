package itmo.nick.database;

import itmo.nick.database.entities.ResultTable;
import itmo.nick.database.repositories.PersonTableRepository;
import itmo.nick.database.repositories.ResultTableRepository;
import itmo.nick.test.attention.TestOneData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void saveTestOne(TestOneData[] testOneData) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("1");
		resultTable.setPerson_id(personTableRepository.findCurrentPersonId());
		//todo НЕ * 2
		resultTable.setP1(String.valueOf(testOneData[1].getTime() + testOneData[1].getErrors() * 2));
		resultTable.setP2(String.valueOf(testOneData[2].getTime() + testOneData[2].getErrors() * 2));
		resultTable.setP3(String.valueOf(testOneData[3].getTime() + testOneData[3].getErrors() * 2));
		resultTable.setP4(String.valueOf(testOneData[4].getTime() + testOneData[4].getErrors() * 2));

		resultTableRepository.save(resultTable);
	}

	public boolean getStatus(int id) {
		if ("0".equals(resultTableRepository.getStatus("1"))) {
			return false;
		} //TODO GET REAL ID
		return true;
	}
}
