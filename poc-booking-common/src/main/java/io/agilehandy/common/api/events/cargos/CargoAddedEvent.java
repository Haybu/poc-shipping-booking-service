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

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.events.BookingEvent;
import io.agilehandy.common.api.events.CargoBaseEvent;
import io.agilehandy.common.api.events.EventTypes;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.ContainerSize;
import io.agilehandy.common.api.model.Location;
import lombok.Data;

/**
 * @author Haytham Mohamed
 **/

@Data
public class CargoAddedEvent extends CargoBaseEvent implements BookingEvent {

	private Location origin;
	private Location destination;

	private CargoNature nature;
	private ContainerSize requiredSize;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cutOffDate;

	public CargoAddedEvent(){}

	public static class Builder {
		private CargoAddedEvent eventToBuild;

		public Builder() {
			eventToBuild = new CargoAddedEvent();
		}

		public CargoAddedEvent build() {
			eventToBuild.setOccurredOn(LocalDateTime.now());
			eventToBuild.setType(EventTypes.CARGO_ADDED);
			CargoAddedEvent eventBuilt = eventToBuild;
			eventToBuild = new CargoAddedEvent();
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

		public Builder setNature(CargoNature cargoNature) {
			eventToBuild.setNature(cargoNature);
			return this;
		}

		public Builder setOrigin(Location origin) {
			eventToBuild.setOrigin(origin);
			return this;
		}

		public Builder setDestination(Location destination) {
			eventToBuild.setDestination(destination);
			return this;
		}

		public Builder setRequiredSize(ContainerSize size) {
			eventToBuild.setRequiredSize(size);
			return this;
		}

		public Builder setCutOffDate(LocalDateTime date) {
			eventToBuild.setCutOffDate(date);
			return this;
		}
	}

}
