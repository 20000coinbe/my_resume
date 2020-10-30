package application;
	
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Stage.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.geometry.Insets.*;


public class Main extends Application {
		
	public static ExecutorService threadPool; //쓰레드에 효과적인 관리라이브러리, 쓰레드숫자에 제한을 두어 서버성능 저하를 막을 수 있음
	public static Vector<Client> clients  = new Vector<Client>();
	private HashMap<String, ObjectOutputStream> hm;
	
	ServerSocket serverSocket; //서버가 연결을 기다리는 소켓
	
	public void startServer(String IP, int port) { //bind-accpet
		//클라이언트의 연결을 기다림
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP,port));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(!serverSocket.isClosed()) 
				stopServer();
			return;
		}
		
		//클라이언트가 접속할때까지 기다림
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
				try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[클라이언트 connect] " + socket.getRemoteSocketAddress()
											+ " : " + Thread.currentThread().getName());		
				}catch(Exception e) {
					if(!serverSocket.isClosed()) 
						stopServer();
					break;
					
					}
				}
			}
		};
		//쓰레드풀 초기화

		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	
		
	public void stopServer() {
		//서버중지
		try {
			//현재 작동중인 소켓닫기
			Iterator<Client> it = clients.iterator();
			while(it.hasNext()) {
				Client client = it.next();
				client.socket.close();
				it.remove();
			}
			//서버소켓도 닫기
			if(serverSocket != null && !serverSocket.isClosed()) 
				serverSocket.close();
			
			//쓰레드 풀도 종료
			if(threadPool != null && !threadPool.isShutdown()) 
				threadPool.shutdown();
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void userIn(String str) {
		
	}
	
	//UI생성
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		TextArea textArea = new TextArea();
		
		textArea.setEditable(false);
		root.setCenter(textArea);	
		//textArea.setFont
		
		Button toggleButton = new Button("시작하기");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);
		
		
		String IP = "127.0.0.1";
		int port = 7523;
		
		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("시작하기")) {
				startServer(IP, port);
				Platform.runLater(() -> {
					String message = String.format("[서버 시작]\n", IP, port);
					textArea.appendText(message);
					textArea.appendText("[DB관리자 : TRUE] \n");
					toggleButton.setText("종료하기");
				});
			}
			else {
				stopServer();
				Platform.runLater(() -> {
					String message = String.format("[서버 종료]\n", IP, port);
					textArea.appendText(message);
					textArea.appendText("[DB관리자 : FALSE]\n");
					toggleButton.setText("시작하기");
				});				
			}
				
		});
	
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[채탱서버]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	
	}
	
	
	public void sendSecretMsg(String msg) {
		int begin = msg.indexOf(":") + 1;
		int end = msg.indexOf(":", begin);
		
		if(end != -1) {
			String id = msg.substring(begin, end);
			String s_msg = msg.substring(end+1);
			ObjectOutputStream oos = hm.get(id);
			try {
				if(oos != null) {
					oos.writeObject(id + "님이 귓속말을 보냈습니다 : " + msg);
					oos.flush();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	//프로그램진입
	public static void main(String[] args) {
		DBConnection connection = new DBConnection();
		System.out.println("관리자여부 : " + connection.isAdmin("admin", "admin"));
		launch(args);
		
	}
}

	

