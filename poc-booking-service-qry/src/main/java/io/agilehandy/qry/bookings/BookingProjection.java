/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.agilehandy.qry.bookings;

import io.agilehandy.common.api.events.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.model.BookingStatus;
import lombok.extern.log4j.Log4j2;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author Haytham Mohamed
 **/

@Component
@EnableBinding(Sink.class)
@Log4j2
public class BookingProjection {

	private final BookingRepository repository;

	public BookingProjection(BookingRepository repository) {
		this.repository = repository;
	}

	@StreamListener(target = Sink.INPUT,
			condition = "headers['event_type']=='BOOKING_CREATED'")
	public void bookingCreatedProjection(@Payload BookingCreatedEvent event) {
		log.info("Projecting a new Booking entity");
		Booking booking = new Booking();
		booking.setBookingId(event.getBookingId());
		booking.setCustomerId(event.getCustomerId());
		booking.setStatus(BookingStatus.NEW);
		booking.setLastUpdateDate(event.getOccurredOn());
		booking.setStatusDate(event.getOccurredOn());
		repository.save(booking);
	}

}
