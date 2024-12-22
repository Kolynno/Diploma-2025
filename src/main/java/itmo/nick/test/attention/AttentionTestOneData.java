package itmo.nick.test.attention;

import lombok.*;

/**
 * Данные этапа
 * Время и кол-во ошибок
 *
 * @author Николай Жмакин
 * @since 17.11.2024
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString

public class AttentionTestOneData {
	private final Integer stage;
	private final Double time;
	private final Integer errors;
}
