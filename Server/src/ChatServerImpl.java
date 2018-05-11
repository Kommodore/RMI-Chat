import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatServerImpl implements ChatServer, ChatProxy{
	
	@Override
	public ChatProxy subscribeUser(String username, ClientProxy handle) throws RemoteException {
		return null;
	}
	
	@Override
	public ChatProxy unsubscribeUser(String username) throws RemoteException {
		return null;
	}
	
	@Override
	public void sendMessage(String message) throws RemoteException {
	
	}
	
	public static void main(String[] args) {
		Registry registry = null;
		
		try {
			registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.err.println("Remote Exception occurred while getting registry:");
			e.printStackTrace();
		}
		
		if(registry == null){
			System.err.println("Cannot find registry. Exiting");
			System.exit(1);
		}
		
		ChatServerImpl export = new ChatServerImpl();
		try {
			ChatServer stub = (ChatServer)UnicastRemoteObject.exportObject(export, 0);
			registry.rebind("ChatServer", stub);
			System.out.println("Chat server registered as \"ChatServer\"");
		} catch (RemoteException e) {
			System.err.println("Remote Exception occurred while registering the server.");
			e.printStackTrace();
			System.exit(0);
		}
	}
}
