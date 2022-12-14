package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.websocket.*;
import javax.websocket.server.*;

import com.google.gson.Gson;

import beans.OperationReq;
import beans.OperationResp;
import java.util.*;

@ServerEndpoint("/actions")
public class ProvaWS {

	//Mappe in cui la chiave è l'id della sessione
	//Ogni sessione ha una riga nella mappa 
	private Session sessione;
	private Gson g;
	private Map<String, Integer> reqCounter = new HashMap<String, Integer>();
	private Map<String, OperationResp> lastOperations = new HashMap<String, OperationResp>();

	@OnOpen
	public void open(Session session) {
		this.sessione = session;
		g = new Gson();
		System.out.println("Connessione Aperta ");
		reqCounter.put(session.getId(), 0);
		lastOperations.put(session.getId(), new OperationResp());
	}

	@OnClose
	public void close(Session session) {
		System.out.println("Connessione Chiusa ");
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws IOException, EncodeException {

		int counter = reqCounter.get(session.getId());
		OperationResp resp;
		if (counter > 100) {
			resp = new OperationResp();
			resp.setValid(false);
		} else {
			counter++;
			reqCounter.put(session.getId(), counter);
			OperationReq req = g.fromJson(message, OperationReq.class);
			System.out.println("Messaggio Arrivato: " + message + " da: " + session.getId());
			System.out.println(req.toString());
			resp = DoOperation(req);
			lastOperations.put(session.getId(), resp);
		}

		sendback(g.toJson(resp));
	}

	private OperationResp DoOperation(OperationReq req) {
		// TODO Auto-generated method stub
		OperationResp r = new OperationResp();
		r.setOp1(req.getOp1());
		r.setOp2(req.getOp2());
		r.setTipoOp(req.getOperazione());
		switch (req.getOperazione()) {
		case "add": {
			r.setRisultato(req.getOp1() + req.getOp2());
			r.setSuccess(true);
		}
			break;

		case "sott": {
			r.setRisultato(req.getOp1() - req.getOp2());
			r.setSuccess(true);
		}
			break;

		case "mol": {
			r.setRisultato(req.getOp1() * req.getOp2());
			r.setSuccess(true);
		}
			break;

		case "div": {
			if (req.getOp2() == 0) {
				r.setSuccess(false);
				r.setRisultato(-99);
			} else {
				r.setRisultato(req.getOp1() / req.getOp2());
				r.setSuccess(true);
			}
		}
			break;
		}
		return r;
	}

	private void sendback(String message) throws IOException, EncodeException {
		// TODO Auto-generated method stub
		try {
			System.out.println("Sto inviando: " + message);
			this.sessione.getBasicRemote().sendText(message);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Errore ");
	}

}
