package application;

import java.awt.event.KeyEvent;
import java.util.Calendar;

public class ChatingBot {
	
	String init;
	String[] wordArray;
	
	Calendar cal = Calendar.getInstance();
	
    int year = cal.get(Calendar.YEAR);
    int mon = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);

    String to_date = year + "�� "+ mon+"�� "+day+"��";
    String to_time = hour + "�� "+ min+"�� "+sec+"��";
    
	String[][] chatBot = {
			//standard greetings
			{"@�ȳ�", "@�ȳ��ϼ���", "@����", "@����"},
			{"@�ȳ��ϼ���","@�ݰ����ϴ�"},
			//question greetings
			{"@�̸�", "@�̸��� ����?","@�� �̸��� ����?","@������?"},
			{"@���� �ȳ�Bot�Դϴ�"},
			//
			{"@����"},
			{"@ê���� ����ϱ� ���ؼ���  @��  �ٿ��ּ��� \n ---> ex) @ī�װ�"},
			//yes
			{"@ī�װ�"},
			{"@�λ�, @�̸�, @��¥, @�ð�, @����"},
			//��¥
			{"@��¥"}, 
			{'@' + to_date},
			//�ð�
			{"@�ð�"}, 
			{'@'+ to_time},
			//default
			{"����տ� @�� �ٿ��ּ���", "ê������", "@ī�װ�	��� �ĺ�����"}
		};
	
	public boolean inArray(String in, String[] str) {
		boolean match = false;
		for(int i=0; i < str.length; i++) {
			if(str[i].equals(in)) {
				match=true;
			}
		}
		
		return match;
	}
	
	
	
	public String setting(String str) {
		boolean settingSwitch = false;
		
		String name = "�ȳ�Bot";
		String bot_msg = null;
		
		init = str;
		init = init.trim();
		
		wordArray = init.split(": ", 2);
		String sp1_msg, sp2_msg;
		sp1_msg = wordArray[0];
		sp2_msg = wordArray[1];
		System.out.println(sp1_msg);
		System.out.println(sp2_msg);
		
		if(sp2_msg.charAt(0) == '@') {
			
			while(	sp2_msg.charAt(sp2_msg.length() -1 ) == '!' ||
					sp2_msg.charAt(sp2_msg.length() -1 ) == '.' ||
					sp2_msg.charAt(sp2_msg.length() -1 ) == '?') 
			{
				sp2_msg = sp2_msg.substring(0, sp2_msg.length()-1);
			}
			
			
			byte response = 0;

			//string�迭�̶� ��Ī
			int j=0;
			while(response == 0) {
				if(inArray(sp2_msg.toLowerCase(), chatBot[j*2])) {
					response = 2;
					int a = (int) Math.floor(Math.random() * chatBot[(j*2)+1].length);
					System.out.println("--> " + name + "\t" + chatBot[(j*2)+1][a]);
					bot_msg = "----> " + name + ":  " + chatBot[(j*2)+1][a] +'\n';
				}
				j++;
				if(j*2 == chatBot.length-1 && response == 0) {
					response = 1;
				}
			}
		
			if(response == 1) {
				int r = (int) Math.floor(Math.random() * chatBot[chatBot.length - 1].length);
				System.out.println("--> " + name + "\t" + chatBot[chatBot.length-1][r]);
				bot_msg = "----> " + name + ":  " + chatBot[chatBot.length-1][r] +'\n';
			}
			settingSwitch = true;
			
		}
		if(settingSwitch == true) {
			init = bot_msg;
			System.out.println(init);
		}
		System.out.println(init);
		return init;
		
	}	

}