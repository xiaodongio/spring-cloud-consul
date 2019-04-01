/*
 * Copyright 2013-2016 the original author or authors.
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

package org.springframework.cloud.consul.serviceregistry;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;

/**
 * @author Piotr Wielgolaski
 */
public class ConsulServletRegistrationCustomizer implements ConsulRegistrationCustomizer {
	private ObjectProvider<ServletContext> servletContext;

	public ConsulServletRegistrationCustomizer(ObjectProvider<ServletContext> servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void customize(ConsulRegistration registration) {
		if (servletContext == null) {
			return;
		}
		ServletContext sc = servletContext.getIfAvailable();
		if(sc != null
				&& StringUtils.hasText(sc.getContextPath())
				&& StringUtils.hasText(sc.getContextPath().replaceAll("/", ""))) {
			List<String> tags = registration.getService().getTags();
			if (tags == null) {
				tags = new ArrayList<>();
			}
			tags.add("contextPath=" + sc.getContextPath());
			registration.getService().setTags(tags);
		}
	}
}