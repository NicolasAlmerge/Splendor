package ca.mcgill.nalmer.splendormodels;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class DeckTest {
  private Deck<Integer> deck;
  private int count = 0;

  @Before
  public void clear() {
    deck = new Deck<>();
    count = 0;
  }

  @Test
  public void testEmpty() {
    checkDeck();
  }

  @Test
  public void testAddOneElement() {
    push();
    checkDeck();
  }

  @Test
  public void testAddTwoElements() {
    push(2);
    checkDeck();
  }

  @Test
  public void testAddThreeElements() {
    push(3);
    checkDeck();
  }

  @Test
  public void testAddAndRemoveOneElement() {
    push();
    pop();
    checkDeck();
  }

  @Test
  public void testAddTwoRemoveOneElement() {
    push(2);
    pop();
    checkDeck();
  }

  @Test
  public void testAddAndRemoveTwoElements() {
    push(2);
    pop(2);
    checkDeck();
  }

  @Test
  public void testAddThreeRemoveOneElement() {
    push(3);
    pop();
    checkDeck();
  }

  @Test
  public void testAddThreeRemoveTwoElements() {
    push(3);
    pop(2);
    checkDeck();
  }

  @Test
  public void testAddAndRemoveThreeElements() {
    push(3);
    pop(3);
    checkDeck();
  }

  @Test
  public void testRemoveOneElementEmptyDeck() {
    Assert.assertThrows(RuntimeException.class, () -> deck.pop());
  }

  @Test
  public void testRemoveEqual() {
    push(1);
    Assert.assertTrue(deck.popIfNotEmptyAndEqual(1));
    checkDeck(0);
  }

  @Test
  public void testRemoveNotEqual() {
    push(1);
    Assert.assertFalse(deck.popIfNotEmptyAndEqual(2));
    checkDeck(1);
  }

  @Test
  public void checkCopyEmpty1() {
    Deck<Integer> newDeck = new Deck<>(new Integer[] {});
    Assert.assertTrue(newDeck.isEmpty());
  }

  @Test
  public void checkCopyEmpty2() {
    Deck<Integer> newDeck = new Deck<>(new ArrayList<>());
    Assert.assertTrue(newDeck.isEmpty());
  }

  @Test
  public void checkCopyEmpty3() {
    Deck<Integer> newDeck = new Deck<>(deck);
    Assert.assertTrue(newDeck.isEmpty());
  }

  @Test
  public void checkCopyNonEmpty1() {
    Deck<Integer> newDeck = new Deck<>(new Integer[] {2, 3, 1});
    Assert.assertEquals(3, newDeck.getSize());
    Assert.assertNotNull(newDeck.peek());
    Assert.assertEquals(2, (int) newDeck.peek());
  }

  @Test
  public void checkCopyNonEmpty2() {
    List<Integer> integers = new ArrayList<>();
    integers.add(2);
    integers.add(3);
    integers.add(1);
    Deck<Integer> newDeck = new Deck<>(integers);
    Assert.assertEquals(3, newDeck.getSize());
    Assert.assertNotNull(newDeck.peek());
    Assert.assertEquals(2, (int) newDeck.peek());
  }

  @Test
  public void checkCopyNonEmpty3() {
    push(3);
    Deck<Integer> newDeck = new Deck<>(deck);
    Assert.assertEquals(3, newDeck.getSize());
    Assert.assertNotNull(newDeck.peek());
    Assert.assertEquals(3, (int) newDeck.peek());
  }

  @Test
  public void testShuffle() {
    final int size = 10000;
    push(size);
    deck.shuffle();
    Assert.assertEquals(size, deck.getSize());
  }

  private void push(int n) {
    for (int i = 0; i < n; ++i) {
      push();
    }
  }

  private void push() {
    deck.push(++count);
  }

  private void pop(int n) {
    for (int i = 0; i < n; ++i) {
      pop();
    }
  }

  private void pop() {
    deck.pop();
    --count;
  }

  private void checkDeck(int size) {
    if (size == 0) {
      Assert.assertTrue(deck.isEmpty());
      Assert.assertEquals(size, deck.getSize());
      Assert.assertNull(deck.peek());
    } else {
      Assert.assertFalse(deck.isEmpty());
      Assert.assertEquals(size, deck.getSize());
      Assert.assertNotNull(deck.peek());
      Assert.assertEquals(size, (int) deck.peek());
    }
  }

  private void checkDeck() {
    checkDeck(count);
  }
}
