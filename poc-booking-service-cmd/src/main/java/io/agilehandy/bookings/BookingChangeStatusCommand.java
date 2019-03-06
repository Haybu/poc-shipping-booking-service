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

import io.agilehandy.common.api.model.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class BookingChangeStatusCommand implements Serializable {

	private String bookingId;
	private BookingStatus status;

	public static class Builder {

		private BookingChangeStatusCommand commandToBuild;

		public Builder() {
			commandToBuild = new BookingChangeStatusCommand();
		}

		public BookingChangeStatusCommand build() {
			BookingChangeStatusCommand commandBuilt = commandToBuild;
			commandToBuild = new BookingChangeStatusCommand();
			return commandBuilt;
		}

		public Builder setBookingId(String bookingId) {
			commandToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setStatus(BookingStatus status) {
			commandToBuild.setStatus(status);
			return this;
		}
	}
}
