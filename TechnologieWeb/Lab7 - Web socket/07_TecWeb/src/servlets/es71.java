package servlets;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.*;

import beans.OperationReq;
import beans.OperationResp;
import encoders.*;

@ServerEndpoint(value = "/action", encoders = { JSONEncoder.class }, decoders = { JSONDecoder.class })

public class es71 {

	@OnMessage
	public void message(Session session, OperationReq msg) {
		CalculationResult calc = new CalculationResult(msg.getOp1(), msg.getOp2(), msg.getOperazione());
		OperationResp resp = new OperationResp();

		resp.setOp1(msg.getOp1());
		resp.setOp2(msg.getOp2());
		resp.setRisultato(calc.calculate());
		resp.setSuccess(calc.calculate().isNaN() ? false : true);
		resp.setValid(calc.calculate().isNaN() ? false : true);

		resp.setOldRis((double) session.getUserProperties().get("oldRis"));
		session.getUserProperties().put("oldRis", calc.calculate());

		try {
			session.getBasicRemote().sendObject(resp);
		} catch (IOException | EncodeException e) {
			System.out.println("Errore");
			e.printStackTrace();
			return;
		}
	}
}