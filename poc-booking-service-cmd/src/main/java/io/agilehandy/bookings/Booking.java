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
import io.agilehandy.common.api.events.BookingEvent;
import io.agilehandy.common.api.events.bookings.BookingCreatedEvent;
import io.agilehandy.cargos.CargoAddCommand;
import io.agilehandy.common.api.events.cargos.CargoAddedEvent;
import io.agilehandy.common.api.exceptions.CargoNotFoundException;
import io.agilehandy.common.api.model.BookingStatus;
import io.agilehandy.common.api.model.CargoRequest;
import io.agilehandy.common.api.model.Location;
import io.agilehandy.common.api.model.RouteLeg;
import io.agilehandy.common.api.model.TransportationType;
import io.agilehandy.cargos.Cargo;
import javaslang.API;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static javaslang.API.*;
import static javaslang.Predicates.*;

/**
 * @author Haytham Mohamed
 **/

// Aggregate Root

@Data
@Slf4j
public class Booking {

	@JsonIgnore
	private List<BookingEvent> cache = new ArrayList<>();

	private UUID id;

	private UUID customerId;

	private List<Cargo> cargoList;

	private BookingStatus status;

	private LocalDateTime statusDate;

	public Booking() {}

	// Command Handler
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

		// attach requested cargos
		this.attachCargos(cmd.getCargoRequests());
	}

	private void attachCargos(List<CargoRequest> requests) {
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

	// EVENT SOURCE HANDLER
	public Booking bookingCreated(BookingCreatedEvent event) {
		this.setId(UUID.fromString(event.getBookingId()));
		this.setCustomerId(UUID.fromString(event.getCustomerId()));
		this.setCargoList(new ArrayList<>());
		this.cacheEvent(event);
		return this;
	}

	// Command Handler
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

	// EVENT SOURCE HANDLER
	public Booking cargoAdded(CargoAddedEvent event) {
		Cargo cargo = this.cargoMember(UUID.fromString(event.getCargoId()));
		cargo.cargoAdded(event);
		this.getCargoList().add(cargo);
		this.cacheEvent(event);
		return this;
	}

	private List<RouteLeg> findRouteLegs(Location origin, Location destination) {
		// TODO: call external service to get legs for a route
		// for this demo generate random route legs
		int numberOfLegs = 0;
		int min = 2;
		int max = 4;
		List<TransportationType> transTypes = Arrays.asList(TransportationType.TRUCK,
				TransportationType.VESSEL);
		List<RouteLeg> legs = new ArrayList<>();
		do {
			Location startLocation = new Location(
					"zone-" + new Random().nextInt(20)
					, "facility-" + new Random().nextInt(20));
			Location endLocation = new Location(
					"zone-" + new Random().nextInt(20)
					, "facility-" + new Random().nextInt(20));

			RouteLeg leg =
					new RouteLeg(startLocation, endLocation
							, transTypes.get(new Random().nextInt(transTypes.size())));
			legs.add(leg);

			numberOfLegs++;
		} while (numberOfLegs > (new Random().nextInt((max - min) + 1) + min));

		return legs;
	}

	// Replaying Event Sourcing Handler
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
