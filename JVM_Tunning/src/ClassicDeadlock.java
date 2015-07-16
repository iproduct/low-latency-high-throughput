
public class ClassicDeadlock {
 
    public static void main(String[] args) {
       SynchronizedObject left;
       SynchronizedObject right;
 
       LeftToRightGrabber leftToRight;
       RightToLeftGrabber rightToLeft;
 
       left  = new SynchronizedObject("left");
       right = new SynchronizedObject("right");
 
       leftToRight = new LeftToRightGrabber(left,right);
       rightToLeft = new RightToLeftGrabber(left,right);
 
       leftToRight.start();
       rightToLeft.start();
    }
 
 } // End ClassicDeadlock.
 
 /** An object containing a synchronized method. */
 class SynchronizedObject {
   public String name;
   public SynchronizedObject(String aName) {
      name = aName;
   }
   public synchronized void synchronizedMethod() {
       String message = name                        + 
                        " lock acquired by thread " +
                        Thread.currentThread().getName(); 
       System.out.println(message);
       System.out.flush();
   }
 } // End of SynchronizedObject.
 
 /** Base class for the Left and Right Grabber. */
 abstract class GrabberBase extends Thread {
    protected SynchronizedObject leftObject;
    protected SynchronizedObject rightObject;
 
    public GrabberBase(SynchronizedObject left,
                       SynchronizedObject right,
                       String             name
                      ) {
      super(name);
      leftObject  = left;
      rightObject = right; 
    } 
 } // End of GrabberBase.
 
 /** Class to grab locks right and then left. */
 class RightToLeftGrabber extends GrabberBase {
       public RightToLeftGrabber( SynchronizedObject left,
                                 SynchronizedObject right) {
           super(left, right, "RightToLeft");
       }
 
       public void run() {
          while(true) {
            grabRightToLeft();
          }
       }
 
       private void grabRightToLeft() {
           synchronized (rightObject) {
              leftObject.synchronizedMethod();
           }
       }
 } // End of RightToLeftGrabber.
 
 /** Class to grab locks left and then right. */
 class LeftToRightGrabber extends GrabberBase {
       public LeftToRightGrabber( SynchronizedObject left,
                                  SynchronizedObject right) {
           super(left, right, "LeftToRight");
       }
 
       public void run() {
          while(true) {
             grabLeftToRight();
          }
       }
  
       private void grabLeftToRight() {
           synchronized (leftObject) {
              rightObject.synchronizedMethod();
           }
       }
 } // End of LeftToRightGrabber.