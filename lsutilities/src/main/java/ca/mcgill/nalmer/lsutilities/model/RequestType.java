package ca.mcgill.nalmer.lsutilities.model;

/**
 * Represents a request type. The four request types are {@link #GET}, {@link #PUT}, {@link #POST}
 * and {@link #DELETE}.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public enum RequestType {
  /**
   * Get request.
   */
  GET,

  /**
   * Put request.
   */
  PUT,

  /**
   * Post request.
   */
  POST,

  /**
   * Delete request.
   */
  DELETE
}
