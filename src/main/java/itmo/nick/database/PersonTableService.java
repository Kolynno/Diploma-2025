package itmo.nick.database;

import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.repositories.PersonTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис участника
 *
 * @author Николай Жмакин
 * @since 01.12.2024
 */
@Service
public class PersonTableService {

	private final PersonTableRepository personTableRepository;

	@Autowired
	public PersonTableService(PersonTableRepository personTableRepository) {
		this.personTableRepository = personTableRepository;
	}

	public void save(PersonTable personTable) {
		personTableRepository.save(personTable);
	}

	public PersonTableRepository getPersonTableRepository() {
		return personTableRepository;
	}
}
