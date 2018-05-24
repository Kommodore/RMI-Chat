import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.HashMap;

@WebServlet(name = "ClientServlet")
public class ClientServlet extends HttpServlet implements PropertyChangeListener{
	
	private HashMap<String, Client> clients = new HashMap<>();
	private static int i = 0;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String action = request.getParameter("action");
		String username;
		try{
			username = getUsername(request);
		} catch(NullPointerException e){
			username = request.getParameter("username");
		}
		
		response.setStatus(200);
		
		if(action.equals("subscribe")){
			username = request.getParameter("username");
			Client newClient;
			try {
				newClient = new Client();
				newClient.connect(request.getParameter("server_ip"), username);
				newClient.getChatServer().addPropertyChangeListener(this);
				clients.put(username, newClient);
				System.out.println("User "+username+" subscribed");
				
				request.getSession().setAttribute("server_ip", request.getParameter("server_ip"));
				request.getSession().setAttribute("username", request.getParameter("username"));
				request.getSession().setMaxInactiveInterval(-1);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				response.sendError(403);
			} catch (NullPointerException e1){
				e1.printStackTrace();
				response.sendError(420);
			}
		}
		
		if(action.equals("unsubscribe")){
			if(clients.get(username).getChatServer().stub == null){
				response.sendError(500);
			} else {
				try{
					clients.get(username).getChatServer().stub.unsubscribeUser(username);
					clients.remove(username);
					
					System.out.println("User "+getUsername(request)+" unsubscribed");
					request.getSession().removeAttribute("server_ip");
					request.getSession().removeAttribute("username");
					
				} catch(RemoteException e1){
					e1.printStackTrace();
					response.sendError(500);
				}
			}
		}
		
		if(action.equals("send_message")){
			System.out.println("User "+getUsername(request)+" is sending message: "+request.getParameter("message"));
			if(clients.get(username).getProxyObject() != null){
				try{
					clients.get(username).getProxyObject().sendMessage(getUsername(request) + ": "+request.getParameter("message"));
				} catch(RemoteException e1){
					e1.printStackTrace();
					response.sendError(420);
				}
			} else if(clients.get(username).getChatServer().stub == null) {
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
			clients.get(getUsername(request)).setNewMessagesRequest(asyncContext);
			System.out.println("Registered async event source");
		} else {
			System.out.println("Unknown");
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Message message = (Message)evt.getNewValue();
		
		System.out.println("Sending message \""+message.getMessage()+"\n to "+message.getUsername());
		
		AsyncContext asyncContext = clients.get(message.getUsername()).getNewMessagesRequest();
		PrintWriter writer;
		
		if(asyncContext != null){
			try {
				writer = asyncContext.getResponse().getWriter();
				sendMessage(writer, message);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					writer = asyncContext.getResponse().getWriter();
					sendMessage(writer, message);
				} catch (IOException e1) {
					e1.printStackTrace();
					clients.get(message.getUsername()).setNewMessagesRequest(null);
				}
			}
		}
	}
	
	private void sendMessage(PrintWriter writer, Message message){
		writer.print("id: ");
		writer.println(i++);
		writer.print("data: ");
		writer.println(message.getMessage());
		writer.println();
		writer.flush();
	}
	
	private String getUsername(HttpServletRequest request){
		return request.getSession().getAttribute("username").toString();
	}
}
