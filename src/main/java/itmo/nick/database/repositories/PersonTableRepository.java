package itmo.nick.database.repositories;

import itmo.nick.database.entities.PersonTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Репозиторий участника
 *
 * @author Николай Жмакин
 * @since 01.12.2024
 */

@Repository
public interface PersonTableRepository extends CrudRepository<PersonTable, Integer> {

	/**
	 * Получить максимальный идентификтор участника - последний зарегистрированный участики = текущий участник
	 * @return ID число
	 */
	@Query("SELECT MAX(person_id) FROM PersonTable")
	String findCurrentPersonId();
}
