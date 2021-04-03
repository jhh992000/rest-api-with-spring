package me.hhjeong.restapiwithspring.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

	public void validation(EventDto eventDto, Errors errors) {
		if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
			errors.reject("wrongPrices", "Values prices are wrong");
		}

		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
			endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
			endEventDateTime.isBefore(eventDto.getBeginEventDateTime())) {
			errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is Wrong");
		}

		//TODO - beginEventDataTime
		//TODO - CloseEnrollmentDateTime

	}

}
