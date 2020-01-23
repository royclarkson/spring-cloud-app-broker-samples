/*
 * Copyright 2002-2020 the original author or authors.
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
package org.springframework.cloud.appbroker.samples.parametertransformer;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import org.springframework.cloud.appbroker.deployer.BackingApplication;
import org.springframework.cloud.appbroker.extensions.parameters.ParametersTransformer;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTimeoutParameterTransformerTest {

	private ParametersTransformer<BackingApplication> transformer;

	@BeforeEach
	void setUp() {
		RequestTimeoutParameterTransformer gatewayServerParameterTransformer = new RequestTimeoutParameterTransformer();
		transformer = gatewayServerParameterTransformer.create(null);
	}

	@Test
	void shouldTransformRequestAndResponseTimeouts() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("request-timeout-ms", 10_000);

		StepVerifier
			.create(transformer
				.transform(BackingApplication.builder()
					.environment(new HashMap<>())
					.build(), parameters)
			).assertNext(backingApplication -> {
			assertThat(backingApplication).isNotNull();
			assertThat(backingApplication.getEnvironment()
				.get("my-app.httpclient.connect-timeout"))
				.isEqualTo(10_000);
		}).verifyComplete();
	}

}
