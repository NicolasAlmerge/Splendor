package ca.mcgill.nalmer.lsutilities.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the collection of all sessions.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class SessionManager {
  private static Map<String, Session> sessionsMap = new HashMap<>();

  /**
   * Private constructor.
   */
  private SessionManager() {}

  /**
   * Gets a session from an id.
   *
   * @param id Session id.
   * @return Session with corresponding id.
   * @throws RuntimeException if session is not found.
   */
  public static Session getSession(String id) {
    Session session = sessionsMap.get(id);
    if (session == null) {
      throw new RuntimeException("Cannot get session with specified id");
    }
    return session;
  }

  /**
   * Get all sessions.
   *
   * @return All sessions as a string to session map.
   */
  public static Map<String, Session> getAllSessions() {
    return Map.copyOf(sessionsMap);
  }

  /**
   * Checks if we have a session with a specific id.
   *
   * @param id Session id.
   * @return True if such session exists, false otherwise.
   */
  public static boolean hasSession(String id) {
    return sessionsMap.containsKey(id);
  }

  /**
   * Sets a session with a special id.
   *
   * @param id Session id.
   * @param session Session to add.
   */
  public static void addSession(String id, Session session) {
    sessionsMap.put(id, session);
  }

  /**
   * Clears all sessions.
   */
  public static void clear() {
    sessionsMap.clear();
  }

  /**
   * Bulk updates all sessions.
   *
   * @param newSessions New sessions to update. If null, then an empty dictionary will be created.
   */
  public static void updateSessions(Map<String, Session> newSessions) {
    if (newSessions == null) {
      sessionsMap = new HashMap<>();
    } else {
      sessionsMap = Map.copyOf(newSessions);
    }
  }
}
