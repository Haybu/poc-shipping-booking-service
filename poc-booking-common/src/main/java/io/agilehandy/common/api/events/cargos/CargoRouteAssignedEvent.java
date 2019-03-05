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


package io.agilehandy.common.api.events.cargos;

import io.agilehandy.common.api.events.BookingEvent;
import io.agilehandy.common.api.events.CargoBaseEvent;
import io.agilehandy.common.api.events.EventTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class CargoRouteAssignedEvent extends CargoBaseEvent implements BookingEvent {

	private String routeId;

	public static class Builder {
		private CargoRouteAssignedEvent eventToBuild;

		public Builder() {
			eventToBuild = new CargoRouteAssignedEvent();
		}

		public CargoRouteAssignedEvent build() {
			eventToBuild.setOccurredOn(LocalDateTime.now());
			eventToBuild.setType(EventTypes.CARGO_ROUTE_ASSIGNED);
			CargoRouteAssignedEvent eventBuilt = eventToBuild;
			eventToBuild = new CargoRouteAssignedEvent();
			return eventBuilt;
		}

		public Builder setBookingId(String bookingId) {
			eventToBuild.setBookingId(bookingId);
			return this;
		}

		public Builder setCargoId(String cargoId) {
			eventToBuild.setCargoId(cargoId);
			return this;
		}

		public Builder setRouteId(String routeId) {
			eventToBuild.setRouteId(routeId);
			return this;
		}
	}
}
