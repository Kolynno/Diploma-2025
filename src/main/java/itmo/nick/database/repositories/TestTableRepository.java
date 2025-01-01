package itmo.nick.database.repositories;

import itmo.nick.database.entities.TestTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Репозиторий тестов
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */

@Repository
public interface TestTableRepository extends CrudRepository<TestTable, Integer> {

	/**
	 * Найти имя теста по идентификатору
	 */
	@Query("SELECT test_name FROM TestTable WHERE test_id = :id")
	String findNameById(int id);

	/**
	 * Найти описание теста по идентификатору
	 */
	@Query("SELECT test_start_desc FROM TestTable WHERE test_id = :id")
	String findDescById(int id);

	/**
	 * Найти результаты оригинального теста по идентификатору
	 */
	@Query("SELECT test_original_result FROM TestTable WHERE test_id = :id")
	String findResultsById(int id);
}
