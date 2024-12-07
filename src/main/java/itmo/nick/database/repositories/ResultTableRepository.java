package itmo.nick.database.repositories;

import itmo.nick.database.entities.ResultTable;
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

}
