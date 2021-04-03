package me.hhjeong.restapiwithspring.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"id", "name"}) // Entity를 지정하지 않는것이 좋다. 이유는 무한루프 stack overflow 발생
//@Data //이것도 사용 권장하지 않는다. 모든 엔티티 getter 생성하여 무한루프 발생가능.
public class Event {

	private Integer id;
	private String name;
	private String description;
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;
	private String location; // (optional) 이게 없으면 온라인 모임
	private int basePrice; // (optional)
	private int maxPrice; // (optional)
	private int limitOfEnrollment;
	private boolean offline;
	private boolean free;
	private EventStatus eventStatus;

}
