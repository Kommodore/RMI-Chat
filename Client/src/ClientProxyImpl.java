import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientProxyImpl implements ClientProxy{
	
	@Override
	public void receiveMessage(String username, String message) throws RemoteException {
	
	}
	
	public static void main(String[] args) {
		String serverUrl = "rmi://localhost:1099/ChatServer";
		
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
}
