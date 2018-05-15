import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;

@WebServlet(name = "ClientServlet")
public class ClientServlet extends HttpServlet implements PropertyChangeListener{
	private ChatProxy proxyObject = null;
	private ClientProxyImpl client;
	private String username = "";
	private ArrayList<AsyncContext> contexts = new ArrayList<>();
	private static int i = 0;
	
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
					proxyObject.sendMessage(username + ": "+request.getParameter("message"));
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		if(request.getParameter("action").equals("listen")){
			//initialize for server-sent events
			request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
			
			// Set header fields
			response.setContentType("text/event-stream");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Connection", "keep-alive");
			response.setCharacterEncoding("UTF-8");
			
			//to clear threads and allow for asynchronous execution
			final AsyncContext asyncContext = request.startAsync(request, response);
			asyncContext.setTimeout(0);
			
			//add context to list for later use
			contexts.add(asyncContext);
			System.out.println("Registered user");
		} else {
			System.out.println("Unknown");
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Sending message to clients: "+evt.getNewValue());
		for(AsyncContext asyncContext: contexts){
			System.out.println("Drinne");
			PrintWriter writer;
			try {
				writer = asyncContext.getResponse().getWriter();
				sendMessage(writer, evt.getNewValue());
			} catch (IOException e) {
				e.printStackTrace();
				try {
					writer = asyncContext.getResponse().getWriter();
					sendMessage(writer, evt.getNewValue());
				} catch (IOException e1) {
					e1.printStackTrace();
					contexts.remove(asyncContext);
				}
			}
		}
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			client = new ClientProxyImpl();
			client.addPropertyChangeListener(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(PrintWriter writer, Object message){
		writer.print("id: ");
		writer.println(i++);
		writer.print("data: ");
		writer.println(message);
		writer.println();
		writer.flush();
	}
}
