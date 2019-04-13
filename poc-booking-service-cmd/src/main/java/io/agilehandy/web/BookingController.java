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

import java.util.UUID;

import io.agilehandy.bookings.BookingCreateCommand;
import io.agilehandy.bookings.BookingPatchCommand;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham Mohamed
 **/

@RestController
public class BookingController {

	private final BookingService service;

	public BookingController(BookingService service) {
		this.service = service;
	}

	@PostMapping
	public String createBooking(@RequestBody BookingCreateCommand cmd) {
		// temporary assign a random customer id. this should come with the request.
		if (cmd.getCustomerId() == null) {
			cmd.setCustomerId(UUID.randomUUID().toString());
		}
		return service.createBooking(cmd);
	}

	/*@GetMapping("/{id}")  // this GET, should not be here.
	public Booking getBooking(@PathVariable String id) {
		return service.getBookingById(id);
	}*/

	@PatchMapping("/{id}")
	public boolean pathBookingStatus(@PathVariable String id,
	                                @RequestBody BookingPatchCommand cmd) {
		return service.patchBooking(cmd);
	}
}
