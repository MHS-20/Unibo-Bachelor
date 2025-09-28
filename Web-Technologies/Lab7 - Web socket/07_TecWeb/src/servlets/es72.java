package servlets;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.*;

import beans.OperationReq;
import beans.OperationResp;
import encoders.*;

@ServerEndpoint(value = "/action", encoders = { JSONEncoder.class }, decoders = { JSONDecoder.class })

public class es72 {
	
	private double oldRis; 

	@OnMessage
	public synchronized void message(Session session, OperationReq msg) {
		CalculationResult calc = new CalculationResult(msg.getOp1(), msg.getOp2(), msg.getOperazione());
		OperationResp resp = new OperationResp();

		resp.setOp1(msg.getOp1());
		resp.setOp2(msg.getOp2());
		resp.setRisultato(calc.calculate());
		resp.setSuccess(calc.calculate().isNaN() ? false : true);
		resp.setValid(calc.calculate().isNaN() ? false : true);

		this.oldRis = (double) session.getUserProperties().get("oldRis"); 
		resp.setOldRis((double) session.getUserProperties().get("oldRis"));
		session.getUserProperties().put("oldRis", calc.calculate());

		try {
			for (Session sess : session.getOpenSessions()) {
				if (sess.isOpen())
					sess.getBasicRemote().sendObject(resp);
			}
		} catch (EncodeException | IOException e) {
			e.printStackTrace();
			return;
		}
	}
}