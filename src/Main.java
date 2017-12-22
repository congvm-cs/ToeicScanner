import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {
	public static void main(String[] args) {
//		ToeicScanner scanner = new ToeicScanner();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat I = new Mat();
		Mat I_pre = new Mat();
		
		I = Imgcodecs.imread("/media/vmc/Data/VMC/Workspace/Toeic-Scanner/Test_images/P1.jpg", 1);	
		ToeicScanner scanner = new ToeicScanner();
		I_pre = scanner.DetectROI(I);
		Imgcodecs.imwrite("Hi.png", I_pre);
		String x = scanner.AlignProcess();
		System.out.println(x);
		System.out.println(scanner.GetAnswers());
	}
}