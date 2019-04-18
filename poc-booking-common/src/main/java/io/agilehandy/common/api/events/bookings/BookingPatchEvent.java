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


package io.agilehandy.common.api.events.bookings;

import java.time.LocalDateTime;
import java.util.Map;

import io.agilehandy.common.api.events.BookingBaseEvent;
import io.agilehandy.common.api.events.BookingEvent;
import io.agilehandy.common.api.events.EventTypes;
import lombok.Data;

/**
 * @author Haytham Mohamed
 **/

@Data
public class BookingPatchEvent extends BookingBaseEvent implements BookingEvent {

	private Map<String, Object> data;

	public BookingPatchEvent(){}

	public static class Builder {
		private BookingPatchEvent eventToBuild;

		public Builder() {
			eventToBuild = new BookingPatchEvent();
		}

		public BookingPatchEvent build() {
			eventToBuild.setOccurredOn(LocalDateTime.now());
			eventToBuild.setType(EventTypes.BOOKING_UPDATED);
			BookingPatchEvent eventBuilt = eventToBuild;
			eventToBuild = new BookingPatchEvent();
			return eventBuilt;
		}

		public Builder setBookingId(String bookingId) {
			eventToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setData(Map<String, Object> data) {
			eventToBuild.setData(data);
			return this;
		}
	}

}
