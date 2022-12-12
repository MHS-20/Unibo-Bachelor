package encoders;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import beans.OperationReq;

public class JSONDecoder implements Decoder.Text<OperationReq> {

	private Gson g= new Gson();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(EndpointConfig ec) {
	}

	@Override
	public OperationReq decode(String mex) throws DecodeException {
		return g.fromJson(mex, OperationReq.class);
	}

	@Override
	public boolean willDecode(String arg0) {
		return false;
	}
}