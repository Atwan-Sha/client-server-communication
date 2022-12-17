# client-server-communication

Project assignment in computer networking university course at KTH.
The task was to implement client and server programs that use the TCP and HTTP protocols to communicate with each other.

- `TCPAsk` - Client that uses TCPClient to send/receive data to/from different servers. Takes server name, port number and optional string as input arguments.
- `HTTPEcho` - Server that echoes the incoming HTTP request back to the browser.
- `HTTPAsk` - Server processes HTTP request and responds to the browser.
- `ConcHTTPAsk` - Server processes multiple HTTP requests (asynchronously) and responds to the browser.
