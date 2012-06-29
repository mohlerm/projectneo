package ch.baws.projectneo.effects;

import java.util.Random;

import android.util.Log;
import ch.baws.projectneo.minions.*;
import ch.baws.projectneo.JoystickSnakeActivity;
import ch.baws.projectneo.R;
import ch.baws.projectneo.frameGenerator.Frame;



public class HumanSnakePlayer extends Effect{
	private static class Snake extends Thread{
		private int SPEED = 600;
		private class BodyPart{ // Represents one Pixel of a Snake
			int x;
			int y;
			BodyPart nextBody;
			public BodyPart(int x, int y){
				this.x = x;
				this.y = y;
			}
			public boolean equals(BodyPart body){
				return ((this.x==body.x) && (this.y==body.y));
			}
		}
		private class Food{ // Represents a Food Pixel
			int x;
			int y;
			/**
			 * Generates a random new Food Pixel
			 */
			public void generate(){
				x = rand.nextInt(8);
				y = rand.nextInt(8);
				BodyPart temp=head;
				while(temp.nextBody != null){ // Test if the food would spawn inside the snake
					if(temp.x==x && temp.y == y){
						generate();
						temp = head;
					}else{
						temp = temp.nextBody;
					}
				}
			}
		}

		
		private static final int COLOR_SNAKE = Frame.NEO_RED;  //Color representing the Snake
		private static final int COLOR_FOOD  = Frame.NEO_BLUE; //Color for the Food piece
		
		
		
		private boolean GAMEOVER = false;
		private BodyPart head;
		private Food food;
		
		private int score;
		private Random rand;
		private int[][] array;
		
		private Dir dir; /** Current direction to go */
		
		public Snake(){
			rand = new Random();
			array = Utils.getEmpty8x8();
			create();
		}
		
		private void create(){
			head=new BodyPart(4, 4);
			dir = Dir.LEFT;
			score =0;
			BodyPart temp1,temp2;
			temp2 = head;
			for(int i=1;i<4;i++){ // Initialise a new snake in the middle
				temp1 = new BodyPart(head.x+i,head.y);
				temp2.nextBody = temp1;
				temp2 = temp1;
			}
			food = new Food();
			food.generate();
		}
		
		public boolean isValidMove(BodyPart body){
			//Test if OutOfBounds
			if(body.x > 7 || body.x < 0 || body.y >7 || body.y < 0){
				if(D) Log.d(TAG,"Oh noes! Out of Bounds!");
				return false;
			}
			
			//Test if eating itself
			BodyPart temp=head.nextBody;
			while(temp!=null){
				if(body.equals(temp)){
					if(D) Log.d(TAG,"Iech, I bit myself at " + body.x +"/"+body.y);
					return false;
				}
				temp = temp.nextBody;
			}	

			return true;
		}
		
		public void run(){
			BodyPart next = null;
			while(!GAMEOVER){
				
				
				//catched the food?
				if(head.x==food.x && head.y==food.y){
					score++;
					food.generate(); // new food
					if(SPEED>50) this.setSpeed(this.SPEED-20); //increase Speed!
				}else{ // loose the tail pixel
					BodyPart temp=head;
					while(temp.nextBody.nextBody!=null){ 
						temp = temp.nextBody;
					}
					temp.nextBody=null;
				}
				
				switch(dir){
				case    UP:
					next = new BodyPart(head.x-1,head.y); //u
					break;
				case RIGHT:
					next = new BodyPart(head.x,head.y+1); //r
					break;
				case  DOWN:
					next = new BodyPart(head.x+1,head.y); //d
					break;
				case  LEFT:
					next = new BodyPart(head.x,head.y-1); //l
					break;
				}
				
				next.nextBody = head;
				head = next;
				
				//Game Over ?
				if(!isValidMove(head)){
					// fancy Game over effect
					GAMEOVER = true;
					if(D) Log.d(TAG,"GameOver!");
					break; // EXIT The loop, GAME OVER!
				}
				
				
				// Since the snake doesn't change till the next tick, we might as
				// well compute the array once and for all, so the call getArray() doesn't need to do anything
				int[][] tmparray = Utils.getEmpty8x8();
				BodyPart temp = head;
				if(D) Log.d(TAG,"GET ARRAY: starting");
				tmparray[food.x][food.y] = COLOR_FOOD;
				while(temp!=null){
					if(D) Log.d(TAG,"GET ARRAY: fill 1  temp.x:" + temp.x + " temp.y:" + temp.y);
					tmparray[temp.x][temp.y] = COLOR_SNAKE; //RED
					if(D) Log.d(TAG,"GET ARRAY: fill 2");
					temp = temp.nextBody;
					if(D) Log.d(TAG,"GET ARRAY: fill 3");
				}
				array = tmparray;
				if(D) Log.d(TAG,"GET ARRAY: finish, set food");
				
				//Speed of Snake :)
				try {
					sleep(SPEED);
				} catch (InterruptedException e) {	}

			}
			// Display Score
			//array = FancyScore.getArray(score); TODO
			if(D) Log.d(TAG,"Snake Died.");
		}
		
		

		public int[][] getArray(){
			return array;
		}
		
		public void exit(){
			GAMEOVER = true;
			this.interrupt();
		}

		public void setDir(Dir dir) {
			this.dir = dir;
		}
		
		public void setSpeed(int speed){
			this.SPEED = speed;
		}
	}
	
	private static final boolean D = true; // Debug Flag
	private static final String TAG = "HUMAN SNAKE";
	public static enum Dir{RIGHT,LEFT,UP,DOWN};
	
	private Snake snake;
	
	public HumanSnakePlayer(){
		super("ThomasR", "Snake!");
		this.icon = R.drawable.ic_eff_snake; //TODO cool icon!
		this.activity = JoystickSnakeActivity.class; //HumanSnakeActivity.class;
		snake = new Snake();
	}
	
	@Override
	public int[][] getArray() {
		return snake.getArray();
	}
	
	public void setDir(Dir dir) {
		if(snake.GAMEOVER){
			if(D) Log.d(TAG, "GameOver, new snake.");
		}else{
			snake.setDir(dir);
		}
	}
	
	public void newGame(){
		if(D) Log.d(TAG, "GameOver, new snake.");
		snake.exit();
		snake = new Snake();
		snake.start();
	}
	
	
	public int getScore(){
		return snake.score;
	}

	@Override
	public void run() {
		snake.start();
		while(!EXIT){
			
			try {
				sleep(40000000);
			} catch (InterruptedException e) {}
		}

	}
}
