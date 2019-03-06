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


package io.agilehandy.web;

import io.agilehandy.cargos.CargoAddCommand;
import io.agilehandy.cargos.Cargo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Haytham Mohamed
 **/

@RestController
public class CargoController {

	private final BookingService service;

	public CargoController(BookingService service) {
		this.service = service;
	}

	@PostMapping("/{bookingId}/cargos")
	public String addCargo(@RequestBody CargoAddCommand cmd) {
		return service.addCargo(cmd);
	}

    @GetMapping("/{bookingId}/cargos/{cargoId}")
	public Cargo getCargo(@PathVariable String bookingId, @PathVariable String cargoId) {
		return service.getCargo(bookingId, cargoId);
	}

	@GetMapping("/{bookingId}/cargos")
	public List<Cargo> getCargos(@PathVariable String bookingId) {
		return service.getCargos(bookingId);
	}
}
