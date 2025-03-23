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

	/**
	 * Кол-во тестов {@code testNumber} у участника {@code personId}
	 */
	@Query("SELECT COUNT(result_date) FROM ResultTable WHERE person_id = :personId AND test_id = :testNumber")
	int getTestsCount(String personId, String testNumber);

	@Query("SELECT p1 FROM ResultTable WHERE person_id = :personId AND result_date = :date AND test_id = :testNumber")
	String getP1(String personId, LocalDate date, String testNumber);

	@Query("SELECT p2 FROM ResultTable WHERE person_id = :personId AND result_date = :date AND test_id = :testNumber")
	String getP2(String personId, LocalDate date, String testNumber);

	@Query("SELECT p3 FROM ResultTable WHERE person_id = :personId AND result_date = :date AND test_id = :testNumber")
	String getP3(String personId, LocalDate date, String testNumber);

	@Query("SELECT p4 FROM ResultTable WHERE person_id = :personId AND result_date = :date AND test_id = :testNumber")
	String getP4(String personId, LocalDate date, String testNumber);

	@Query("SELECT p5 FROM ResultTable WHERE person_id = :personId AND result_date = :date AND test_id = :testNumber")
	String getP5(String personId, LocalDate date, String testNumber);

	@Query("SELECT result_date FROM ResultTable WHERE person_id = :personId AND test_id = :testNumber ORDER BY result_date")
	LinkedList<LocalDate> getTestDates(String personId, String testNumber);

	/**
	 * Получить по {@code testId} лучшиее значение параметра (либо минимальное значение, лиюо макимальное)
	 */
	@Query(value = "SELECT MIN(CAST(p1 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMinP1(String testId);

	@Query(value = "SELECT MIN(CAST(p2 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMinP2(String testId);

	@Query(value = "SELECT MIN(CAST(p3 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMinP3(String testId);

	@Query(value = "SELECT MIN(CAST(p4 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMinP4(String testId);

	@Query(value = "SELECT MIN(CAST(p5 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMinP5(String testId);

	@Query(value = "SELECT MAX(CAST(p1 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMaxP1(String testId);

	@Query(value = "SELECT MAX(CAST(p2 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMaxP2(String testId);

	@Query(value = "SELECT MAX(CAST(p3 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMaxP3(String testId);

	@Query(value = "SELECT MAX(CAST(p4 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMaxP4(String testId);

	@Query(value = "SELECT MAX(CAST(p5 AS double precision)) FROM RESULT WHERE test_id = :testId", nativeQuery = true)
	String getBestMaxP5(String testId);
}
