import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHTTPServer {
    private static class Task implements Runnable {
        private final Socket socket;

        Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                // 解析请求
                Request request = Request.parse(is);
                System.out.println(request);


                if (request.path.equalsIgnoreCase("/")) {
                  //  String body = "<h1>我的网页</h1>";
                    String body = "alert('必须关掉');";
                    byte[] bodyBuffer = body.getBytes("UTF-8");
                    StringBuilder response = new StringBuilder();
                    response.append("HTTP/1.0 200 OK\r\n");

                    //Content-Type 按照格式分析
                  //  response.append("Content-Type: text/plain; charset=UTF-8\r\n");    //文本
                  //  response.append("Content-Type: text/html; charset=UTF-8\r\n");
                    response.append("Content-Type: application/javascript; charset=UTF-8\r\n");
                    response.append("Content-Length: ");
                    response.append(bodyBuffer.length);
                    response.append("\r\n");
                    response.append("\r\n");

                    os.write(response.toString().getBytes("UTF-8"));
                    os.write(bodyBuffer);
                    os.flush();
                }else if(request.path.equalsIgnoreCase("/run")) {
                    //请求  '/' 去拿脚本文件 根路径告诉返回一个js文件(Content-Type) alert是js的一个弹窗处理
                    String body = "<script src='/'></script>";
                    byte[] bodyBuffer = body.getBytes("UTF-8");
                    StringBuilder response = new StringBuilder();
                    response.append("HTTP/1.0 200 OK\r\n");

                    response.append("Content-Type: text/html; charset=UTF-8\r\n");
                    response.append("Content-Length: ");
                    response.append(bodyBuffer.length);
                    response.append("\r\n");
                    response.append("\r\n");

                    os.write(response.toString().getBytes("UTF-8"));
                    os.write(bodyBuffer);
                    os.flush();
                    //若请求的是banjia 则，进行跳转到新的Location :/run中
                }else if(request.path.equalsIgnoreCase("/banjia")){
                    StringBuilder response = new StringBuilder();
                    response.append("HTTP/1.0 307 Temporary Redirect\r\n");
                    response.append("Location: /run\r\n");
                    response.append("\r\n");
                    os.write(response.toString().getBytes("UTF-8"));
                    os.flush();
                }else {
                    StringBuilder response = new StringBuilder();
                    response.append("HTTP/1.0 404 Not Found\r\n");
                    response.append("\r\n");
                    os.write(response.toString().getBytes("UTF-8"));
                    os.flush();
                }
                // 处理业务
            /*
            String body = "<h1>一切正常</h1>";
            // 拼接响应
            Response response = Response.build(os);
            // 发送响应
            response.println(body);
            response.flush();
             */
                socket.close();
            } catch (Exception e) {}
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        while (true) {
            Socket socket = serverSocket.accept();
            pool.execute(new Task(socket));
        }
    }
}
