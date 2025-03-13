package itmo.nick.database;

import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.repositories.PersonTableRepository;
import itmo.nick.person.Person;
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

	/**
	 * Есть ли пользователь с таким id
	 * @return true, если есть, иначе false
	 */
	public boolean isExist(Person person) {
		return !"0".equals(getPersonTableRepository().findPersonById(person.getLoginId()));
	}

	/**
	 * Сохранить данные логина в текущего участника
	 */
	public void saveIdToCurrentPerson(Person person) {
		PersonTable.getInstance().setPerson_id(Long.valueOf(person.getLoginId()));
	}

	public String getFio(String loginId) {
		return personTableRepository.getFio(loginId);
	}

	public String getSex(String loginId) {
		if ("male".equals(personTableRepository.getSex(loginId))) {
			return "мужской";
		} else {
			return "женский";
		}
	}

	public String getEducation(String loginId) {
		return personTableRepository.getEducation(loginId);
	}

	public String getBirthday(String loginId) {
		return personTableRepository.getBirthday(loginId);
	}
}
