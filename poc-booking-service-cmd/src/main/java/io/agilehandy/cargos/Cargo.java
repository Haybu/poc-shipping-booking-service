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


package io.agilehandy.cargos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.events.cargos.CargoAddedEvent;
import io.agilehandy.common.api.events.cargos.CargoRouteAssignedEvent;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.CargoStatus;
import io.agilehandy.common.api.model.ContainerSize;
import io.agilehandy.common.api.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class Cargo {

	private UUID id;

	@JsonIgnore
	private UUID bookingId;

	private CargoNature nature;
	private ContainerSize requiredSize;
	private ContainerSize assignedSize;

	private Location origin;
	private Location destination;

	private UUID routeId;

	private CargoStatus status;

	private LocalDateTime statusDate;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cutOffDate;

	public Cargo(UUID cargoId) {
		this.id = cargoId;
	}

	public void cargoAdded(CargoAddedEvent event) {
		this.setId(UUID.fromString(event.getCargoId()));
		this.setBookingId(UUID.fromString(event.getBookingId()));
		this.setNature(event.getNature());
		this.setRequiredSize(event.getRequiredSize());
		this.setCutOffDate(event.getCutOffDate());
		this.setOrigin(event.getOrigin());
		this.setDestination(event.getDestination());
	}

	public void routeAssigned(CargoRouteAssignedEvent event) {

	}

}
