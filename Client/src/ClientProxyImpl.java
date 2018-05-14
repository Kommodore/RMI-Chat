import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import gui.*;

public class ClientProxyImpl implements ClientProxy, ActionListener {
	
	private LoginWindow loginWindow;
	private ChatWindow chatWindow;
	private ChatServer stub;
	private String username;
	private ChatProxy irgendwas;
	private ClientProxy bla;

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
		chatWindow.addChatMsg(username, message);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("LOGIN")) {
			
			System.out.println("IP ADDRESS: " + loginWindow.getIpAddr());
			System.out.println("NAME: " + loginWindow.getName());
			System.out.println("PASSWORD: " + loginWindow.getPassword());
			username = loginWindow.getName();
			try {
				irgendwas = stub.subscribeUser(username, bla);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
			
			loginWindow.killWindow();
			chatWindow = new ChatWindow();
			chatWindow.addChatAction(this);
			
		}else if(e.getActionCommand().equals("SETMSG")){
			chatWindow.addChatMsg(chatWindow.getChatMsg());
			try {
				irgendwas.sendMessage(chatWindow.getChatMsg());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ClientProxyImpl();
	}
}
