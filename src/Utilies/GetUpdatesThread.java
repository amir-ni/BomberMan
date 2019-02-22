package Utilies;

import Config.LocalConfig;
import org.json.JSONObject;

public class GetUpdatesThread extends Thread {

    public void run() {
        try {
            while (LocalConfig.interruptionState == 0) {
                JSONObject request = (new JSONObject()).put("Type", "GetUpdates").put("LatestUpdateId",LocalConfig.latestUpdateId);
                LocalEngine.UpdateHandler(LocalEngine.doSocket(request));
                synchronized (this) {
                    wait(LocalConfig.getUpdatesWaitTime);
                }
            }

            this.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
