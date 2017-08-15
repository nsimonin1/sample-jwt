/**
 *
 */
package org.simon.pascal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author simon.pascal.ngos
 *
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Password is Invalid")
public class InvalidPasswordException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
