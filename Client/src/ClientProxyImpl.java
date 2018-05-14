import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import gui.*;

public class ClientProxyImpl implements ClientProxy, ActionListener, Serializable {
	
	private LoginWindow loginWindow;
	private ChatWindow chatWindow;
	private ChatServer stub;
	private String myUsername;
	private ChatProxy irgendwas;

	private ClientProxyImpl(){
		String serverUrl = "rmi://localhost:1099/ChatServer";
		
		loginWindow = new LoginWindow();
		loginWindow.addLoginAction(this);
		
		try {
			stub = (ChatServer)Naming.lookup(serverUrl);
			
		} catch (NotBoundException e) {
			System.err.println("Remote object not found. Is the server started?");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.err.println("URL not valid. Please check if "+serverUrl+" is a valid URL.");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.err.println("Please check if the server and registry are running.");
			e.printStackTrace();
		}
	}

	@Override
	public void receiveMessage(String username, String message) throws RemoteException {
		if(chatWindow == null){
			System.out.println("ChatWindow ist null");
		} else {
			chatWindow.addChatMsg(username, message);
			System.out.println("message ist: "+message);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("LOGIN")) {
			
			System.out.println("IP ADDRESS: " + loginWindow.getIpAddr());
			System.out.println("NAME: " + loginWindow.getName());
			System.out.println("PASSWORD: " + loginWindow.getPassword());
			myUsername = loginWindow.getName();
			
			loginWindow.killWindow();
			chatWindow = new ChatWindow();
			chatWindow.addChatAction(this);
			
			try {
				irgendwas = stub.subscribeUser(myUsername, this);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}else if(e.getActionCommand().equals("SETMSG")){
			String message1234 = chatWindow.getChatMsg();
			try {
				if(irgendwas != null){
					irgendwas.sendMessage(message1234);
				} else {
					System.out.println("Nullpointer");
				}
				
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ClientProxyImpl();
	}
}
