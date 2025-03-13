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

	@Query("SELECT fio FROM PersonTable WHERE person_id = :loginId")
	String getFio(String loginId);

	@Query("SELECT sex FROM PersonTable WHERE person_id = :loginId")
	String getSex(String loginId);

	@Query("SELECT education FROM PersonTable WHERE person_id = :loginId")
	String getEducation(String loginId);

	@Query("SELECT birthday FROM PersonTable WHERE person_id = :loginId")
	String getBirthday(String loginId);
}
