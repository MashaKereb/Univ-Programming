package com.acterics.lab1.contrallers;

import com.acterics.lab1.data.Message;
import com.acterics.lab1.data.SceneParams;
import com.acterics.lab1.data.TrajectoryGenerator;
import com.acterics.lab1.data.TrajectoryResponse;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

/**
 * Created by Masha Kereb on 02-Jun-17.
 */
public class FullResponseController extends ResponseController {
    FullResponseController(Session session) {
        super(session);
    }

    @Override
    public void processRequest(Message message) {
        try {
            SceneParams sceneParams = mapper.readValue(message.getData(), SceneParams.class);
            TrajectoryGenerator trajectoryGenerator = new TrajectoryGenerator(sceneParams);

            TrajectoryResponse trajectoryResponse = new TrajectoryResponse();
            trajectoryResponse.setTrajectory(trajectoryGenerator.getFullTrajectory());


            String data = mapper.writeValueAsString(trajectoryResponse);

            Message response = new Message();
            response.setType(START_RESPONSE);
            response.setStatus(STATUS_SUCCESS);
            response.setData(data);

            sendMessage(response);

        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
        sendFinalMessage();

    }
}
