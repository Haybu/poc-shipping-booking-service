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


package io.agilehandy.common.api.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.agilehandy.common.api.events.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.events.bookings.BookingPatchEvent;
import io.agilehandy.common.api.events.bookings.BookingStatusChangedEvent;
import io.agilehandy.common.api.events.cargos.CargoAddedEvent;

/**
 * @author Haytham Mohamed
 **/

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "BOOKING_CREATED", value = BookingCreatedEvent.class),
		@JsonSubTypes.Type(name = "BOOKING_STATUS_CHANGED", value = BookingStatusChangedEvent.class),
		@JsonSubTypes.Type(name = "BOOKING_UPDATED", value = BookingPatchEvent.class),
		@JsonSubTypes.Type(name = "CARGO_ADDED", value = CargoAddedEvent.class)
})

public interface BookingEvent {

	public String getType();
	public String getBookingId();

}
