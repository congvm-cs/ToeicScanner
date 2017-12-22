import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.video.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;
import static org.opencv.imgproc.Imgproc.equalizeHist;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.line;
import static org.opencv.imgproc.Imgproc.morphologyEx;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.imgproc.Imgproc.threshold;
import static org.opencv.imgproc.Imgproc.warpAffine;

public class ToeicScanner {
	private Mat inputImage = new Mat();                   // store configured input image
	private Mat colorInputImage = new Mat();                // store configured input image with color
	private Mat templateImage = new Mat();                  // store configured template image
	private Mat drawRoiImage = new Mat();
	private List<MatOfPoint> squares = new ArrayList<>();  // store points of squares (bounding box)
	private List<Integer> xGrid = new ArrayList<>();                  		// store bubble answer by x axis
	private List<Integer> yGrid = new ArrayList<>();          		// store bubble answer by y axis
	
	private int circleRadius = 5;                   // store bubble answer radius
	private char[] answerKey = {'A', 'B', 'C', 'D', 'X'};
	private MatOfPoint approxf1 = new MatOfPoint();		// store largest contour Points
	private List<Character> answers = new ArrayList<>();               // store bubble answer from paper
	private int choosenThreshold = 50;

	//============================================================================================//
    public List<Character> GetAnswers(){
        return this.answers;
    };
    
    public String AlignProcess() {
    	Mat resultAlign = new Mat();
    	try {
		resultAlign = this.Align(this.drawRoiImage);
    	
		resultAlign = this.DrawVerticalGrid(resultAlign, true);
		resultAlign = this.DrawHorizontalGrid(resultAlign, true);
		
		this.DetectAnswer(resultAlign);
		resultAlign = this.DrawCircle(resultAlign);
    	}
    	catch(Exception e) {    		
    		return "False";
    	}
    	return "True";
    }
    
	public Mat DetectROI(Mat img){
		this.LoadTemplate();
		this.LoadInputImage(img);
		Mat result = new Mat();
		this.drawRoiImage = this.Preprocess(this.inputImage);
		result = this.Detect(this.drawRoiImage);
		return result;
	};
		
	private void LoadTemplate(){
		/*
		 * 
		 * */
        Mat template_temp = Imgcodecs.imread("/media/vmc/Data/VMC/Workspace/Toeic-Scanner/Scanner/assets/templates/T2.jpg");
	    resize(template_temp, template_temp, new Size(1280, 768));
	    Imgproc.cvtColor(template_temp, template_temp, Imgproc.COLOR_BGR2GRAY);
	    this.templateImage = template_temp;
	};
 
	private void LoadInputImage(Mat img){
		Mat imgColor = new Mat();
        resize(img, imgColor, new Size(1280, 768));
        this.colorInputImage = imgColor;
        
        Mat imgGray = new Mat();
        Imgproc.cvtColor(imgColor, imgGray, Imgproc.COLOR_BGR2GRAY);
        this.inputImage = imgGray;	
    };

    
	private Mat Preprocess(Mat img){
	    Mat blurredImage = new Mat();
	    Imgproc.GaussianBlur(img, blurredImage, new Size(5, 5), 0);
	    Mat thresholdImage = new Mat();
	    double otsu_thresh_val = threshold(	blurredImage,
								            thresholdImage,
								            0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
	
	    Mat edgeImage = new Mat();
	    double high_thresh_val  = otsu_thresh_val;
	    double lower_thresh_val = otsu_thresh_val * 0.2;
	    Imgproc.Canny(blurredImage, edgeImage, lower_thresh_val, high_thresh_val);
	
	    Mat sel = getStructuringElement(Imgproc.MORPH_RECT, new Size(11, 11));
	    Mat morpImage = new Mat();
	    morphologyEx(edgeImage, morpImage, Imgproc.MORPH_CLOSE, sel);
	    return morpImage;
	};
  
  
	//=======================
    private Mat Detect(Mat img){
    	
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        
        /// Find contours
        Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        
        // Find max contour area and its index
        double maxContourArea=0;
        int indexContour = 0;
        for (int idx = 0; idx < contours.size(); idx++) {
            Mat contour = contours.get(idx);
            double contourArea = Imgproc.contourArea(contour);
            if (contourArea > maxContourArea){
                maxContourArea = contourArea;
                indexContour = idx;
            }
        }
        
        // 	For each contour found
        MatOfPoint2f approx = new MatOfPoint2f();
        //Processing on mMOP2f1 which is in type MatOfPoint2f
        double approxDistance = Imgproc.arcLength(new MatOfPoint2f(contours.get(indexContour).toArray()), true)*0.02;
        Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(indexContour).toArray()), approx, approxDistance, true);
        
        
        if(approx.toArray().length == 4)
        {
        	approx.convertTo(this.approxf1, CvType.CV_32S);
        	this.squares.add(this.approxf1);
        	System.out.println(this.squares);
        }
        
        Imgproc.polylines(this.colorInputImage, this.squares, true, new Scalar(0, 255, 0), 4);
        
        for(int i = 0; i < this.approxf1.total(); i++)
        {
        	System.out.println(this.approxf1.get(i, 0));
            Imgproc.circle(this.colorInputImage, new Point(this.approxf1.get(i, 0)), 15, new  Scalar(0, 0, 255), 2);
            Imgproc.circle(this.colorInputImage, new Point(this.approxf1.get(i, 0)), 5,new  Scalar(0, 0, 255), -1);
        }
        return this.colorInputImage;
    };

    
    private Mat Align(Mat img){
        //=========================SORT POINT IN ORDER======================//
        MatOfPoint tempSquares = this.approxf1;
        List<Point> squarePoint = new ArrayList<Point>();
        
        for(int i = 0; i < tempSquares.total(); i++)
        {
        	Point ptnInApproxf2 = new Point(tempSquares.get(i, 0));
        	squarePoint.add(ptnInApproxf2);
        }
       
        Point tl = new Point();
        Point br = new Point();
        Point tr = new Point();
        Point bl = new Point();
        // Find Top Left Point
        int indexMinSum = 0;
        int minSum = (int) (squarePoint.get(0).x + squarePoint.get(0).y) ;
        
        for(int i = 0; i < squarePoint.size(); i++)
        {
            int x = (int) squarePoint.get(i).x;
            int y = (int) squarePoint.get(i).y;
            if((x + y) <= minSum)
            {
                minSum = x + y;
                tl = new Point(x, y);
                indexMinSum = i;
            }
        }
        squarePoint.remove(indexMinSum);
        
        // Find Bottom Right Point
        int indexMaxSum = 0;
        int maxSum = (int) (squarePoint.get(0).x + squarePoint.get(0).y);
        for(int i = 0; i< squarePoint.size(); i++)
        {
            int x = (int) squarePoint.get(i).x;
            int y = (int) squarePoint.get(i).y;
            if((x + y) >= maxSum)
            {
                maxSum = (x + y);
                br = new Point(x, y);
                indexMaxSum = i;
            }
        }
        squarePoint.remove(indexMaxSum);
        

        // Find Bottom Left Point and Top Right Point
        if(squarePoint.get(0).x < squarePoint.get(1).x)
        {
            int x1 = (int) squarePoint.get(0).x;
            int y1 = (int) squarePoint.get(0).y;
            bl = new Point(x1, y1);

            int x2 = (int) squarePoint.get(1).x;
            int y2 = (int) squarePoint.get(1).y;
            tr = new Point(x2, y2);
        }
        else
        {
        	int x1 = (int) squarePoint.get(0).x;
            int y1 = (int) squarePoint.get(0).y;
            tr = new Point(x1, y1);

            int x2 = (int) squarePoint.get(1).x;
            int y2 = (int) squarePoint.get(1).y;
            bl = new Point(x2, y2);
        }

        // Adding Anchor Point to transform
        List<Point> dstPoints1= new ArrayList<Point>();
        dstPoints1.add(tl);
        dstPoints1.add(tr);
        dstPoints1.add(br);
        dstPoints1.add(bl);

        Mat inputRect = Converters.vector_Point2f_to_Mat(dstPoints1);

        //=========================COMPUTE ARRAY FOR ALIGNMENT======================//
        // compute the width of the new image, which will be the
        // maximum distance between bottom-right and bottom-left
        // x-coordiates or the top-right and top-left x-coordinates
        double widthA = sqrt((br.x - bl.x)*(br.x - bl.x) + (br.y - bl.y)*(br.y - bl.y));
        double widthB = sqrt((tr.x - tl.x)*(tr.x - tl.x) + (tr.y - tl.y)*(tr.y - tl.y));

        double maxWidth = max(widthA, widthB);

        // compute the height of the new image, which will be the
        // maximum distance between the top-right and bottom-right
        // y-coordinates or the top-left and bottom-left y-coordinates
        double heightA = sqrt((tr.x - br.x)*(tr.x - br.x) + (tr.y - br.y)*(tr.y - br.y));
        double heightB = sqrt((tl.x - bl.x)*(tl.x - bl.x) + (tl.y - bl.y)*(tl.y - bl.y));
        double maxHeight = max(heightA, heightB);

        // now that we have the dimensions of the new image, construct
        // the set of destination points to obtain a "birds eye view",
        // (i.e. top-down view) of the image, again specifying points
        // in the top-left, top-right, bottom-right, and bottom-left
        // order

        List<Point> dstPoints = new ArrayList<Point>();
        dstPoints.add(new Point(0, 0));
        dstPoints.add(new Point(maxWidth - 1, 0));
        dstPoints.add(new Point(maxWidth - 1, maxHeight - 1));
        dstPoints.add(new Point(0, maxHeight - 1));

        Mat outputRect = Converters.vector_Point2f_to_Mat(dstPoints);

        //============================ALIGNMENT===================================//
        Mat alignImage = Mat.zeros(this.colorInputImage.size(), CvType.CV_8UC1);
        Mat M = new Mat(2, 4, CvType.CV_32FC1);
        M = Imgproc.getPerspectiveTransform(inputRect, outputRect);
        Imgproc.warpPerspective(this.inputImage, alignImage, M, new Size(maxWidth, maxHeight));

        // Resize to Approriate Size
        resize(alignImage, alignImage, new Size(1280, 768));

        //============================= IMAGE REGISTRATION=========================//
        // Define the motion model
        int warp_mode = Video.MOTION_AFFINE;

        // Set a 2x3 or 3x3 warp matrix depending on the motion model.
        Mat warp_matrix = Mat.eye(2, 3, CvType.CV_32FC1);

        // Specify the number of iterations.
        int number_of_iterations = 50;

        // Specify the threshold of the increment
        // in the correlation coefficient between two iterations
        double termination_eps = 0.0001;

        // Define termination criteria
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT + TermCriteria.EPS, number_of_iterations, termination_eps);
        Mat tempt = new Mat();
        // Run the ECC algorithm. The results are stored in warp_matrix.
        Video.findTransformECC(this.templateImage, alignImage, warp_matrix, warp_mode, criteria, tempt);
        // Storage for warped image.
        Mat im2_aligned = new Mat();
        warpAffine(alignImage, im2_aligned, warp_matrix, this.templateImage.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);

        // Threshold image
        equalizeHist(im2_aligned, im2_aligned);

        return im2_aligned;
    };
    
    
    private Mat DrawVerticalGrid(Mat img, boolean isDraw){
        int colAxis = 145;
        int colRange = 57;
        int colWidth[] = {116, 114, 113, 113, 112, 112, 114, 115, 115, 112};
        for(int colIndex = 0; colIndex < 10; colIndex++)
        {
            List<Double> cols = linspace(colAxis, colAxis + colRange, 4);
            for(int i = 0; i < cols.size(); i++)
            {
                this.xGrid.add(cols.get(i).intValue());
                if(isDraw == true)
                {
                    Point pt1 = new Point(cols.get(i).intValue(), 0);
                    Point pt2 = new Point(cols.get(i).intValue(), img.size().height);
                    line(img, pt1, pt2, new  Scalar(0, 255, 0), 2);
                }
            }
            colAxis += colWidth[colIndex];
        }
        return img;
    };
    
    private Mat DrawHorizontalGrid(Mat img, boolean isDraw){
        int rowAxis = 191;
        int rowRange = 190;
        int[] rowWidth = {265, 0};
        for(int rowIndex = 0; rowIndex < 2; rowIndex++)
        {
        	List<Double> rows = linspace(rowAxis, rowAxis + rowRange, 10);
            for(int i = 0; i < rows.size(); i++)
            {
                this.yGrid.add(rows.get(i).intValue());
                if(isDraw == true)
                {
                    Point pt1 = new Point(0, rows.get(i).intValue());
                    Point pt2 = new Point(img.size().width, rows.get(i).intValue());
                    line(img, pt1, pt2, new Scalar(0, 255, 0), 2);
                }
            }
            rowAxis += rowWidth[rowIndex];
        }
        return img;
    };
    
    private Mat DrawCircle(Mat img)
    {
        for(int xIndex = 0; xIndex < this.xGrid.size(); xIndex++)
        {
            for(int yIndex = 0; yIndex < this.yGrid.size(); yIndex++)
            {
                Point center = new Point(this.xGrid.get(xIndex).intValue(), this.yGrid.get(yIndex).intValue());
                Imgproc.circle(img, center, this.circleRadius,new Scalar(255, 0, 0), 2);
            }
        }
        return img;
    };
    
    
    private List<Double> linspace(double a, long b, int n)
    {
    	List<Double> array = new ArrayList<>();
        double step = (b-a) / (n-1);
        while(a <= b) 
        {
            array.add(a);
            a += step;           // could recode to better handle rounding errors
        }
        return array;
    };
    
  
    private int IndexOfSmallestElement(List<Double> array)
    {
        int index = 0;
        double n = array.get(0);
        for (int i = 1; i < array.size(); ++i)
        {
            if (array.get(i)< n)
            {
                n = array.get(i) ;
                index = i ;
            }
        }


        if(array.get(index) < this.choosenThreshold)   // Selected
        {
            return index;
        }
        else                                        // Didnt select
        {
            return 4;                               // return X
        }
    };
    
    private void DetectAnswer(Mat img) {
        List<Double> meanStored4 = new ArrayList<>();
        // Listening Part
        for(int blockIndex = 0; blockIndex < 10; blockIndex++)
        {
            for(int y_ = 0; y_ < this.yGrid.size()/2; y_++)
            {
                for(int x_ = blockIndex*4; x_ < blockIndex*4 + 4; x_++)
                {
                    int y = this.yGrid.get(y_) - this.circleRadius;
                    int x = this.xGrid.get(x_) - this.circleRadius;
                    int w = 2*this.circleRadius;
                    int h = 2*this.circleRadius;
                    Rect bubbleAnswer = new Rect(x, y, w, h); // x, y, w, h

                    Mat crop = img.submat(bubbleAnswer);
                    Scalar _mean = Core.mean(crop);
                    meanStored4.add(_mean.val[0]);
                }
                answers.add(this.answerKey[IndexOfSmallestElement(meanStored4)]);
                meanStored4.clear();
            }
        }

        // Reading Part
        for(int blockIndex = 0; blockIndex < 10; blockIndex++)
        {
            for(int y_ = 10; y_ < this.yGrid.size(); y_++)
            {
                for(int x_ = blockIndex*4; x_ < blockIndex*4 + 4; x_++)
                {
                    int y = this.yGrid.get(y_) - circleRadius;
                    int x = this.xGrid.get(x_) - circleRadius;
                    int w = 2*this.circleRadius;
                    int h = 2*this.circleRadius;
                    Rect bubbleAnswer = new Rect(x, y, w, h); // x, y, w, h

                    Mat crop = img.submat(bubbleAnswer);
                    Scalar _mean = Core.mean(crop);
                    meanStored4.add(_mean.val[0]);
                }
                answers.add(this.answerKey[IndexOfSmallestElement(meanStored4)]);
                meanStored4.clear();
            }
        }
    }
}