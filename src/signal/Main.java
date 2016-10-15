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
			for (int i = 0; i < 5; i++)
				if (finger.get(i).isValid())
					System.out.println((i+1)+ ": " + (((Pointable) finger.get(i)).tipPosition().minus(hand.palmPosition())));
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void checkAlpha() {

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
