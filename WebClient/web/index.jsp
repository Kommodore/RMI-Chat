<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<title>RMI Chat Webclient</title>
		<link rel="stylesheet" href="assets/main.css">
		<link rel="stylesheet" href="assets/spinner.css">
	</head>
	<body>
		<main>
			<section id="error">
				<div class="error-header"><h1>Error</h1><a href="#" class="error-close">X</a></div>
				<p class="error-message"></p>
			</section>
			<section id="login">
				<h1>Login</h1>
				<form action="" method="post" id="login-form">
					<input type="text" id="server" name="server" placeholder="Server" required>
					<input type="text" id="username" name="username" placeholder="Username" required>
					<input type="submit" value="Verbinden...">
				</form>
			</section>
			<section id="chat">
				<div id="chat-wrapper">
					<div id="chat-info">
						<p>IP: <span class="server-ip"></span></p>
						<p>Username: <span class="my-username"></span></p>
						<button id="chat-logout">Logout</button>
					</div>
					<div id="chat-main">
						<div class="chat-message system">
							Chatraum betreten...
						</div>
					</div>
					<div id="message-box">
						<form action="" method="post" id="message-form">
							<input type="text" id="new-message" title="New message" required>
							<div class="send-button">
								<input type="submit" id="send-message" value="">
							</div>
						</form>
					</div>
				</div>
			</section>
			<div class="spinner" id="loading-spinner">
				<div class="double-bounce1"></div>
				<div class="double-bounce2"></div>
			</div>
		</main>
		<script src="assets/jquery.min.js"></script>
		<script>
			$(document).ready(function($){
			    var username = "";
			    var server_ip = "";
			    var spinner = $("div#loading-spinner");
			    var error = $("section#error");
			    $("a.error-close").on("click", function(){
			       error.fadeOut("fast");
			    });

			   $("#login-form").on("submit", function(event){
			       event.preventDefault();
			       username = $("section#login input#username").val();
			       server_ip = $("input#server").val();
                   $("section#login :input").attr("disabled", true);
				   spinner.fadeIn("fast");
			       $.post('/api', {
			           action: "subscribe",
				       username: username,
				       server_ip: server_ip
			       }).done(function(){
			           $(".my-username").text(username);
                       $(".server-ip").text(server_ip);
			           $("section#login").fadeOut("fast");
			           $("section#chat").fadeIn("fast", function(){
                           spinner.fadeOut("fast");
			           });
			       }).fail(function(xhr){
			           console.log("Error while connecting: "+JSON.stringify(xhr));
                       $("section#login :input").attr("disabled", false);
                       $(".error-message").text("Error while connecting to the server: "+xhr.status);
                       error.fadeIn("fast");
                       spinner.fadeOut("fast");
				   });
			   });

			   $("#chat-logout").on("click", function(){
			       $.post('/api', {
			           action: "unsubscribe"
			       }).done(function(){
                       $("section#chat").fadeOut("fast");
                       $('section#login input[type="text"]').val("");
                       $("section#login :input").attr("disabled", false);
                       $("section#login").fadeIn("fast", function(){
                           spinner.fadeOut("fast");
                       });
                   }).fail(function(xhr){
                       console.log("Error while disconnecting: "+JSON.stringify(xhr));
                       $(".error-message").text("Error while disconnecting from the server: "+xhr.status);
                       error.fadeIn("fast");
                       spinner.fadeOut("fast");
                   });
			   });

                $("#message-form").on("submit", function(event){
                    event.preventDefault();
                    $.post('/api', {
                        action: "send_message",
	                    message: $("div#message-box input#new-message").val()
                    }).done(function(){
                        $("div#message-box input#new-message").val("");
                    }).fail(function(xhr){
                        console.log("Error while sending message: "+JSON.stringify(xhr));
                        if(xhr.status === 401){
                            $(".error-message").text("Could not authenticate. Please log out and back in. "+xhr.status);
                        } else {
                            $(".error-message").text("Error while sending message to server. Please retry. "+xhr.status);
                        }
                        error.fadeIn("fast");
                    });
                });
			});
		</script>
	</body>
</html>
