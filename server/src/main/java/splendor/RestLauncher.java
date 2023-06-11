package splendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Rest launcher class.
 */
@SpringBootApplication
public class RestLauncher {

  /**
   * Constructor.
   */
  public RestLauncher() {
  }

  /**
   * Starting the application.
   *
   * @param args Not used.
   */
  public static void main(String[] args) {
    SpringApplication.run(RestLauncher.class, args);
  }
}
