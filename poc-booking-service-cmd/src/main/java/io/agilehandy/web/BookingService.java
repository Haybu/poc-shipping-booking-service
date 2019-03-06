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


package io.agilehandy.web;

import io.agilehandy.bookings.Booking;
import io.agilehandy.bookings.BookingChangeStatusCommand;
import io.agilehandy.bookings.BookingCreateCommand;
import io.agilehandy.bookings.BookingPatchCommand;
import io.agilehandy.cargos.Cargo;
import io.agilehandy.cargos.CargoAddCommand;
import io.agilehandy.common.api.exceptions.BookingNotFoundException;
import io.agilehandy.common.api.model.BookingStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/

@Service
public class BookingService {

	private final BookingRepository repository;

	public BookingService(BookingRepository repository) {
		this.repository = repository;
	}

	// create summary and associated requested cargos
	public String createBooking(BookingCreateCommand cmd) {
		Booking booking = new Booking(cmd);
		// attach requested cargos
		booking.attachCargos(cmd.getCargoRequests());
		repository.save(booking);
		return booking.getId().toString();
	}

	public boolean patchBooking(BookingPatchCommand cmd) {
		if (cmd.getData().containsKey(Booking.STATUS_FIELD_NAME)) { // status change
			return changeBookingStatus(cmd);
		}
		return updateStatusAttributes(cmd);
	}

	private boolean updateStatusAttributes(BookingPatchCommand cmd) {
		// TODO: summary to change attributes in cmd data bag.
		return true;
	}

	private boolean changeBookingStatus(BookingPatchCommand cmd) {
		Booking booking = repository.findById(cmd.getBookingId());
		BookingChangeStatusCommand statusChangeCommand =
				new BookingChangeStatusCommand.Builder()
						.setBookingId(cmd.getBookingId())
						.setStatus(BookingStatus
								.fromValue((String)cmd.getData().get(Booking.STATUS_FIELD_NAME)))
						.build();
		booking.changeStatus(statusChangeCommand);
		return true;
	}

	public Booking getBookingById(String bookingId) {
		Booking booking = repository.findById(bookingId.toString());
		if (booking == null) {
			throw new BookingNotFoundException(
					String.format("Booking with id %s is not found", bookingId));
		}
		return  booking;
	}

	public String addCargo(CargoAddCommand cmd) {
		Booking booking = this.getBookingById(cmd.getBookingId());
		String cargoId = booking.addCargo(cmd).toString();
		repository.save(booking);
		return cargoId;
	}

	public List<Cargo> getCargos(String bookingId) {
		return this.getBookingById(bookingId).getCargoList();
	}

	public Cargo getCargo(String bookingId, String cargoId) {
		Booking booking = this.getBookingById(bookingId);
		return booking.getCargo(UUID.fromString(cargoId));
	}

}
