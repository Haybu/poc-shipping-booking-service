/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.agilehandy.qry.cargos;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.agilehandy.common.api.model.CargoNature;
import io.agilehandy.common.api.model.CargoStatus;
import io.agilehandy.common.api.model.ContainerSize;
import io.agilehandy.qry.bookings.Booking;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Haytham Mohamed
 **/

@Entity(name="cargos")
@Data
@NoArgsConstructor
public class Cargo {

	@Id
	private String cargoId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bookingId")
	private Booking booking;

	private CargoNature nature;
	private ContainerSize requiredSize;
	private ContainerSize assignedSize;

	private String originOpZone;
	private String originFacility;

	private String destOpZone;
	private String destFacility;

	private CargoStatus status;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime statusDate;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cutOffDate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Cargo )) return false;
		return cargoId != null && cargoId.equals(((Cargo) o).getCargoId());
	}

	@Override
	public int hashCode() {
		return cargoId.hashCode();
	}
}
