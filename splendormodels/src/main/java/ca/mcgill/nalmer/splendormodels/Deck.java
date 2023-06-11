package ca.mcgill.nalmer.splendormodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a stack of elements.
 *
 * @param <T> Type parameter.
 */
public final class Deck<T> {
  private final List<T> stack;

  /**
   * Creates an empty deck.
   */
  public Deck() {
    stack = new ArrayList<>();
  }

  /**
   * Creates a deck from a list of elements.
   *
   * @param elements First elements.
   * @throws NullPointerException if <code>elements</code> is <code>null</code> or contains
   *                              <code>null</code> elements.
   */
  public Deck(T[] elements) {
    if (elements == null) {
      throw new NullPointerException("elements cannot be null");
    }
    stack = new ArrayList<>(List.of(elements));
  }

  /**
   * Creates a deck from a list of elements.
   *
   * @param elements First elements.
   * @throws NullPointerException if <code>elements</code> is <code>null</code> or contains
   *                              <code>null</code> elements.
   */
  public Deck(List<T> elements) {
    if (elements == null) {
      throw new NullPointerException("elements cannot be null");
    }
    for (T element : elements) {
      if (element == null) {
        throw new NullPointerException("elements cannot contain null elements");
      }
    }
    stack = new ArrayList<>(elements);
  }

  /**
   * Copy constructor.
   *
   * @param newDeck Deck to copy.
   * @throws NullPointerException if <code>newDeck</code> is <code>null</code>.
   */
  public Deck(Deck<T> newDeck) {
    if (newDeck == null) {
      throw new NullPointerException("newDeck cannot be null");
    }
    stack = new ArrayList<>(newDeck.stack);
  }

  /**
   * Shuffles the deck.
   */
  public void shuffle() {
    Collections.shuffle(stack);
  }

  /**
   * Returns the size of the deck.
   *
   * @return Size of the deck.
   */
  public int getSize() {
    return stack.size();
  }

  /**
   * Returns whether the list is empty or not.
   *
   * @return True if pile is empty, False otherwise.
   */
  public boolean isEmpty() {
    return stack.isEmpty();
  }

  /**
   * Pops the first element if the stack is not empty and equal to the corresponding element.
   *
   * @param element Element to compare.
   * @return <code>true</code> if the element has been popped, <code>false</code> otherwise.
   */
  public boolean popIfNotEmptyAndEqual(T element) {
    if (element == null) {
      return false;
    }
    if (!stack.isEmpty() && stack.get(0).equals(element)) {
      pop();
      return true;
    }
    return false;
  }

  /**
   * Returns the top element.
   *
   * @return Element at the top of the deck, or <code>null</code> if the deck is empty.
   */
  public T peek() {
    try {
      return stack.get(0);
    } catch (IndexOutOfBoundsException ignored) {
      return null;
    }
  }

  /**
   * Returns and pop the element at the top of the pile.
   *
   * @return Element at the top of the pile.
   * @throws RuntimeException if the stack is empty.
   */
  public T pop() {
    try {
      return stack.remove(0);
    } catch (IndexOutOfBoundsException ignored) {
      throw new RuntimeException("stack is empty");
    }
  }

  /**
   * Pushes an element at the top of the pile.
   *
   * @param element Element to push.
   * @throws NullPointerException if <code>element</code> is <code>null</code>.
   */
  public void push(T element) {
    if (element == null) {
      throw new NullPointerException("element cannot be null");
    }
    stack.add(0, element);
  }
}
