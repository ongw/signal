package signal;
import java.io.IOException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class LeapListener extends Listener {
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}
	public void onConnect(Controller controller) {
		System.out.println("Connected to Motion Sensor");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}
	public void onDisconnect(Controller controller) {
		System.out.println("Motion Sensor Disconnected");
	}
	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	long lastFrameID = 0;

	public void onFrame(Controller controller) {	
		Frame frame = controller.frame();
		Hand hand = frame.hands().rightmost();   
		FingerList finger = hand.fingers();
		if (!finger.isEmpty()) {
			checkAlpha(finger, hand);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void checkAlpha(FingerList finger, Hand hand) {
		float sum = 0;
		float MIN_PREC = 20;
		float Y_MIN_PREC = 50;
		float MAX_PREC = 20;
		
		// 0 = x, 1 = y, 2 = z
		float[][] fingerData = new float[5][3]; 

		//initialize values
		for (int i = 1; i < 5; i++)
			if (finger.get(i).isValid()) {
				fingerData[i][0] =  Math.abs(finger.get(i).tipPosition().minus(hand.palmPosition()).getX());
				fingerData[i][1] =  Math.abs(finger.get(i).tipPosition().minus(hand.palmPosition()).getY());
				fingerData[i][2] =  Math.abs(finger.get(i).tipPosition().minus(hand.palmPosition()).getZ());
			}

		//A
		if ((fingerData[1][0]+fingerData[2][0]+fingerData[3][0]+fingerData[4][0])/4 < MIN_PREC && 
				(fingerData[1][1]+fingerData[2][1]+fingerData[3][1]+fingerData[4][1])/4 < Y_MIN_PREC && 
				(fingerData[1][2]+fingerData[2][2]+fingerData[3][2]+fingerData[4][2])/4 < MIN_PREC)
			System.out.println("A");
		
		//B
		else if ((fingerData[0][0]+fingerData[0][2])/2 < MIN_PREC && 
				(fingerData[1][0]+fingerData[1][2] + fingerData[2][0]+fingerData[2][2] + fingerData[3][0]+
						fingerData[3][2] + fingerData[4][0]+fingerData[4][2])/10 < MIN_PREC &&
				(fingerData[1][1]+fingerData[2][1]+fingerData[3][1]+fingerData[4][1])/4 > MAX_PREC)
			System.out.println("B");
		else System.out.println("Z");

	}
}

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LeapListener listener = new LeapListener();
		Controller controller = new Controller();
		controller.addListener(listener);

		System.out.println("Press Enter to quit");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.removeListener(listener);
	}

}
