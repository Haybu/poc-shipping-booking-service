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


package io.agilehandy.common.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoRequest {

	private ContainerSize requiredSize;
	private CargoNature nature;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cutOffDate;

	private Location origin;
	private Location destination;

	public static class Builder {

		private CargoRequest requestToBuild;

		public Builder() {
			requestToBuild = new CargoRequest();
		}

		public CargoRequest build() {
			CargoRequest requestBuilt = requestToBuild;
			requestToBuild = new CargoRequest();
			return requestToBuild;
		}

		public Builder setRequiredSize(ContainerSize size) {
			requestToBuild.setRequiredSize(size);
			return this;
		}

		public Builder setNature(CargoNature nature) {
			requestToBuild.setNature(nature);
			return this;
		}

		public Builder setCutOffDate(LocalDateTime cutOffDate) {
			requestToBuild.setCutOffDate(cutOffDate);
			return this;
		}

		public Builder setOrigin(Location location) {
			requestToBuild.setOrigin(location);
			return this;
		}

		public Builder setDestination(Location location) {
			requestToBuild.setDestination(location);
			return this;
		}
	}

}
