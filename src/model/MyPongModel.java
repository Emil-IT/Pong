package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;



// TODO: Auto-generated Javadoc
/**
 * The Class MyPongModel.
 */
public class MyPongModel implements PongModel {
	
	/**
	 * The Class MyPoint.
	 */
	private class MyPoint extends Point{
		
		/** The x position. */
		double x;
		
		/** The y position. */
		double y;
	}
	
	/** The position of the bar. */
	private Map<BarKey, Integer> barPos = new HashMap<BarKey, Integer>();
	
	/** The height of the bar. */
	private Map<BarKey, Integer> barHeight = new HashMap<BarKey, Integer>();
	
	/** The name of the left player. */
	private String leftPlayer;
	
	/** The name of the right player. */
	private String rightPlayer;
	
	/** The message to be displayed on screen. */
	private String message;
	
	/** The players' score. */
	private Map<BarKey, Integer> score = new HashMap<BarKey, Integer>();
	
	/** The size of the field. */
	private Dimension fieldSize;
	
	/** The position of the ball. */
	private Point ballPos = new Point();
	
	/** The speed of the ball. */
	private int ballSpeed;
	
	/** The direction of the ball in radians */
	private double ballDir;
	private Map<BarKey, Integer> hitPos = new HashMap<BarKey, Integer>();
	private boolean sim = false;
	private boolean singelplayer = false;
	private boolean comVScom = true;
	
	/**
	 * Instantiates a new pong model.
	 *
	 * @param leftPlayer the name of the left player
	 * @param rightPlayer the name of the right player
	 */
	public MyPongModel(String leftPlayer, String rightPlayer){
		fieldSize = new Dimension();
		fieldSize.setSize(1500, 1000);
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		barPos.put(BarKey.LEFT, 100);
		barPos.put(BarKey.RIGHT, 100);
		resetBarHeight();
		resetScore();
		ballPos.setLocation(750, 500);
		resetBall();
		message =  leftPlayer + " VS " + rightPlayer;
		
		
		
		
	}
	
	/**
	 * Resets the score.
	 */
	private void resetScore(){
		score.put(BarKey.LEFT, 0);
		score.put(BarKey.RIGHT, 0);
	}
<<<<<<< HEAD
	 
 	/**
 	 * Resets height of the bar.
 	 */
 	private void resetBarHeight(){
		 barHeight.put(BarKey.LEFT, 200);
		 barHeight.put(BarKey.RIGHT, 200);
	 }
	
	/**
	 * Computes the positions of the bars and the ball based on input and how much time
	 * has passed since the last computation.
	 * @param input a set with players left and right and directions up and down
	 * @param delta_t the time passed since the last computation in milliseconds
	 * 
	 */
	public void compute(Set<Input> input, long delta_t){
		Iterator<Input> iterator = input.iterator();
		while(iterator.hasNext()){
			moveBar(iterator.next(), delta_t);
		}
		
		//------------AI------------------------------------------------------------------------------------------------------
		if(singelplayer){
			double posDir = ballDir % (2*Math.PI);
			if(posDir < 0){
				posDir += 2*Math.PI;
			}

			//---Com VS Com---
			if(comVScom){
				//Bollen rör sig åt vänster
				if(hitPos.get(BarKey.RIGHT) != -1 && posDir > Math.PI / 2 && (posDir) < 3 * Math.PI / 2){
					hitPos.put(BarKey.RIGHT, -1);
				}

				//Bollen rör sig åt höger
				if(hitPos.get(BarKey.RIGHT) == -1){
					if(posDir < Math.PI / 2 || posDir > 3 * Math.PI / 2){
						calculateHitPos(BarKey.RIGHT);
					}
				}
				//Ovanför beräknad träffyta plus marginal
				if(hitPos.get(BarKey.RIGHT) != -1 && getBarPos(BarKey.RIGHT) < hitPos.get(BarKey.RIGHT) - delta_t * 0.75){
					moveBar(new Input(BarKey.RIGHT, Input.Dir.DOWN), delta_t);

					//Under beräknad träffyta plus marginal	
				}else if(hitPos.get(BarKey.RIGHT) != -1 && getBarPos(BarKey.RIGHT) > hitPos.get(BarKey.RIGHT) + delta_t * 0.75){
					moveBar(new Input(BarKey.RIGHT, Input.Dir.UP), delta_t);
				}
			}
			//----------------
			
			//Bollen rör sig åt vänster
			if(hitPos.get(BarKey.LEFT) == -1 && posDir > Math.PI / 2 && (posDir) < 3 * Math.PI / 2){
				calculateHitPos(BarKey.LEFT);
			}

			//Bollen rör sig åt höger
			if(hitPos.get(BarKey.LEFT) != -1){
				if(posDir < Math.PI / 2 || posDir > 3 * Math.PI / 2){
					hitPos.put(BarKey.LEFT, -1);
				}
			}

			//Ovanför beräknad träffyta plus marginal
			if(hitPos.get(BarKey.LEFT) != -1 && getBarPos(BarKey.LEFT) < hitPos.get(BarKey.LEFT) - delta_t * 0.75){
				moveBar(new Input(BarKey.LEFT, Input.Dir.DOWN), delta_t);

				//Under beräknad träffyta plus marginal	
			}else if(hitPos.get(BarKey.LEFT) != -1 && getBarPos(BarKey.LEFT) > hitPos.get(BarKey.LEFT) + delta_t * 0.75){
				moveBar(new Input(BarKey.LEFT, Input.Dir.UP), delta_t);
			}
				//Innom träffområdet eller bollen rör sig åt höger

			//		else{
			//			if(ballPos.y > getBarPos(BarKey.LEFT) && Math.abs(ballPos.y - getBarPos(BarKey.LEFT))  > delta_t * 0.75 ){
			//				moveBar(new Input(BarKey.LEFT, Input.Dir.DOWN), delta_t);
			//			}else if((ballPos.y < getBarPos(BarKey.LEFT) && Math.abs(ballPos.y - getBarPos(BarKey.LEFT))  > delta_t * 0.75 )){
			//				moveBar(new Input(BarKey.LEFT, Input.Dir.UP), delta_t);
			//			}
			//		}
		}
		//--------------------------------------------------------------------------------------------------------------------
		
		moveBall(delta_t);
		
	}
	
	/**
	 * Moves the bar.
	 *
	 * @param input a set with players left and right and directions up and down
	 * @param delta_t the time passed since the last computation in milliseconds
	 */
	private void moveBar(Input input, long delta_t){
		int pos = getBarPos(input.key);
		if(input.dir == Input.Dir.UP && pos > barHeight.get(input.key)/2){
			pos -= 0.55 * delta_t;
		}
		if(input.dir == Input.Dir.DOWN && pos < 1000 - barHeight.get(input.key)/2){
			pos += 0.75 * delta_t;
		}
		setBarPos(input.key, pos);
	}
	
	/**
	 * Moves the ball.
	 *
	 * @param delta_t the time passed since the last computation in milliseconds
	 */
	private void moveBall(long delta_t){
		ballPos.x += ballSpeed * Math.cos(ballDir) * 0.03 * delta_t;
		ballPos.y -= ballSpeed * Math.sin(ballDir) * 0.03 * delta_t;
		
		//Bollen träffar högerkanten
		if(ballPos.x >= 1463){
			ballPos.x = 1463;
			if(Math.abs(ballPos.y - getBarPos(BarKey.RIGHT)) > barHeight.get(BarKey.RIGHT)/2 + 25 && !sim){
				score.put(BarKey.LEFT, score.get(BarKey.LEFT) + 1);
				if(score.get(BarKey.LEFT) == 10){
					message = leftPlayer + " wins!!!!"; 
					resetScore();
					resetBarHeight();
				}
				barHeight.put(BarKey.RIGHT, barHeight.get(BarKey.RIGHT) - 10);
				resetBall();
			}
			else if(!sim){
				ballSpeed++;
				ballDir = Math.PI - ballDir;
				double hit = (double)(ballPos.y - getBarPos(BarKey.RIGHT)) / (double)(barHeight.get(BarKey.RIGHT)/2.0);
				ballDir += hit/2;
			}
		}
		
		//Bollen träffar vänsterkanten
		if(ballPos.x <= 37){
			ballPos.x = 37;
			if(Math.abs(ballPos.y - getBarPos(BarKey.LEFT)) > barHeight.get(BarKey.LEFT)/2 + 25 && !sim){
				score.put(BarKey.RIGHT, score.get(BarKey.RIGHT) + 1);
				if(score.get(BarKey.RIGHT) == 10){
					message = rightPlayer + " wins!!!!"; 
					resetScore();
					resetBarHeight();
				}
				resetBall();
				barHeight.put(BarKey.LEFT, barHeight.get(BarKey.LEFT) - 10);
			}
			else if(!sim){
				ballSpeed++;
				ballDir = Math.PI - ballDir;
				double hit = (double)(ballPos.y - getBarPos(BarKey.LEFT)) / (double)(barHeight.get(BarKey.LEFT)/2.0);
				ballDir -= hit/2;
			}
		}
		
		//Bollen träffar golvet
		if(ballPos.y >= 975){
			if(hitPos.get(BarKey.LEFT) != -1 && !sim) calculateHitPos(BarKey.LEFT);
			if(hitPos.get(BarKey.RIGHT) != -1 && !sim) calculateHitPos(BarKey.RIGHT);
			ballPos.y = 975;
			ballDir *= -1;
		}
		//Bollen träffar taket
		if(ballPos.y <= 25){
			if(hitPos.get(BarKey.LEFT) != -1 && !sim) calculateHitPos(BarKey.LEFT);
			if(hitPos.get(BarKey.RIGHT) != -1 && !sim) calculateHitPos(BarKey.RIGHT);
			ballPos.y = 25;
			ballDir *= -1;
		}
	}
		
	 /**
 	 * Resets the ball to the center of the field and resets the balls speed. The ball is sent out in a
 	 * random direction between 45 and 135 degrees in either direction.
 	 */
 	private void resetBall(){
		 ballPos.setLocation(750, 500);
		 ballDir = Math.random()*2.0*Math.PI;
		 while(ballDir > Math.PI / 4 && ballDir < 3 * Math.PI / 4 || ballDir > 5 * Math.PI / 4 && ballDir < 7 * Math.PI / 4) ballDir = Math.random()*2.0*Math.PI;
		 ballSpeed = 20;
		 hitPos.put(BarKey.LEFT, -1);
		 hitPos.put(BarKey.RIGHT, -1);
	 }
	 
	 /** returns the position of the bar
	  * @param k the BarKey (LEFT or RIGHT)
 	 */
 	public int getBarPos(BarKey k){
		 return barPos.get(k);
	 }
	 
 	/**
 	 * Sets the poition of the bar to pos.
 	 *
 	 * @param k the BarKey (LEFT or RIGHT)
 	 * @param pos the position
 	 */
 	public void setBarPos(BarKey k, int pos){
		 barPos.put(k, pos);
	 }
	 
	 /** returns the height of the bar
	  * @param k the BarKey (LEFT or RIGHT)
 	  */
 	public int getBarHeight(BarKey k){
		 return barHeight.get(k);
	 }
	 
	 /** returns the position of the ball */
 	public Point getBallPos(){
		 return ballPos;
	 }
	 
	 /** returns the message string */
 	public String getMessage(){
		 return message;
	 }
	 
	 /** returns the score of the player
	  * @param k the BarKey (LEFT or RIGHT)
 	 */
 	public String getScore(BarKey k){
		 return "" + score.get(k);
	 }
	 
	 /** returns the size of the field
 	 */
 	public Dimension getFieldSize(){
		 return fieldSize;
	 }
	 
	 private void calculateHitPos(BarKey key){
		 double posDir = ballDir % (2*Math.PI);
			if(posDir < 0){
				posDir += 2*Math.PI;
			}
		 sim = true;
		 if((posDir) < 3*Math.PI / 2 && (posDir) > Math.PI / 2){
			 Point posCopy = new Point(ballPos.x, ballPos.y);
			 int speedCopy = ballSpeed;
			 double dirCopy = ballDir;
			 while(ballPos.x > 37){
				 moveBall(30);
			 }
			 int returnValue = ballPos.y;
			 ballPos = posCopy;
			 ballSpeed = speedCopy;
			 ballDir = dirCopy;
			 hitPos.put(key, returnValue);
		 }else{
			 Point posCopy = new Point(ballPos.x, ballPos.y);
			 int speedCopy = ballSpeed;
			 double dirCopy = ballDir;
			 while(ballPos.x < 1463){
				 moveBall(30);
			 }
			 int returnValue = ballPos.y;
			 ballPos = posCopy;
			 ballSpeed = speedCopy;
			 ballDir = dirCopy;
			 hitPos.put(key, returnValue);
		 }
		 sim = false;
	 }
	 
	
}
