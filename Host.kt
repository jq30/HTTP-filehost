import java.net.*;
import java.io.*;

val PORT = 80;

fun main(args: Array<String>) {
  if (args.size < 1) {
    println("please specify file.");
    System.exit(1);
  }

  val file = File(args[0]);
  val fileName = file.getName();
  val fileSize = file.length();

  val server = ServerSocket(PORT);
  println("server listening on port $PORT...");
  val clientSocket = server.accept(); //blocks

  val responseHeader = "HTTP/1.1 200 OK\r\nContent-Length: $fileSize\r\nContent-Disposition: attachment; filename=\"$fileName\"\r\n\r\n";

  val read = FileInputStream(file);
  val write = clientSocket.getOutputStream();

  write.write(responseHeader.toByteArray());

  val buffer = ByteArray(1024) {0};
  var bytesread = read.read(buffer);
  while (bytesread != -1) {
    write.write(buffer, 0, bytesread);
    bytesread = read.read(buffer);
  }

  write.write("\r\n\r\n".toByteArray());

  try {
    while (true) {
      Thread.sleep(1000);
      write.write(0); //test connection
    }
  } catch (e: SocketException) {
    println("connection closed."); //broken pipe
  }

  println("done.");
}
