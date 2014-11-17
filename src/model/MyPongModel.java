package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class MyPongModel implements PongModel {
	
	private Map<BarKey, Integer> barPos = new HashMap<BarKey, Integer>();
	private Map<BarKey, Integer> barHeight = new HashMap<BarKey, Integer>();
	private String leftPlayer;
	private String rightPlayer;
	private Point BallPos;
	private String message;
	private Map<BarKey, Integer> score = new HashMap<BarKey, Integer>();
	private Dimension fieldSize;
	private Point ballPos = new Point();
	
	public MyPongModel(String leftPlayer, String rightPlayer){
		fieldSize = new Dimension();
		fieldSize.setSize(150, 100);
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		barPos.put(BarKey.LEFT, 0);
		barPos.put(BarKey.RIGHT, 0);
		barHeight.put(BarKey.LEFT, 0);
		barHeight.put(BarKey.RIGHT, 0);
		score.put(BarKey.LEFT, 0);
		score.put(BarKey.RIGHT, 0);
		ballPos.setLocation(100, 100);
		
		
		
	}
	
	public void compute(Set<Input> input, long delta_t){
		
	}
	
	 public int getBarPos(BarKey k){
		 return barPos.get(k);
	 }
	 
	 public int getBarHeight(BarKey k){
		 return barHeight.get(k);
	 }
	 
	 public Point getBallPos(){
		 return ballPos;
	 }
	 
	 public String getMessage(){
		 return "";
	 }
	 
	 public String getScore(BarKey k){
		 return "" + score.get(k);
	 }
	 
	 public Dimension getFieldSize(){
		 return fieldSize;
	 }
	 
	
}
