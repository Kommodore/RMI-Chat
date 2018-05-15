import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@WebServlet(name = "ClientServlet")
public class ClientServlet extends HttpServlet implements ClientProxy{
	private ChatServer stub;
	private ChatProxy proxyObject = null;
	private String username = "";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String action = request.getParameter("action");
		response.setStatus(200);
		
		if(action.equals("subscribe")){
			try {
				username = request.getParameter("username");
				proxyObject = stub.subscribeUser(username, this);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				response.sendError(403);
			} catch (NullPointerException e1){
				response.sendError(420);
			}
		}
		
		if(action.equals("unsubscribe")){
			try{
				stub.unsubscribeUser(username);
				proxyObject = null;
			} catch(RemoteException e1){
				e1.printStackTrace();
				response.sendError(500);
			}
		}
		
		if(action.equals("send_message")){
			if(proxyObject != null){
				try{
					proxyObject.sendMessage(request.getParameter("message"));
				} catch(RemoteException e1){
					e1.printStackTrace();
					response.sendError(420);
				}
			} else {
				response.sendError(401);
			}
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	
	@Override
	public void receiveMessage(String username, String message) throws RemoteException {
	
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		String serverUrl = "rmi://localhost:1099/ChatServer";
		
		try {
			Registry registry = LocateRegistry.getRegistry();
			stub = (ChatServer)registry.lookup(serverUrl);
			
		} catch (NotBoundException e) {
			System.err.println("Remote object not found. Is the server started?");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.err.println("Please check if the server and registry are running.");
			e.printStackTrace();
		}
	}
}
