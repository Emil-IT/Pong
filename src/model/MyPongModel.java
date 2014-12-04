package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;



public class MyPongModel implements PongModel {
	private class MyPoint extends Point{
		double x;
		double y;
	}
	
	private Map<BarKey, Integer> barPos = new HashMap<BarKey, Integer>();
	private Map<BarKey, Integer> barHeight = new HashMap<BarKey, Integer>();
	private String leftPlayer;
	private String rightPlayer;
	private String message;
	private Map<BarKey, Integer> score = new HashMap<BarKey, Integer>();
	private Dimension fieldSize;
	private Point ballPos = new Point();
	private int ballSpeed;
	private double ballDir;
	private Map<BarKey, Integer> hitPos = new HashMap<BarKey, Integer>();
	private boolean sim = false;
	private boolean singelplayer = false;
	private boolean comVScom = true;
	
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
	private void resetScore(){
		score.put(BarKey.LEFT, 0);
		score.put(BarKey.RIGHT, 0);
	}
	private void resetBarHeight(){
		barHeight.put(BarKey.LEFT, 200);
		barHeight.put(BarKey.RIGHT, 200);
	}

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
				//Bollen r�r sig �t v�nster
				if(hitPos.get(BarKey.RIGHT) != -1 && posDir > Math.PI / 2 && (posDir) < 3 * Math.PI / 2){
					hitPos.put(BarKey.RIGHT, -1);
				}

				//Bollen r�r sig �t h�ger
				if(hitPos.get(BarKey.RIGHT) == -1){
					if(posDir < Math.PI / 2 || posDir > 3 * Math.PI / 2){
						calculateHitPos(BarKey.RIGHT);
					}
				}
				//Ovanf�r ber�knad tr�ffyta plus marginal
				if(hitPos.get(BarKey.RIGHT) != -1 && getBarPos(BarKey.RIGHT) < hitPos.get(BarKey.RIGHT) - delta_t * 0.75){
					moveBar(new Input(BarKey.RIGHT, Input.Dir.DOWN), delta_t);

					//Under ber�knad tr�ffyta plus marginal	
				}else if(hitPos.get(BarKey.RIGHT) != -1 && getBarPos(BarKey.RIGHT) > hitPos.get(BarKey.RIGHT) + delta_t * 0.75){
					moveBar(new Input(BarKey.RIGHT, Input.Dir.UP), delta_t);
				}
			}
			//----------------
			
			//Bollen r�r sig �t v�nster
			if(hitPos.get(BarKey.LEFT) == -1 && posDir > Math.PI / 2 && (posDir) < 3 * Math.PI / 2){
				calculateHitPos(BarKey.LEFT);
			}

			//Bollen r�r sig �t h�ger
			if(hitPos.get(BarKey.LEFT) != -1){
				if(posDir < Math.PI / 2 || posDir > 3 * Math.PI / 2){
					hitPos.put(BarKey.LEFT, -1);
				}
			}

			//Ovanf�r ber�knad tr�ffyta plus marginal
			if(hitPos.get(BarKey.LEFT) != -1 && getBarPos(BarKey.LEFT) < hitPos.get(BarKey.LEFT) - delta_t * 0.75){
				moveBar(new Input(BarKey.LEFT, Input.Dir.DOWN), delta_t);

				//Under ber�knad tr�ffyta plus marginal	
			}else if(hitPos.get(BarKey.LEFT) != -1 && getBarPos(BarKey.LEFT) > hitPos.get(BarKey.LEFT) + delta_t * 0.75){
				moveBar(new Input(BarKey.LEFT, Input.Dir.UP), delta_t);
			}
				//Innom tr�ffomr�det eller bollen r�r sig �t h�ger

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
	
	private void moveBall(long delta_t){
		ballPos.x += ballSpeed * Math.cos(ballDir) * 0.03 * delta_t;
		ballPos.y -= ballSpeed * Math.sin(ballDir) * 0.03 * delta_t;
		
		//Bollen tr�ffar h�gerkanten
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
		
		//Bollen tr�ffar v�nsterkanten
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
		
		//Bollen tr�ffar golvet
		if(ballPos.y >= 975){
			if(hitPos.get(BarKey.LEFT) != -1 && !sim) calculateHitPos(BarKey.LEFT);
			if(hitPos.get(BarKey.RIGHT) != -1 && !sim) calculateHitPos(BarKey.RIGHT);
			ballPos.y = 975;
			ballDir *= -1;
		}
		//Bollen tr�ffar taket
		if(ballPos.y <= 25){
			if(hitPos.get(BarKey.LEFT) != -1 && !sim) calculateHitPos(BarKey.LEFT);
			if(hitPos.get(BarKey.RIGHT) != -1 && !sim) calculateHitPos(BarKey.RIGHT);
			ballPos.y = 25;
			ballDir *= -1;
		}
	}
		
	 private void resetBall(){
		 ballPos.setLocation(750, 500);
		 ballDir = Math.random()*2.0*Math.PI;
		 while(ballDir > Math.PI / 4 && ballDir < 3 * Math.PI / 4 || ballDir > 5 * Math.PI / 4 && ballDir < 7 * Math.PI / 4) ballDir = Math.random()*2.0*Math.PI;
		 ballSpeed = 20;
		 hitPos.put(BarKey.LEFT, -1);
		 hitPos.put(BarKey.RIGHT, -1);
	 }
	 
	 public int getBarPos(BarKey k){
		 return barPos.get(k);
	 }
	 public void setBarPos(BarKey k, int pos){
		 barPos.put(k, pos);
	 }
	 
	 public int getBarHeight(BarKey k){
		 return barHeight.get(k);
	 }
	 
	 public Point getBallPos(){
		 return ballPos;
	 }
	 
	 public String getMessage(){
		 return message;
	 }
	 
	 public String getScore(BarKey k){
		 return "" + score.get(k);
	 }
	 
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
