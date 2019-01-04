import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class snakePanel extends JPanel{

	private int[][] map = new int[20][15];		//map:整張地圖，其數值用以表示蛇身的生命時間
	private int p_x,p_y,r_x=-1,r_y=-1;			//p:表示得點物品,r:表示重置速度物品
	private int direction = 2;					//direction:表示方向
	private int dir_temp = 2;					//dir_temp:方向暫存器
	private int x = 12,y = 7;					//x,y:蛇頭的位置
	private int length = 5;						//length:蛇的長度
	private int sequence = 0;					//sequence:表示分數累加
	private int rcount;							//rcount:重置速度物品的暫存器
	private boolean gameover = false;			//gameover:表示遊戲是否結束
	private int score = 0;						//score:分數
	Action A = new Action();			//A:ActionListener
	private Timer t = new Timer(300, A);		//t:timer
	
	private void reset(){
		for (int i=0;i<20;i++)
			for(int j=0;j<15;j++)
				map[i][j]=0;			//將整張地圖變為0
		r_x=-1;r_y=-1;					//將重置速度的物品清除
		direction = 2;					//將方向設定為右
		dir_temp = 2;					//將方向暫存器設定為右
		x = 12;y = 7;					//將蛇頭位置設定在中間偏右
		length = 5;						//將蛇身長度設定為5
		sequence = 0;					//將分數累加氣設定為0
		score = 0;						//將分數設定為0
		gameover = false;				//遊戲沒有結束
		t.setDelay(300);				//將速度設定為30秒一格
		rcount=30;						//第一個重置器將固定在30回合後出現
	}
	
	public void start(){
		
		reset();						//重設所有數值
		
		for(int i=length;i>0;i--)
			map[x+i-length][y]=i;		//將蛇放置在地圖上，從蛇頭開始向左依序為5,4,3,2,1的生命時間
		do{
			p_x=(int)(Math.random()*20);
			p_y=(int)(Math.random()*15);
		}while(map[p_x][p_y]>0);		//隨機放置得點物品於場上，但不會放在蛇身
		t.start();						//讓計時器開始運作
		repaint();						//重畫地圖
	}
	
	private void move(){
		direction = dir_temp;			//讓方向變為方向暫存器的値
		switch(direction){				//檢查方向有沒有超出範圍，若超出範圍則gameover
		case 0:							//若仍在範圍內則朝著方向前進一格
			if(x==0)
				gameover=true;
			else
				x--;
			break;
		case 1:
			if(y==14)
				gameover=true;
			else
				y++;
			break;
		case 2:
			if(x==19)
				gameover=true;
			else
				x++;
			break;
		case 3:
			if(y==0)
				gameover=true;
			else
				y--;
			break;
		}
		
		rcount--;						//使計數器遞減，到0時發生重置速度物品出現或消失
		if(rcount==0)
		do{
			if(r_x==-1 && r_y==-1){		//若場上並沒有重置速度物品，則使之出現
				r_x=(int)(Math.random()*20);
				r_y=(int)(Math.random()*15);
				rcount=(int)(Math.random()*20+20);
										//設定20-40回合後消失
			}
			else{						//反之則消失
				r_x=-1;
				r_y=-1;
				rcount=(int)(Math.random()*20+20);
										//設定20-40回合後出現
			}
			
		}while( ( r_x!=-1 && r_y!=-1 ) && ( map[r_x][r_y]>0 || (r_x == p_x && r_y == p_y ) ) );
										//確定物品出現在蛇身或得點物品上
										//但為了避免讀取map[-1][-1]錯誤，所以加入不存在時不判斷的條件
		
		if(x == r_x && y == r_y){		//如果吃到重置速度物品
			t.setDelay(300);			//使速度重置為0.3秒一格
			sequence=0;					//使分數累加變為0
			r_x=-1;
			r_y=-1;						//使重置速度物品消失
			rcount=(int)(Math.random()*20+20);
										//設定20-40回合後出現
		}
			
		if(x == p_x && y == p_y){		//若吃到得點物品
			length++;					//長度+1
			sequence++;					//分數累計+1
			score+=sequence;			//分數提高為分數累計得值
			t.setDelay((int)(t.getDelay()*0.95238));
										//速度提高5%
			for(int i=0;i<20;i++)
				for(int j=0;j<15;j++)
					if(map[i][j]>0)
						map[i][j]++;	//使現存的蛇身生命時間+1
			do{
				p_x=(int)(Math.random()*20);
				p_y=(int)(Math.random()*15);
			}while(map[p_x][p_y]>0 || (p_x == r_x && p_y == r_y));	
										//放置下一個得點物品
		}
		
		for (int i=0;i<20;i++)
			for(int j=0;j<15;j++)
				map[i][j]--;			//不論什麼情況，都使生命時間-1
		
		if(map[x][y]>0)
			gameover=true;				//若撞到蛇身(生命時間>0表示有蛇身)則gameover
		
		map[x][y]=length;				//使蛇頭位置的生命時間設定為長度的値
		
		repaint();						//重畫地圖
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//畫外框
		g.drawRect(2, 2, 20*12+2,15*12+2);
				
		if(!gameover){
			//畫蛇身
			for (int i=0;i<20;i++)
				for(int j=0;j<15;j++)
					if(map[i][j]>0)
						g.drawOval(i*12+4, j*12+4, 10, 10);
			//畫得點物品					
			g.drawRect(p_x*12+4, p_y*12+4, 10, 10);
			//畫重置速度物品，稜形為四條斜線的組合				
			if(r_x>-1 && r_y>-1){
				g.drawLine(r_x*12+10, r_y*12+4, r_x*12+16, r_y*12+10);
				g.drawLine(r_x*12+16, r_y*12+10, r_x*12+10, r_y*12+16);
				g.drawLine(r_x*12+10, r_y*12+16, r_x*12+4, r_y*12+10);
				g.drawLine(r_x*12+4, r_y*12+10, r_x*12+10, r_y*12+4);
			}
			//顯示長度、分數和速度的値
			g.drawString("length:"+String.valueOf(length), 5, 16*12+5);
			g.drawString("score:"+String.valueOf(score), 6*12+5, 16*12+5);
			g.drawString("speed:"+String.valueOf((int)(300.0/t.getDelay()*100)+"%"), 12*12+5, 16*12+5);
		}
		else{
			//若遊戲結束，則顯示其訊息和分數
			g.drawString("GAME OVER", 7*12, 6*12);
			g.drawString("your score is "+String.valueOf(score),6*12+6,8*12);
			g.drawString("pass F2 to restart", 6*12, 10*12);
			t.stop();
		}
	}
	
	private class Action implements KeyListener,ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//此觸發器會被Timer t觸發，觸發時就進行一次移動
			move();
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			//當鍵盤被按下時，檢查其值是否為←↓→↑
			//利用暫存器儲存將改變的方向，避免連續轉向時發生的exception
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				if(direction!=2)	//不會使方向變為反向(右變左)
					dir_temp=0;
				break;
			case KeyEvent.VK_DOWN:
				if(direction!=3)	//不會使方向變為反向(上變下)
					dir_temp=1;
				break;
			case KeyEvent.VK_RIGHT:
				if(direction!=0)	//不會使方向變為反向(左變右)
					dir_temp=2;
				break;
			case KeyEvent.VK_UP:
				if(direction!=1)	//不會使方向變為反向(下變上)
					dir_temp=3;
				break;
			case KeyEvent.VK_F2:	//按下F2時，重新開使遊戲
				start();
				break;
			}
				
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
