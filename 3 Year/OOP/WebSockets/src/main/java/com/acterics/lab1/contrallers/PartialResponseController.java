package com.acterics.lab1.contrallers;

import com.acterics.lab1.data.Message;
import com.acterics.lab1.data.SceneParams;
import com.acterics.lab1.data.TrajectoryGenerator;
import com.acterics.lab1.data.TrajectoryPointResponse;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

/**
 * Created by Masha Kereb on 02-Jun-17.
 */
public class PartialResponseController extends ResponseController {
    PartialResponseController(Session session) {
        super(session);
    }

    @Override
    public void processRequest(Message message) {
        try {
            SceneParams sceneParams = mapper.readValue(message.getData(), SceneParams.class);
            TrajectoryGenerator trajectoryGenerator = new TrajectoryGenerator(sceneParams);

            while (trajectoryGenerator.hasNext()) {
                TrajectoryPointResponse trajectoryResponse = new TrajectoryPointResponse();
                trajectoryResponse.setTrajectoryPoint(trajectoryGenerator.getNextTrajectoryPoint());
                String data = mapper.writeValueAsString(trajectoryResponse);

                Message response = new Message();
                response.setType(NEXT_RESPONSE);
                response.setStatus(STATUS_SUCCESS);
                response.setData(data);

                sendMessage(response);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            log.error(e.getStackTrace());
        }
        sendFinalMessage();
    }
}
