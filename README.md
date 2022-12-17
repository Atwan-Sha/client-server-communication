# client-server-communication

Project assignment in computer networking university course at KTH.
The task was to implement client and server programs that use the TCP and HTTP protocols to communicate with each other.

- `TCPAsk` - Client that uses TCPClient to send/receive data to/from different servers. Takes hostname, port number and optional string as input arguments.
- `HTTPEcho` - Server that echoes the incoming HTTP request back to the browser. Takes port number as input argument.
- `HTTPAsk` - Server that processes incoming HTTP request (query) and responds to the browser. Program takes port number as input argument and also hostname/portnumber/string queries embedded in HTTP request.
- `ConcHTTPAsk` - Multithreaded version of HTTPAsk, processes multiple HTTP requests (concurrently) and responds to the browser. Uses MyRunnable to create new threads.
