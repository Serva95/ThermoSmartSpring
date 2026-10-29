package it.srv.ThermoSmartSpring;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Verifies the Thymeleaf Spring Security dialect (thymeleaf-extras-springsecurity6, "sec:" namespace)
 * renders correctly on top of Spring Security 7: sec:authorize branching and the #authentication
 * expression object are both exercised through the real template engine and filter chain.
 */
@SpringBootTest
class SecurityDialectRenderTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		// Boot 4 removed @AutoConfigureMockMvc, so the MockMvc setup is explicit
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(SecurityMockMvcConfigurers.springSecurity())
			.build();
	}

	@Test
	void adminSeesAuthorizedContent() throws Exception {
		mockMvc.perform(get("/").with(user("admin").roles("ADMIN")))
			.andExpect(status().isOk())
			// sec:authorize="isAuthenticated()" branch -> #authentication.getName() must resolve
			.andExpect(content().string(containsString("Benvenuto admin")))
			// sec:authorize="hasRole('ROLE_ADMIN')" branch -> admin-only nav link must be present
			.andExpect(content().string(containsString("/sensors")));
	}

	@Test
	void anonymousSeesLoginPromptOnly() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			// sec:authorize="!isAuthenticated()" branch -> login prompt must be present
			.andExpect(content().string(containsString("Registrati e Accedi")))
			// sec:authorize="hasRole('ROLE_ADMIN')" branch -> admin-only content must be absent
			.andExpect(content().string(not(containsString("Benvenuto"))))
			.andExpect(content().string(not(containsString("/sensors"))));
	}
}
