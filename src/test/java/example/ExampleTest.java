package example;

import org.junit.Assert;
import org.junit.Test;

public class ExampleTest {
	@Test
	public void testValueGetter() {
		final Example ex = new Example();
		
		Assert.assertEquals(42, ex.getValue());
	}
}