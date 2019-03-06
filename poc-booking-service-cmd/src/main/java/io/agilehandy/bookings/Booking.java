/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.agilehandy.bookings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.agilehandy.cargos.Cargo;
import io.agilehandy.cargos.CargoAddCommand;
import io.agilehandy.common.api.events.BookingEvent;
import io.agilehandy.common.api.events.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.events.bookings.BookingPatchEvent;
import io.agilehandy.common.api.events.bookings.BookingStatusChangedEvent;
import io.agilehandy.common.api.events.cargos.CargoAddedEvent;
import io.agilehandy.common.api.exceptions.CargoNotFoundException;
import io.agilehandy.common.api.model.BookingStatus;
import io.agilehandy.common.api.model.CargoRequest;
import javaslang.API;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javaslang.API.*;
import static javaslang.Predicates.*;

/**
 * @author Haytham Mohamed
 **/

// Aggregate Root

@Data
@Slf4j
@NoArgsConstructor
public class Booking {

	public static final String STATUS_FIELD_NAME = "status";

	@JsonIgnore
	private List<BookingEvent> cache = new ArrayList<>();

	private UUID id;

	private UUID customerId;

	private List<Cargo> cargoList;

	private BookingStatus status;

	private LocalDateTime statusDate;

	private LocalDateTime lastUpdateDate;

	// new booking
	public Booking(BookingCreateCommand cmd) {
		// TODO: perform any invariant rules here
		UUID bookingId = UUID.randomUUID();
		BookingCreatedEvent event =
				new BookingCreatedEvent.Builder()
				.setBookingId(bookingId.toString())
				.setCustomerId(UUID.randomUUID().toString())
				.setCargoRequests(cmd.getCargoRequests())
				.build();

		this.bookingCreated(event);
	}

	// Create booking event Handler
	public Booking bookingCreated(BookingCreatedEvent event) {
		this.setId(UUID.fromString(event.getBookingId()));
		this.setStatus(BookingStatus.NEW);
		this.setStatusDate(event.getOccurredOn());
		this.setLastUpdateDate(event.getOccurredOn());
		this.setCustomerId(UUID.fromString(event.getCustomerId()));
		this.setCargoList(new ArrayList<>());
		this.cacheEvent(event);
		return this;
	}

	// Attaching a cargo to booking
	public void attachCargos(List<CargoRequest> requests) {
		// create cargo command for every requested cargo by customer
		if (requests != null && !requests.isEmpty()) {
			requests.stream().forEach(cargoRequest -> {
				CargoAddCommand cargoAddCommand =
						new CargoAddCommand.Builder()
								.setBookingId(this.getId().toString())
								.setRequiredSize(cargoRequest.getRequiredSize())
								.setNature(cargoRequest.getNature())
								.setCutOffDate(cargoRequest.getCutOffDate())
								.setOrigin(cargoRequest.getOrigin())
								.setDestination(cargoRequest.getDestination())
								.build();
				this.addCargo(cargoAddCommand);
			});
		}
	}

	// Add cargo
	public UUID addCargo(CargoAddCommand cmd) {
		// TODO: perform any invariant rules here
		UUID cargoId = UUID.randomUUID();
		CargoAddedEvent event =
				new CargoAddedEvent.Builder()
				.setBookingId(cmd.getBookingId())
				.setCargoId(cargoId.toString())
				.setNature(cmd.getNature())
				.setRequiredSize(cmd.getRequiredSize())
				.setOrigin(cmd.getOrigin())
				.setDestination(cmd.getDestination())
				.setCutOffDate(cmd.getCutOffDate())
				.build();
		this.cargoAdded(event);
		return cargoId;
	}

	// Add cargo event Handler
	public Booking cargoAdded(CargoAddedEvent event) {
		Cargo cargo = this.cargoMember(UUID.fromString(event.getCargoId()));
		cargo.cargoAdded(event);
		this.setLastUpdateDate(event.getOccurredOn());
		this.getCargoList().add(cargo);
		this.cacheEvent(event);
		return this;
	}

	// change status
	public void changeStatus(BookingChangeStatusCommand cmd) {
		// TODO: any business invariant checks go here
		BookingStatusChangedEvent event =
				new BookingStatusChangedEvent.Builder()
				.setBookingId(this.getId().toString())
				.setStatus(cmd.getStatus().getValue())
				.build()
				;

		this.statusChanged(event);
	}

	// Change status event handler
	public Booking statusChanged(BookingStatusChangedEvent event) {
		setStatus(BookingStatus.fromValue(event.getStatus()));
		setStatusDate(event.getOccurredOn());
		setLastUpdateDate(event.getOccurredOn());
		return this;
	}

	// patch booking with some attributes update
	public boolean updateBooking(BookingPatchCommand cmd) {
		// TODO: any business invariant checks go here
		BookingPatchEvent event =
				new BookingPatchEvent.Builder()
				.setBookingId(cmd.getBookingId())
				.setData(cmd.getData())
				.build();
		this.BookingUpdated(event);
		return true;
	}

	// path an update event handler
	private Booking BookingUpdated(BookingPatchEvent event)  {
		this.setLastUpdateDate(event.getOccurredOn());
		event.getData().entrySet().stream()
				.forEach(entry ->
						{
							try {
								FieldUtils.writeField(this, entry.getKey()
										, entry.getValue(), true);
							} catch (IllegalAccessException ex) {
								log.debug("cannot update booking!", ex);
							}
						});
		return this;
	}

	// Event Sourcing Handler (When replaying)
	public Booking handleEvent(BookingEvent event) {
		return API.Match(event).of(
				Case( $( instanceOf( BookingCreatedEvent.class ) ), this::bookingCreated)
				, Case( $( instanceOf( CargoAddedEvent.class ) ), this::cargoAdded)
		);
	}

	public void cacheEvent(BookingEvent event) {
		cache.add(event);
	}

	public void clearEventCache() {
		this.cache.clear();
	}

	public Cargo getCargo(UUID cargoId) {
		return cargoList.stream()
				.filter(c -> c.getId() == cargoId)
				.findFirst()
				.orElseThrow(() -> new CargoNotFoundException(
						String.format("No Cargo found with %s ", cargoId)));
	}

	public Cargo cargoMember(UUID cargoId) {
		Cargo cargo = getCargoList().stream()
				.filter(c -> c.getId().toString().equals(cargoId.toString()))
				.findFirst().orElse(new Cargo(cargoId));
		return cargo;
	}

}
