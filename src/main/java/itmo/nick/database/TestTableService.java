package itmo.nick.database;

import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.entities.TestTable;
import itmo.nick.database.repositories.PersonTableRepository;
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

	@Autowired
	public TestTableService(TestTableRepository testTableRepository) {
		this.testTableRepository = testTableRepository;
	}

	public void save(TestTable testTable) {
		testTableRepository.save(testTable);
	}

	public String getNameById(int id) {
		return testTableRepository.findNameById(id);
	}
}
