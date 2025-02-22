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
	 * @deprecated
	 * Получить максимальный идентификтор участника - последний зарегистрированный участики = текущий участник
	 * @return ID число
	 */
	@Query("SELECT MAX(person_id) FROM PersonTable")
	String findCurrentPersonId();

	@Query("SELECT COUNT(person_id) FROM PersonTable WHERE person_id = :loginId")
	String findPersonById(String loginId);

	@Query("SELECT fioInitials FROM PersonTable WHERE person_id = :loginId")
	String getFioInitials(String loginId);
}
