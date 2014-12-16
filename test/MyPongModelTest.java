import static org.junit.Assert.*;

import org.junit.Test;

import model.*;

import java.awt.Dimension;
import java.awt.Point;


public class MyPongModelTest {



	@Test
	public void testMoveBallFromInitPos() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.setSingleplayer(false);
		double dir = testModel.getBallDir();
		Point pos = testModel.getBallPos();
		Point newPos =  new Point((int) (pos.x + testModel.getBallSpeed() * Math.cos(dir) * 0.03 * 30), (int) (pos.y -  testModel.getBallSpeed() * Math.sin(dir) * 0.03 * 30));
		testModel.moveBall(30);
		assertTrue(newPos.equals(testModel.getBallPos()));	
	}
	@Test
	public void testMoveBallChangeDirWhenHitCeiling() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.ballDir = Math.PI / 4;
		testModel.ballPos.setLocation(800, 1);
		double newDir =  testModel.ballDir * -1;
		testModel.moveBall(30);
		assertTrue(testModel.ballDir == newDir);
	}
	@Test
	public void testMoveBallChangeDirWhenHitFloor() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.ballDir = -Math.PI / 4;
		testModel.ballPos.setLocation(800, 999);
		double newDir =  testModel.ballDir * -1;
		testModel.moveBall(30);
		assertTrue(testModel.ballDir == newDir);
	}
	@Test
	public void testMoveBallRightGetsPointWhenBallHitLeftSide() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.ballDir = Math.PI;
		testModel.ballPos.setLocation(40, 500);
		testModel.moveBall(30);
		assertTrue(testModel.score.get(BarKey.RIGHT) == 1);
	}
	@Test
	public void testMoveBallLeftGetsPointWhenBallHitRightSide() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.ballDir = 0;
		testModel.ballPos.setLocation(1460, 500);
		testModel.moveBall(30);
		assertTrue(testModel.score.get(BarKey.LEFT) == 1);
	}
	@Test
	public void testMoveBallBounceWhenHitRightBar() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.ballDir = 0;
		testModel.ballPos.setLocation(1460, 100);
		testModel.moveBall(30);
		assertTrue(testModel.ballDir == Math.PI);
	}
	@Test
	public void testMoveBallBounceWhenHitLeftBar() {
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.ballDir = Math.PI;
		testModel.ballPos.setLocation(40, 100);
		testModel.moveBall(30);
		assertTrue(testModel.ballDir == 0);
	}
	
	//----------------moveBar-------------------------------
	@Test
	public void testMoveBarRightBarDownFromInitPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		int oldPos = testModel.getBarPos(BarKey.RIGHT);
		int newPos = oldPos + (int)(0.75*30);
		testModel.moveBar(new Input(BarKey.RIGHT, Input.Dir.DOWN), 30);
		assertTrue(testModel.getBarPos(BarKey.RIGHT) == newPos);
	}
	@Test
	public void testMoveBarRightBarUpFromInitPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		int oldPos = testModel.getBarPos(BarKey.RIGHT);
		testModel.moveBar(new Input(BarKey.RIGHT, Input.Dir.UP), 30);
		assertTrue(testModel.getBarPos(BarKey.RIGHT) == oldPos);
	}
	@Test
	public void testMoveBarLeftBarDownFromInitPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		int oldPos = testModel.getBarPos(BarKey.LEFT);
		int newPos = oldPos + (int)(0.75*30);
		testModel.moveBar(new Input(BarKey.LEFT, Input.Dir.DOWN), 30);
		assertTrue(testModel.getBarPos(BarKey.LEFT) == newPos);
	}
	@Test
	public void testMoveBarLeftBarUpFromInitPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		int oldPos = testModel.getBarPos(BarKey.LEFT);
		testModel.moveBar(new Input(BarKey.LEFT, Input.Dir.UP), 30);
		assertTrue(testModel.getBarPos(BarKey.LEFT) == oldPos);
	}
	
	//Bottom pos
	@Test
	public void testMoveBarRightBarDownFromBottomPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.barPos.put(BarKey.RIGHT, 900);
		int oldPos = testModel.getBarPos(BarKey.RIGHT);
		testModel.moveBar(new Input(BarKey.RIGHT, Input.Dir.DOWN), 30);
		assertTrue(testModel.getBarPos(BarKey.RIGHT) == oldPos);
	}
	@Test
	public void testMoveBarRightBarUpFromBottomPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.barPos.put(BarKey.RIGHT, 900);
		int oldPos = testModel.getBarPos(BarKey.RIGHT);
		int newPos = (int)(oldPos - (0.55*30));
		testModel.moveBar(new Input(BarKey.RIGHT, Input.Dir.UP), 30);
		assertTrue(testModel.getBarPos(BarKey.RIGHT) == newPos);
	}
	@Test
	public void testMoveBarLeftBarDownFromBottomPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.barPos.put(BarKey.LEFT, 900);
		int oldPos = testModel.getBarPos(BarKey.LEFT);
		testModel.moveBar(new Input(BarKey.LEFT, Input.Dir.DOWN), 30);
		assertTrue(testModel.getBarPos(BarKey.LEFT) == oldPos);
	}
	@Test
	public void testMoveBarLeftBarUpFromBottomPos(){
		MyPongModel testModel = new MyPongModel("P1", "P2");
		testModel.barPos.put(BarKey.LEFT, 900);
		int oldPos = testModel.getBarPos(BarKey.LEFT);
		int newPos = (int)(oldPos - (0.55*30));
		testModel.moveBar(new Input(BarKey.LEFT, Input.Dir.UP), 30);
		assertTrue(testModel.getBarPos(BarKey.LEFT) == newPos);
	}
	

}
