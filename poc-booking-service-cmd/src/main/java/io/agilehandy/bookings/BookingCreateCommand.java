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

import io.agilehandy.common.api.model.CargoRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class BookingCreateCommand implements Serializable {

	private String customerId;
	List<CargoRequest> cargoRequests;

	public static class Builder {

		private BookingCreateCommand commandToBuild;

		public Builder() {
			commandToBuild = new BookingCreateCommand();
		}

		public BookingCreateCommand build() {
			BookingCreateCommand commandBuilt = commandToBuild;
			commandToBuild = new BookingCreateCommand();
			return commandBuilt;
		}

		public Builder setCustomerId(String customerId) {
			commandToBuild.setCustomerId(customerId);
			return this;
		}

		public Builder requestCargo(CargoRequest request) {
			if (commandToBuild.getCargoRequests() != null) {
				commandToBuild.setCargoRequests(new ArrayList<>());
			}
			commandToBuild.getCargoRequests().add(request);
			return this;
		}
	}

}
