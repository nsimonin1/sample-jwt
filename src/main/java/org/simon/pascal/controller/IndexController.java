/**
 *
 */
package org.simon.pascal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author simon.pascal.ngos
 *
 */
@Controller
public class IndexController {

	/**
	 * @return index page
	 */
	@GetMapping({ "/" })
	public String index() {
		return "forward:index.html";
	}
}
