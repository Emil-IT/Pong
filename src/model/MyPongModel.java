package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


public class MyPongModel implements PongModel {
	
	private Map<BarKey, Integer> barPos = new HashMap<BarKey, Integer>();
	private Map<BarKey, Integer> barHeight = new HashMap<BarKey, Integer>();
	private String leftPlayer;
	private String rightPlayer;
	private Point BallPos;
	private String message = "hej!!!";
	private Map<BarKey, Integer> score = new HashMap<BarKey, Integer>();
	private Dimension fieldSize;
	private Point ballPos = new Point();
	private int ballSpeed = 3;
	private double ballDir;
	
	public MyPongModel(String leftPlayer, String rightPlayer){
		fieldSize = new Dimension();
		fieldSize.setSize(150, 100);
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		barPos.put(BarKey.LEFT, 10);
		barPos.put(BarKey.RIGHT, 10);
		barHeight.put(BarKey.LEFT, 20);
		barHeight.put(BarKey.RIGHT, 20);
		score.put(BarKey.LEFT, 0);
		score.put(BarKey.RIGHT, 0);
		ballPos.setLocation(75, 50);
		resetBall();
		
		
		
		
	}
	
	public void compute(Set<Input> input, long delta_t){
		Iterator<Input> iterator = input.iterator();
		while(iterator.hasNext()){
			moveBar(iterator.next(), delta_t);
		}
		moveBall(delta_t);
		
	}
	
	private void moveBar(Input input, long delta_t){
		int pos = getBarPos(input.key);
		if(input.dir == Input.Dir.UP && pos > 10){
			pos -= 0.05 * delta_t;
		}
		if(input.dir == Input.Dir.DOWN && pos < 90){
			pos += 0.07 * delta_t;
		}
		setBarPos(input.key, pos);
	}
	
	private void moveBall(long delta_t){
		ballPos.x += ballSpeed * Math.cos(ballDir);
		ballPos.y += ballSpeed * Math.sin(ballDir);
		if(ballPos.x >= 150){
			if(Math.abs(ballPos.y - getBarPos(BarKey.RIGHT)) > 10){
				score.put(BarKey.LEFT, score.get(BarKey.LEFT) + 1);
				resetBall();
			}else ballDir = Math.PI - ballDir;
		}
		if(ballPos.x <= 0){ 
			if(Math.abs(ballPos.y - getBarPos(BarKey.LEFT)) > 10){
				score.put(BarKey.RIGHT, score.get(BarKey.RIGHT) + 1);
				resetBall();
			}else ballDir = Math.PI - ballDir;
		}
		if(ballPos.y > 100 || ballPos.y < 0){
			ballDir = 2*Math.PI - ballDir;
			System.out.println("" + ballDir);
		}
	}
		
	 private void resetBall(){
		 ballPos.setLocation(75, 50);
		 ballDir = Math.random()*2.0*Math.PI;
		 while(ballDir > Math.PI / 4 && ballDir < 3 * Math.PI / 4 || ballDir > 5 * Math.PI / 4 && ballDir < 7 * Math.PI / 4) ballDir = Math.random()*2.0*Math.PI;
		 ballSpeed = 3;
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
	 
	
}
