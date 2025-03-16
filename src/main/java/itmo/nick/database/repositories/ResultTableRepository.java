package itmo.nick.database.repositories;

import itmo.nick.database.entities.ResultTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;


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

	@Query("SELECT COUNT(result_date) FROM ResultTable WHERE person_id = :personId")
	int getTestsCount(String personId);

	@Query("SELECT p1 FROM ResultTable WHERE person_id = :personId AND result_date = :date")
	String getP1(String personId, LocalDate date);

	@Query("SELECT p2 FROM ResultTable WHERE person_id = :personId AND result_date = :date")
	String getP2(String personId, LocalDate date);

	@Query("SELECT p3 FROM ResultTable WHERE person_id = :personId AND result_date = :date")
	String getP3(String personId, LocalDate date);

	@Query("SELECT p4 FROM ResultTable WHERE person_id = :personId AND result_date = :date")
	String getP4(String personId, LocalDate date);

	@Query("SELECT p5 FROM ResultTable WHERE person_id = :personId AND result_date = :date")
	String getP5(String personId, LocalDate date);

	@Query("SELECT result_date FROM ResultTable WHERE person_id = :personId ORDER BY result_date")
	LinkedList<LocalDate> getTestDates(String personId);

	//todo не всегда мниимальное - это лучшее
	@Query(value = "SELECT MIN(CAST(p1 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestP1(String testId);

	@Query(value = "SELECT MIN(CAST(p2 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestP2(String testId);

	@Query(value = "SELECT MIN(CAST(p3 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestP3(String testId);

	@Query(value = "SELECT MIN(CAST(p4 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestP4(String testId);

	@Query(value = "SELECT MIN(CAST(p5 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestP5(String testId);
}
