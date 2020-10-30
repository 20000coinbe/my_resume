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
		
	public static ExecutorService threadPool; //�����忡 ȿ������ �������̺귯��, ��������ڿ� ������ �ξ� �������� ���ϸ� ���� �� ����
	public static Vector<Client> clients  = new Vector<Client>();
	private HashMap<String, ObjectOutputStream> hm;
	
	ServerSocket serverSocket; //������ ������ ��ٸ��� ����
	
	public void startServer(String IP, int port) { //bind-accpet
		//Ŭ���̾�Ʈ�� ������ ��ٸ�
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
		
		//Ŭ���̾�Ʈ�� �����Ҷ����� ��ٸ�
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
				try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[Ŭ���̾�Ʈ connect] " + socket.getRemoteSocketAddress()
											+ " : " + Thread.currentThread().getName());		
				}catch(Exception e) {
					if(!serverSocket.isClosed()) 
						stopServer();
					break;
					
					}
				}
			}
		};
		//������Ǯ �ʱ�ȭ

		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	
		
	public void stopServer() {
		//��������
		try {
			//���� �۵����� ���ϴݱ�
			Iterator<Client> it = clients.iterator();
			while(it.hasNext()) {
				Client client = it.next();
				client.socket.close();
				it.remove();
			}
			//�������ϵ� �ݱ�
			if(serverSocket != null && !serverSocket.isClosed()) 
				serverSocket.close();
			
			//������ Ǯ�� ����
			if(threadPool != null && !threadPool.isShutdown()) 
				threadPool.shutdown();
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void userIn(String str) {
		
	}
	
	//UI����
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		TextArea textArea = new TextArea();
		
		textArea.setEditable(false);
		root.setCenter(textArea);	
		//textArea.setFont
		
		Button toggleButton = new Button("�����ϱ�");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);
		
		
		String IP = "127.0.0.1";
		int port = 7523;
		
		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("�����ϱ�")) {
				startServer(IP, port);
				Platform.runLater(() -> {
					String message = String.format("[���� ����]\n", IP, port);
					textArea.appendText(message);
					textArea.appendText("[DB������ : TRUE] \n");
					toggleButton.setText("�����ϱ�");
				});
			}
			else {
				stopServer();
				Platform.runLater(() -> {
					String message = String.format("[���� ����]\n", IP, port);
					textArea.appendText(message);
					textArea.appendText("[DB������ : FALSE]\n");
					toggleButton.setText("�����ϱ�");
				});				
			}
				
		});
	
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ä�ʼ���]");
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
					oos.writeObject(id + "���� �ӼӸ��� ���½��ϴ� : " + msg);
					oos.flush();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	//���α׷�����
	public static void main(String[] args) {
		DBConnection connection = new DBConnection();
		System.out.println("�����ڿ��� : " + connection.isAdmin("admin", "admin"));
		launch(args);
		
	}
}

	

