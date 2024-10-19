package itmo.nick.person;

import lombok.*;

/**
 * Первичная оценка состояния участника
 *
 * @author Николай Жмакин
 * @since 19.10.2024
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString
public class PersonState {
	/**
	 *  Максиальная радость - 1
	 *  Максимальное спосойствие - 1
	 *  Максимальное доминирование - 1
	 */
	private final String joy;
	private final String calm;
	private final String dominance;
}
