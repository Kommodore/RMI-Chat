import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ChatServerImpl implements ChatServer, ChatProxy{
	private HashMap<String, ClientProxy> clients = new HashMap<>();
	
	@Override
	public ChatProxy subscribeUser(String username, ClientProxy handle) throws RemoteException {
		clients.put(username, handle);
		System.out.println(username+" registered.");
		return null;
	}
	
	@Override
	public ChatProxy unsubscribeUser(String username) throws RemoteException {
		clients.remove(username);
		System.out.println(username+" unregistered.");
		return null;
	}
	
	@Override
	public void sendMessage(String message){
		clients.forEach((s, clientProxy) -> {
			try {
				clientProxy.receiveMessage(s, message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		});
		System.out.println("Send message "+message);
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
