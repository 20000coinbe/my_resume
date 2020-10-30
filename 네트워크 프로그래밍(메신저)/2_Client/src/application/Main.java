package application;
	
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit.BoldAction;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.application.Platform;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.geometry.*;



public class Main extends Application {
	
	Socket socket;
	TextArea textArea;

	public static int yes, no = 0;
	
	//�̸�Ƽ�� �̹���
//	FileInputStream inputstream = new FileInputStream("C:\\Users\\USER\\eclipse-workspace\\2_Client\\src\\application\\emo1.png"); 
//	Image img = new Image("emo1.png"); 
	
	public void startClient(String IP, int PORT) {
		Thread thread = new Thread() {
			
		public void run() {
				try {
					socket = new Socket(IP, PORT);
					receive();
				} catch (Exception e) {
					// TODO: handle exception
					if(!socket.isClosed()) {
						stopClient();
						System.out.println("[���� ���� ����]");
						Platform.exit();
						}
					}
				}
			};
			thread.start();
	}
	
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//�����κ��� �޽����� ���޹���
	public void receive() {
		while(true) {
			
			try {
				
				InputStream is = socket.getInputStream();
				byte[] buffer = new byte[512];	
				int length = is.read(buffer); 
				if(length == -1) throw new IOException();
				String msg = new String(buffer, 0, length, "UTF-8");
//				System.out.println(msg);
				Platform.runLater(() -> {
				
						if(msg.charAt(0) != '-') {
							BlackList bl = new BlackList();
							String msg2 = bl.blackList(msg); //������Ʈ�� �������� Ȯ��
							textArea.appendText(msg2 + '\n');//���޹��� �޽��� ����
						}
						if(msg.charAt(0) == '-') {
							
							textArea.appendText(msg + '\n');
						}


				});
			} catch (Exception e) {
				// TODO: handle exception
				stopClient(); 
				break;
			}
		}
	}
	
	//������ �޽����� ����
	public void send(String msg) {
		Thread th = new Thread() {
//			BlackList bl = new BlackList();
			
			public void run() {
				try {

//					String msg2 = bl.blackList(msg);
					OutputStream out = socket.getOutputStream();
					byte[] buffer = msg.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		th.start();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
						
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		
		TextField userName = new TextField();
		userName.setPrefWidth(150);
		userName.setPromptText("�г����� �Է��ϼ���");
		HBox.setHgrow(userName, Priority.ALWAYS);
		
		TextField IPText = new TextField("127.0.0.1");
		TextField PortText = new TextField("7523");
		PortText.setPrefWidth(80);
		
		hbox.getChildren().addAll(userName, IPText, PortText);
		root.setTop(hbox);
		
		textArea = new TextArea();
		textArea.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12));
		textArea.setEditable(false);

		root.setCenter(textArea);
		
		
		TextField input = new TextField();
		input.setPrefWidth(Double.MAX_VALUE);
		input.setDisable(true);
		
		input.setOnAction(event -> {
			send(userName.getText() + ": " + input.getText() + "\n"); //
			input.setText("");
			input.requestFocus();
		});
		
		Button sendButton = new Button("������");
		sendButton.setDisable(true);
		
		sendButton.setOnAction(event -> {
			send(userName.getText() + ": " + input.getText() + "\n");
//			addVector(userName.getText());
			input.setText("");
			input.requestFocus();
		});
		
		
		
		
		Button connectionButton = new Button("�����ϱ�");
		connectionButton.setOnAction(event -> {
			if(connectionButton.getText().equals("�����ϱ�")) {
				
				int port = 7523;
				try {
					port = Integer.parseInt(PortText.getText());
				}catch (Exception e) {
					e.printStackTrace();
				}
				startClient(IPText.getText(), port);
				Platform.runLater(() -> {
					textArea.appendText("[ä�ù� ����]\n");
					textArea.appendText("[ê�����]�� ����Ͻ÷���	" + "@����	�� �Է��غ����� \n");
				});
				connectionButton.setText("�����ϱ�");
				input.setDisable(false);
				sendButton.setDisable(false);
				input.requestFocus();
				
			}
			else {
				stopClient();
				Platform.runLater(() -> {
					textArea.appendText("[ä�ù� ����]\n");
				});
				connectionButton.setText("�����ϱ�");
				input.setDisable(true);
				sendButton.setDisable(true);
				
			}
		});
		

			
		//������
		Button btn_kick = new Button("�����ϱ�");
		btn_kick.setPrefWidth(300);
		if(!btn_kick.isPressed()) {
			btn_kick.setOnAction(event -> {
				Stage primaryStage2 = new Stage();
				ListKickOut kick = new ListKickOut();
				kick.vote(primaryStage2);
									
			});
		}
		
		
		//�ӼӸ�
		
		
		//�̸�Ƽ��

		
			
		/////
		Button btn_emo = new Button("�ӼӸ�");
		btn_emo.setPrefWidth(300);
		btn_emo.setOnAction(event -> {
			ImageView iv = new ImageView();
				
				
			
		});
		
//		

			
		
		///////////////////
		
		
		HBox hbox2 = new HBox();
		
		hbox2.getChildren().addAll(btn_kick, btn_emo);
		
		BorderPane pane = new BorderPane();
		pane.setLeft(connectionButton);
		pane.setCenter(input);
		pane.setRight(sendButton);
		pane.setBottom(hbox2);
		
		root.setBottom(pane);
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ä�� Ŭ���̾�Ʈ]");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
		
		connectionButton.requestFocus();
		
	}
	
	public void SocketOut() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	
	public static void main(String[] args) {
		launch(args);

	}
	
	
	
}

