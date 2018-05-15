import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;

@WebServlet(name = "ClientServlet")
public class ClientServlet extends HttpServlet{
	private ChatProxy proxyObject = null;
	private ClientProxyImpl client;
	private String username = "";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String action = request.getParameter("action");
		response.setStatus(200);
		
		if(action.equals("subscribe")){
			try {
				client.connect(request.getParameter("server_ip"));
				username = request.getParameter("username");
				proxyObject = client.stub.subscribeUser(username, client);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				response.sendError(403);
			} catch (NullPointerException e1){
				e1.printStackTrace();
				response.sendError(420);
			}
		}
		
		if(action.equals("unsubscribe")){
			if(client.stub == null){
				response.sendError(500);
			} else {
				try{
					client.stub.unsubscribeUser(username);
					proxyObject = null;
					System.out.println("User unsubscribed");
				} catch(RemoteException e1){
					e1.printStackTrace();
					response.sendError(500);
				}
			}
		}
		
		if(action.equals("send_message")){
			if(proxyObject != null){
				try{
					proxyObject.sendMessage("<" + username + ">: "+request.getParameter("message"));
				} catch(RemoteException e1){
					e1.printStackTrace();
					response.sendError(420);
				}
			} else if(client.stub == null) {
				response.sendError(500);
			} else {
				response.sendError(401);
			}
		}
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			client = new ClientProxyImpl();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
