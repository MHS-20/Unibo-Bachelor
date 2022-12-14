package encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import beans.OperationResp;

public class JSONEncoder implements Encoder.Text<OperationResp> {

	private Gson g = new Gson();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(EndpointConfig ec) {

	}

	@Override
	public String encode(OperationResp mex) throws EncodeException {
		return g.toJson(mex);
	}

}
