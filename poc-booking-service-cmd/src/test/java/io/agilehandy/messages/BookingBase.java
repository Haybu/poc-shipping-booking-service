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


package io.agilehandy.messages;

import io.agilehandy.PocBookingServiceCmdApplication;
import io.agilehandy.common.api.events.BookingEvent;
import io.agilehandy.common.api.events.bookings.BookingCreatedEvent;
import io.agilehandy.common.api.model.CargoRequest;
import io.agilehandy.pubsub.BookingEventPubSub;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Haytham Mohamed
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocBookingServiceCmdApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public class BookingBase {

	@Autowired
	private MessageVerifier messaging;

	@Autowired
	BookingEventPubSub pubSub;

	@Before
	public void setup() {
		// let's clear any remaining messages
		this.messaging.receive("output", 100, TimeUnit.MILLISECONDS);
	}

	public void bookingCreated() {
		CargoRequest cargoRequest = new CargoRequest.Builder()
				.setCutOffDate(null)
				.setDestination(null)
				.setDestination(null)
				.setNature(null)
				.setRequiredSize(null)
				.build();

		LocalDateTime dt = LocalDateTime.of(2010, 10, 10, 10,10, 10);
		BookingEvent event = new BookingCreatedEvent.Builder()
				.setOccurredOn(dt)
				.setBookingId("123456789")
				.setCustomerId("12345")
				.setCargoRequests(Arrays.asList(cargoRequest))
				.build();

		pubSub.publish(event);
	}
}
