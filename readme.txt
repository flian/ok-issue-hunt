just some toy test attempt.

socket server and client:
 server side, just start server socket and waiting client socket
 client side will try to setup connection with server side and keep it.
 purpose is try to reproduce some company net work issue will stuck client at "socket.read" for about 15 minutes.


 oracle database ping:
  last application, thread will be stuck at "socket.read" about 15 minutes. seems even "ping database" will stuck..
  so just a simple application, start a jdbc connection pool, try to ping database time to time.