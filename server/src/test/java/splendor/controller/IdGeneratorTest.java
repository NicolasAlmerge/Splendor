package splendor.controller;

import org.junit.Assert;
import org.junit.Test;

public final class IdGeneratorTest {

  @Test
  public void testInitialisation1() {
    IdGenerator generator = new IdGenerator();
    Assert.assertEquals(0, generator.getNext());
  }

  @Test
  public void testInitialisation2() {
    int start = 54162;
    IdGenerator generator = new IdGenerator(start);
    Assert.assertEquals(start, generator.getNext());
  }

  @Test
  public void testIncrement1() {
    IdGenerator generator = new IdGenerator();
    generator.getNext();
    Assert.assertEquals(1, generator.getNext());
    Assert.assertEquals(2, generator.getNext());
    Assert.assertEquals(3, generator.getNext());
  }

  @Test
  public void testIncrement2() {
    int start = 54162;
    IdGenerator generator = new IdGenerator(start);
    generator.getNext();
    Assert.assertEquals(start+1, generator.getNext());
    Assert.assertEquals(start+2, generator.getNext());
    Assert.assertEquals(start+3, generator.getNext());
  }
}
