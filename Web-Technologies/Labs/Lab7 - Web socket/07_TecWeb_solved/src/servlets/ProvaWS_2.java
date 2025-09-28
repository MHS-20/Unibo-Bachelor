package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.websocket.*;
import javax.websocket.server.*;

import com.google.gson.Gson;

import beans.OperationReq;
import beans.OperationResp;
import beans.UpdateReq;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/actions2")
public class ProvaWS_2 {

	private Session sessione;
	private Gson g;
	private Map<String, Integer> reqCounter = new HashMap<String, Integer>();
	private OperationResp lastOperation = new OperationResp();
	private Set<String> users = new HashSet<String>();
	private static final Set<ProvaWS_2> calcEndpoints = new CopyOnWriteArraySet<>();

	@OnOpen
	public void open(Session session) {
		this.sessione = session;
		g = new Gson();
		System.out.println("Connessione Aperta ");
		reqCounter.put(session.getId(), 0);
		calcEndpoints.add(this);
		users.add(session.getId());
		System.out.print("ho finito la open");
	}

	@OnClose
	public void close(Session session) {
		System.out.println("Connessione Chiusa ");
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws IOException, EncodeException {

		System.out.println("Messaggio: " + message.toString());
		if (message.contains("valore")) {
			UpdateReq up;
			try {
				up = g.fromJson(message, UpdateReq.class);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			System.out.println("Broadcasting: " + up.toString());
			broadcast(g.toJson(up));
		} else {
			int counter = reqCounter.get(session.getId());
			OperationResp resp = new OperationResp();
			if (counter > 100) {
				resp = new OperationResp();
				resp.setValid(false);
			} else {
				counter++;
				reqCounter.put(session.getId(), counter);
				OperationReq req = g.fromJson(message, OperationReq.class);
				System.out.println("Messaggio Arrivato: " + message + " da: " + session.getId());
				System.out.println(req.toString());
				resp = DoOperation(req, lastOperation.getRisultato());
				lastOperation = resp;
			}
			System.out.println("resp toString" + resp.toString());
			broadcast(g.toJson(resp));
		}

	}

	private OperationResp DoOperation(OperationReq req, double oldRis) {
		// TODO Auto-generated method stub
		OperationResp r = new OperationResp();
		r.setOp1(req.getOp1());
		r.setOp2(req.getOp2());
		r.setTipoOp(req.getOperazione());
		r.setOldRis(oldRis);
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

//	private void sendback(String message) throws IOException, EncodeException {
//		// TODO Auto-generated method stub
//		try {
//			System.out.println("Sto inviando: "+message);
//			this.sessione.getBasicRemote().sendText(message);
//			
//		} catch (IOException  e) {
//            e.printStackTrace();
//        }
//		
//	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Errore ");
		throwable.printStackTrace();
	}

	private static void broadcast(String message) throws IOException, EncodeException {

		calcEndpoints.forEach(endpoint -> {
			try {
				endpoint.sessione.getBasicRemote().sendText(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
