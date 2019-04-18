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

import io.agilehandy.common.api.model.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.LocalDateTime;

/**
 * @author Haytham Mohamed
 **/

@Data
@NoArgsConstructor
public class CargoChangeStatusCommand {

	private BookingStatus status;
	private LocalDateTime statusDate;

	public static class Builder {

		private CargoChangeStatusCommand commandToBuild;

		public Builder() {
			commandToBuild = new CargoChangeStatusCommand();
		}

		public CargoChangeStatusCommand build() {
			CargoChangeStatusCommand commandBuilt = commandToBuild;
			commandToBuild = new CargoChangeStatusCommand();
			return commandBuilt;
		}

		public Builder setStatus(BookingStatus status) {
			commandToBuild.setStatus(status);
			return this;
		}

		public Builder setStatusDate(LocalDateTime statusDate) {
			commandToBuild.setStatusDate(statusDate);
			return this;
		}
	}
}
