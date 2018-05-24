import javax.servlet.AsyncContext;
import java.rmi.RemoteException;

class Client {
	private ChatProxy proxyObject;
	private ClientProxyImpl chatServer;
	private AsyncContext newMessagesRequest;
	
	Client() throws RemoteException {
		this.proxyObject = null;
		this.chatServer = new ClientProxyImpl();
		this.newMessagesRequest = null;
	}
	
	void connect(String server_ip, String username) throws RemoteException {
		this.chatServer.connect(server_ip);
		proxyObject = chatServer.stub.subscribeUser(username, chatServer);
	}
	
	ChatProxy getProxyObject() {
		return proxyObject;
	}
	
	ClientProxyImpl getChatServer() {
		return chatServer;
	}
	
	AsyncContext getNewMessagesRequest() {
		return newMessagesRequest;
	}
	
	void setNewMessagesRequest(AsyncContext newMessagesRequest) {
		this.newMessagesRequest = newMessagesRequest;
	}
}
