/**
 *
 */
package org.simon.pascal;

import org.junit.runner.RunWith;
import org.simon.pascal.SampleBackendApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author simon.pascal.ngos
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBackendApplication.class)
@ActiveProfiles("test")
public abstract class BaseTest {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

}
