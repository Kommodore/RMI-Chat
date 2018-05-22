class Message {
	private String username;
	private String message;
	
	Message(String username, String message) {
		this.username = username;
		this.message = message;
	}
	
	String getUsername() {
		return username;
	}
	
	String getMessage() {
		return message;
	}
}
