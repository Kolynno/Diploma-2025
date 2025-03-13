package itmo.nick.database.repositories;

import itmo.nick.database.entities.ResultTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


/**
 * Репозиторий результатов
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */

@Repository
public interface ResultTableRepository extends CrudRepository<ResultTable, Integer> {

	/**
	 * Поиск кол-ва прохождений определного теста по текущему пользователю.
	 * @return кол-во прохождений теста (0 или 1)
	 */
	@Query(
		"SELECT COUNT(result_id) " +
		"FROM ResultTable " +
		"WHERE person_id = :personId AND test_id = :testId AND result_date = CURRENT_DATE")
	String getStatus(String personId, String testId);

	/**
	 * Дата прохождения самого первого теста
	 */
	@Query("SELECT MIN(result_date) FROM ResultTable")
	LocalDate getFirstDateResult();

	/**
	 * Дата проходения крайнего теста
	 */
	@Query("SELECT MAX(result_date) FROM ResultTable")
	LocalDate getLastDateResult();
}
