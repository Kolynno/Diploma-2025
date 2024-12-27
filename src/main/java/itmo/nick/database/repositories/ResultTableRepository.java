package itmo.nick.database.repositories;

import itmo.nick.database.entities.ResultTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Репозиторий результатов
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */

@Repository
public interface ResultTableRepository extends CrudRepository<ResultTable, Integer> {

	@Query("SELECT COUNT(result_id) FROM ResultTable WHERE person_id = (SELECT CAST(MAX(person_id) AS char) FROM PersonTable) AND test_id = :testId")
	String getStatus(String testId);
}
