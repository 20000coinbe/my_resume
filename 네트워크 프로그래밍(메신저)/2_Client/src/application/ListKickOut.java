package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ListKickOut {
	
	Main main = new Main();
	
	public static ArrayList<String> userList = new ArrayList<String>();
	
	public static boolean socketOut = false;
	
	private String setItemName;
	
	
	
	public void vote(Stage primaryStage) {

		Button btn = new Button("투표하기");
		ObservableList<String> data = FXCollections.observableArrayList();
		//FXCollections.observableArrayList();
	    ListView<String> listView = new ListView<String>(data);
	    
	    listView.setPrefSize(200, 250);
	    data.addAll(userList);
		System.out.println(userList);
		
		
	   
	    listView.setItems(data);
	    listView.getSelectionModel().selectedItemProperty().addListener(
	        (ObservableValue<? extends String> ov, String old_val, 
	            String new_val) -> {
	                System.out.println(new_val);
	                if(!btn.isPressed()) {
	                	btn.setOnAction(event -> {
	                		String setItemName = new_val;
	                		Stage p = new Stage();
	                		yesno(p);	
	                		if(kick(p, listView, setItemName) == true) {;
	                			data.remove(new_val);
	                			socketOut = true;
	                		}
	                	});
	                	
	                }
	                
	                
	                
	    });
	    ////////////////////////////////////


	    /////////////////////////////////////////

	    StackPane root = new StackPane();
	    root.getChildren().addAll(listView, btn);
	    root.setAlignment(listView, Pos.CENTER);
	    root.setAlignment(btn, Pos.BOTTOM_CENTER);
	    
	    primaryStage.setScene(new Scene(root, 200, 250));
	    primaryStage.show();
	}
	
	//유저리스트
	public void addArrayList(String user) {
		try {
			user.replace(":", "");
			if(userList.contains(user) == true) {
				return;
			}
			else
				userList.add(user);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void removeArrayList(String user) {
		try {
			userList.remove(user);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//강퇴기능
	public boolean kick(Stage primaryStage, ListView<String> listView, String user) {
		if(main.yes > main.no) {
			
			//리스트에서 제외 + 소켓종료
			
			//yes mo 0으로 초기화
			main.yes = 0;
			main.no = 0;
			System.out.println("찬성 : " + main.yes);
			System.out.println("반대 : " + main.no);
			
			return true;
		}
		
		else 
			System.out.println("투표가 부결되었습니다 \n");
			return false;
	}
	
	public void yesno(Stage primaryStage3) {
		
		
		HBox hbox = new HBox();
	    
	    Button yes_btn = new Button("찬성");
	    yes_btn.setPrefWidth(300);
	    Button no_btn = new Button("반대");
	    no_btn.setPrefWidth(300);
	    
	    hbox.getChildren().addAll(yes_btn, no_btn);
	    hbox.requestFocus();
		primaryStage3.setTitle("투표");
	    primaryStage3.setScene(new Scene(hbox, 50, 100));
	    primaryStage3.show();
	    
	    if(!yes_btn.isPressed()) {
	    	yes_btn.setOnAction(event -> {
	    		main.yes++;
	    		primaryStage3.close();
	    		System.out.println(main.yes + "\t" + main.no );
	    		primaryStage3.close();
	    	});
	    }
	    
	    if(!no_btn.isPressed()) {
	    	no_btn.setOnAction(event -> {
	    		main.no++;
	    		primaryStage3.close();
	    		System.out.println(main.yes + "\t" + main.no );
	    		primaryStage3.close();
	    	});
	    	
	    }
	    
	}
}
