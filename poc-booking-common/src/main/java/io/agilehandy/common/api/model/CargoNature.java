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

import java.util.Arrays;

/**
 * @author Haytham Mohamed
 **/

public enum CargoNature {

	GENERAL_CARGO("GC"),
	DANGEROUS_CARGO("DG");

	private String value;
	private static CargoNature[] allValues = values();

	private CargoNature(String val) {
		this.value = val;
	}

	public String getValue() {
		return this.value;
	}

	public static CargoNature fromValue(String val) {
		return Arrays.asList(allValues)
				.stream()
				.filter(v -> v.equals(val))
				.findFirst()
				.orElseGet(() -> GENERAL_CARGO);
	}
}
