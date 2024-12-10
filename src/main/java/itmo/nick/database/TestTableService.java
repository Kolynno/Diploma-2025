package itmo.nick.database;

import itmo.nick.database.entities.TestTable;
import itmo.nick.database.repositories.TestTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис тестов
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */
@Service
public class TestTableService {

	private final TestTableRepository testTableRepository;
	private final ResultTableService resultTableService;

	@Autowired
	public TestTableService(TestTableRepository testTableRepository, ResultTableService resultTableService) {
		this.testTableRepository = testTableRepository;
		this.resultTableService = resultTableService;
	}

	public void save(TestTable testTable) {
		testTableRepository.save(testTable);
	}

	public String getNameById(int id) {
		String name = testTableRepository.findNameById(id);
		boolean status = resultTableService.getStatus(id);
		if (status) {
			return name + " ✓";
		} else {
			return name;
		}
	}

	public String getDescById(int id) {
		return testTableRepository.findDescById(id);
	}

	public String getResultsById(int id) {
		return testTableRepository.findResultsById(id);
	}
}
