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


package io.agilehandy.qry.summary;

import io.agilehandy.common.api.events.cargos.CargoAddedEvent;
import io.agilehandy.common.api.model.Location;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Haytham Mohamed
 **/

@Component
@EnableBinding(Sink.class)
@Log4j2
public class DataProjection {

	private final String HEADER_EVENT_TYPE = "event-type";

	private final LocationSummaryRepository locationSummaryRepository;


	public DataProjection(LocationSummaryRepository repository) {

		this.locationSummaryRepository = repository;
	}

	@StreamListener(target = Sink.INPUT,
			condition = "headers['event_type']=='CARGO_ADDED'")
	public void createSummaryProjection(@Payload CargoAddedEvent event) {
		log.info("projecting a summary of number of cargos in/out from/to facilities");
		Location origin = event.getOrigin();
		Location dest = event.getDestination();

		// set count out
		Optional<LocationSummary> locationOut = locationSummaryRepository
				.findByZoneAndFacility(origin.getOpZone(), origin.getFacility());
		LocationSummary summaryOut = locationOut.orElseGet(() -> new LocationSummary(
				origin.getOpZone(), origin.getFacility()
		));
		summaryOut.getRequestOut().incrementAndGet();
		locationSummaryRepository.save(summaryOut);

		// set count in
		Optional<LocationSummary> locationIn = locationSummaryRepository
				.findByZoneAndFacility(dest.getOpZone(), dest.getFacility());
		LocationSummary summaryIn = locationIn.orElseGet(() -> new LocationSummary(
				dest.getOpZone(), dest.getFacility()
		));
		summaryIn.getRequestIn().incrementAndGet();
		locationSummaryRepository.save(summaryIn);
	}
}
