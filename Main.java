package testOpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import testOpenCV.ToeicScanner;

public class Main {
	public static void main(String[] args) {
//		ToeicScanner scanner = new ToeicScanner();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat I = new Mat();
		Mat I_pre = new Mat();
		
		I = Imgcodecs.imread("/media/vmc/Data/VMC/Workspace/Toeic-Scores/Test_images/P5.jpg", 1);	
		ToeicScanner scanner = new ToeicScanner();
		I_pre = scanner.Process(I);	
		System.out.println(scanner.GetAnswers());
	}
}
