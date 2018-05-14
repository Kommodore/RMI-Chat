import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientProxyImpl implements ClientProxy, ActionListener{
	
	private LoginWindow loginWindow;
	private ChatWindow chatWindow;

	public ClientProxyImpl(){
		String serverUrl = "rmi://localhost:1099/ChatServer";
		
		loginWindow = new LoginWindow();
		loginWindow.addLoginAction(this);
	

		try {
			ChatServer stub = (ChatServer)Naming.lookup(serverUrl);
			
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
	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("LOGIN")) {
			
			System.out.println("IP ADDRESS: " + loginWindow.getIpAddr());
			System.out.println("NAME: " + loginWindow.getName());
			System.out.println("PASSWORD: " + loginWindow.getPassword());
		
			loginWindow.killWindow();
			chatWindow = new ChatWindow();
			chatWindow.addChatAction(this);
			
		}else if(e.getActionCommand().equals("SETMSG")){
			chatWindow.addChatMsg(chatWindow.getChatMsg());
		}
	}

	public static void main(String[] args) {
		new ClientProxyImpl();
	}
}
