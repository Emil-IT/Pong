import static org.junit.Assert.*;
import org.junit.Test;
import model.*;


public class MyPongModelTest {



	@Test
	public void testComputeMultiplayer() {
		MyPongModel model = new MyPongModel("P1", "P2");
		model.singleplayer = false;
	}

	

}
