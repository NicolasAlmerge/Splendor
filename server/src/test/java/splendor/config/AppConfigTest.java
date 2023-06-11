package splendor.config;

import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.TakeTokensAction;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public final class AppConfigTest {
  public static Gson getGsonBean() {
    return new AppConfig().gson();
  }

  @Test
  public void testGsonBean() {
    Gson gsonBean = getGsonBean();
    TakeTokensAction action = new TakeTokensAction(new Cost("01100"), 0);
    TakeTokensAction afterProcess = gsonBean.fromJson(gsonBean.toJson(action), TakeTokensAction.class);

    Assert.assertEquals(action.getCost(), afterProcess.getCost());
    Assert.assertEquals(action.getType(), afterProcess.getType());
  }
}
