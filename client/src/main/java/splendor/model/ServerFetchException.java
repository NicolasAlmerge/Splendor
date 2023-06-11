package splendor.model;

import ca.mcgill.nalmer.lsutilities.model.RequestType;

/**
 * Exception thrown when a server operation failed.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class ServerFetchException extends RuntimeException {

  /**
   * Request type.
   */
  private final RequestType request;

  /**
   * Server path.
   */
  private final String serverPath;

  /**
   * Error code.
   */
  private final int errorCode;

  /**
   * Error body message.
   */
  private final String errorBody;

  /**
   * Error status text.
   */
  private final String statusText;

  /**
   * Constructor.
   *
   * @param req    Request type.
   * @param path   Server subresource path.
   * @param code   Error code.
   * @param body   Error body string.
   * @param status Error status message.
   */
  public ServerFetchException(
      RequestType req, String path, int code, String body, String status
  ) {
    super("Failed to access " + req + " '" + path + "'. Error " + code + ": " + body + status);
    request = req;
    serverPath = path;
    errorCode = code;
    errorBody = body;
    statusText = status;
  }

  /**
   * Gets the request type made.
   *
   * @return Request type.
   */
  public RequestType getRequest() {
    return request;
  }

  /**
   * Gets the server subresource path that we tried to access.
   *
   * @return Server subresource path.
   */
  public String getPath() {
    return serverPath;
  }

  /**
   * Gets the error code.
   *
   * @return Error code.
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Gets the error body message.
   *
   * @return Error body message.
   */
  public String getErrorBody() {
    return errorBody;
  }

  /**
   * Gets the error status text.
   *
   * @return Error status text.
   */
  public String getStatusText() {
    return statusText;
  }
}
