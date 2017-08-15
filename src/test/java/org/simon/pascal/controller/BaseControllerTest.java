/**
 *
 */
package org.simon.pascal.controller;

import org.simon.pascal.BaseTest;
import org.simon.pascal.controller.BaseController;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author simon.pascal.ngos
 *
 */
@WebAppConfiguration
public abstract class BaseControllerTest extends BaseTest {

	protected MockMvc mvc;

	protected void setUp(BaseController controller) {
		this.mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
}
