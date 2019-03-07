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


package io.agilehandy.qry.contracts;

import io.agilehandy.common.api.events.BookingEvent;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Haytham Mohamed
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"io.agilehandy:poc-booking-service-cmd:0.0.1-SNAPSHOT:stubs"},
		stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class BookingListenerTests {

	@Autowired
	StubTrigger stubTrigger;

	@Autowired
	Sink sink;

	@Test
	public void should_receive_book_created_event_successfully() {
		boolean isPublished = stubTrigger.trigger("booking_created");  // as the contact is labeled

		Assertions.assertThat(isPublished).isEqualTo(true);

		sink.input().subscribe(msg -> {
			Assertions.assertThat(msg).isNotNull();
			Assertions.assertThat(msg.getPayload()).isNotNull();
			Assertions.assertThat(((BookingEvent)msg.getPayload()).getType()).isEqualTo("BOOKING_CREATED");
			Assertions.assertThat(((BookingEvent)msg.getPayload()).getBookingId()).isEqualTo("123456789");
		});
	}

}
