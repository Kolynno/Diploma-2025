package itmo.nick.database;

import itmo.nick.database.entities.ResultTable;
import itmo.nick.database.repositories.PersonTableRepository;
import itmo.nick.database.repositories.ResultTableRepository;
import itmo.nick.test.attention.AttentionTestOneData;
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

	/**
	 * Сохранение результатов.
	 * Для 1 и 2: Время
	 * Для 3 и 4: Время + ошибки на удвоенное среднее время прочтения слова
	 * @param attentionTestOneData данные этапов
	 */
	public void saveTestOne(AttentionTestOneData[] attentionTestOneData) {
		ResultTable resultTable = new ResultTable();
		resultTable.setTest_id("1");
		resultTable.setPerson_id(personTableRepository.findCurrentPersonId());
		resultTable.setP1(String.valueOf(attentionTestOneData[1].getTime()));
		resultTable.setP2(String.valueOf(attentionTestOneData[2].getTime()));
		resultTable.setP3(String.valueOf(attentionTestOneData[3].getTime() + attentionTestOneData[3].getErrors() * 2 * attentionTestOneData[3].getTime()/100));
		resultTable.setP4(String.valueOf(attentionTestOneData[4].getTime() + attentionTestOneData[4].getErrors() * 2 * attentionTestOneData[4].getTime()/100));

		resultTableRepository.save(resultTable);
	}

	public boolean getStatus() {
		if ("0".equals(resultTableRepository.getStatus())) {
			return false;
		}
		return true;
	}
}
