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
package io.agilehandy.qry.cargos;

import java.util.Optional;

import io.agilehandy.common.api.events.cargos.CargoAddedEvent;
import io.agilehandy.common.api.model.CargoStatus;
import io.agilehandy.qry.bookings.Booking;
import io.agilehandy.qry.bookings.BookingRepository;
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
public class CargoProjection {

	private final CargoRepository cargoRepository;
	private final BookingRepository bookingRepository;

	public CargoProjection(CargoRepository cargoRepository, BookingRepository bookingRepository) {
		this.cargoRepository = cargoRepository;
		this.bookingRepository = bookingRepository;
	}

	@StreamListener(target = Sink.INPUT,
			condition = "headers['event_type']=='CARGO_ADDED'")
	public void cargoAddedProjection(@Payload CargoAddedEvent event) {
		log.info("Projecting a new Cargo entity");
		Optional<Booking> optionalBooking = bookingRepository.findById(event.getBookingId());
		if (optionalBooking.isPresent()) {
			Booking booking = optionalBooking.get();
			Cargo cargo = new Cargo();
			cargo.setBooking(booking);
			cargo.setCargoId(event.getCargoId());
			cargo.setRequiredSize(event.getRequiredSize());
			cargo.setCutOffDate(event.getCutOffDate());
			cargo.setOriginFacility(event.getOrigin().getFacility());
			cargo.setOriginOpZone(event.getOrigin().getOpZone());
			cargo.setDestFacility(event.getDestination().getFacility());
			cargo.setDestOpZone(event.getDestination().getOpZone());
			cargo.setNature(event.getNature());
			cargo.setStatus(CargoStatus.NEW);
			cargo.setStatusDate(event.getOccurredOn());
			booking.addCargo(cargo);
			bookingRepository.save(booking);
			cargoRepository.save(cargo);
		}
	}

}
