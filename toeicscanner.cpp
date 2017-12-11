#include "toeicscanner.h"

ToeicScanner::ToeicScanner()
{
    this->circleRadius = 6;
}

Mat ToeicScanner::Process(Mat img)
{
    this->LoadTemplate();
    Mat result;
    result = this->LoadInputImage(img);
    result = this->Preprocess(this->inputImage);
    result = this->Detect(result);

    imshow("result", result);
    waitKey(0);
    destroyAllWindows();

    result = this->Align(result);
    result = this->DrawVerticalGrid(result);
    result = this->DrawHorizontalGrid(result);
    this->DetectAnswer(result);
    result = this->DrawCircle(result);
    return result;
}

vector<char> ToeicScanner::GetAnswers()
{
    return this->answers;
}

void ToeicScanner::LoadTemplate()
{
    Mat template_temp = imread("/home/vmc/Desktop/Scanner/assets/templates/T2.jpg");
    resize(template_temp, template_temp, Size(1280, 768));
    cvtColor(template_temp, template_temp, COLOR_RGB2GRAY);
    this->templateImage = template_temp;
}

Mat ToeicScanner::LoadInputImage(Mat img)
{
    resize(img, img, Size(1280, 768));
    this->colorInputImage = img;

    cvtColor(img, img, COLOR_RGB2GRAY);
    this->inputImage = img;

    return this->inputImage;
}

Mat ToeicScanner::Preprocess(Mat img)
{
//    Mat img = this->inputImage;
    Mat blurredImage;
    GaussianBlur(img, blurredImage, Size(5, 5), 0);
    Mat thresholdImage;
    double otsu_thresh_val = threshold(blurredImage,
                                       thresholdImage,
                                       0, 255, CV_THRESH_BINARY | CV_THRESH_OTSU);

    Mat edgeImage;
    double high_thresh_val  = otsu_thresh_val;
    double lower_thresh_val = otsu_thresh_val * 0.2;
    cv::Canny(blurredImage, edgeImage, lower_thresh_val, high_thresh_val);

    Mat sel = getStructuringElement(MORPH_RECT,
                                    Size(11, 11));
    Mat morpImage;
    morphologyEx(edgeImage, morpImage, MORPH_CLOSE, sel);

    return morpImage;
}

// comparison function object
bool compareContourAreas ( std::vector<cv::Point> contour1, std::vector<cv::Point> contour2 ) {
    double i = fabs( contourArea(cv::Mat(contour1)) );
    double j = fabs( contourArea(cv::Mat(contour2)) );
    return ( i < j );
}

Mat ToeicScanner::Detect(Mat img)
{
    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    /// Find contours
    findContours(img, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE);

    vector<Point> approx;

    std::sort(contours.begin(), contours.end(), compareContourAreas);

    for( size_t i = contours.size() - 1; i >= 0; i-- )
    {
       // approximate contour with accuracy proportional
       // to the contour perimeter
       approxPolyDP(Mat(contours[i]), approx, arcLength(Mat(contours[i]), true)*0.02, true);

       if( approx.size() == 4)
       {
            this->squares.push_back(approx);
            break;
       }
    }

    polylines(this->colorInputImage, this->squares, true, Scalar(0,255,0), 3, LINE_AA);

    for(int i = 0; i<4; i++)
    {
        circle(this->colorInputImage, Point(this->squares[0][i]), 15, Scalar(0, 0, 255), 2);
        circle(this->colorInputImage, Point(this->squares[0][i]), 5, Scalar(0, 0, 255), -1);
    }

    return this->colorInputImage;
}

Mat ToeicScanner::Align(Mat img)
{
    //=========================SORT POINT IN ORDER======================//
    vector<vector<Point> > tempSquares = this->squares;
    Point tl;
    Point br;
    Point tr;
    Point bl;

    // Find Top Left Point
    int indexMinSum = 0;
    int minSum = int(tempSquares[0][0].x) + int(tempSquares[0][0].y);
    for(int i = 0; i < tempSquares[0].size(); i++)
    {
        int x = int(tempSquares[0][i].x);
        int y = int(tempSquares[0][i].y);
        if((x + y) <= minSum)
        {
            minSum = x + y;
            tl = Point(x, y);
            indexMinSum = i;
        }
    }
//    cout << "tl: "<< tl << endl;

    tempSquares[0].erase(tempSquares[0].begin() + indexMinSum);

    // Find Bottom Right Point
    int indexMaxSum = 0;
    int maxSum = int(tempSquares[0][0].x) + int(tempSquares[0][0].y);
    for(int i = 0; i<tempSquares[0].size(); i++)
    {
        int x = int(tempSquares[0][i].x);
        int y = int(tempSquares[0][i].y);
        if((x + y) >= maxSum)
        {
            maxSum = (x + y);
            br = Point(x, y);
            indexMaxSum = i;
        }
    }

//    cout << "br: "<< br << endl;
    tempSquares[0].erase(tempSquares[0].begin() + indexMaxSum);

    // Find Bottom Left Point and Top Right Point
    if(tempSquares[0][0].x < tempSquares[0][1].x)
    {
        int x1 = int(tempSquares[0][0].x);
        int y1 = int(tempSquares[0][0].y);
        bl = Point(x1, y1);

        int x2 = int(tempSquares[0][1].x);
        int y2 = int(tempSquares[0][1].y);
        tr = Point(x2, y2);
    }
    else
    {
        int x1 = int(tempSquares[0][0].x);
        int y1 = int(tempSquares[0][0].y);
        tr = Point(x1, y1);

        int x2 = int(tempSquares[0][1].x);
        int y2 = int(tempSquares[0][1].y);
        bl = Point(x2, y2);
    }
//    cout << "tr: "<< tr << endl;
//    cout << "bl: "<< bl << endl;

    // Adding Anchor Point to transform
    Point2f inputRect[4];
    inputRect[0] = tl;
    inputRect[1] = tr;
    inputRect[2] = br;
    inputRect[3] = bl;

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

    Point2f outputRect[4];
    outputRect[0] = Point(0, 0);
    outputRect[1] = Point(maxWidth - 1, 0);
    outputRect[2] = Point(maxWidth - 1, maxHeight - 1);
    outputRect[3] = Point(0, maxHeight - 1);


    //============================ALIGNMENT===================================//
    Mat alignImage = Mat::zeros(this->colorInputImage.size(), CV_8UC1);
    Mat M(2, 4, CV_32FC1);
    M = getPerspectiveTransform(inputRect, outputRect);
    warpPerspective(this->inputImage, alignImage, M, Size(maxWidth, maxHeight));

    // Resize to Approriate Size
    resize(alignImage, alignImage, Size(1280, 768));

    //============================= IMAGE REGISTRATION=========================//
    // Define the motion model
    const int warp_mode = MOTION_AFFINE;

    // Set a 2x3 or 3x3 warp matrix depending on the motion model.
    Mat warp_matrix = Mat::eye(2, 3, CV_32FC1);

    // Specify the number of iterations.
    int number_of_iterations = 50;

    // Specify the threshold of the increment
    // in the correlation coefficient between two iterations
    double termination_eps = 0.0001;

    // Define termination criteria
    TermCriteria criteria (TermCriteria::COUNT + TermCriteria::EPS, number_of_iterations, termination_eps);

    // Run the ECC algorithm. The results are stored in warp_matrix.
    findTransformECC(this->templateImage, alignImage, warp_matrix, warp_mode,criteria);
    // Storage for warped image.
    Mat im2_aligned;
    warpAffine (alignImage, im2_aligned, warp_matrix, this->templateImage.size(), INTER_LINEAR + WARP_INVERSE_MAP);

    // Threshold image
    equalizeHist(im2_aligned, im2_aligned);
//    threshold(im2_aligned, im2_aligned, 100, 255, CV_THRESH_OTSU);



//    threshold( src_gray, dst, 100, max_BINARY_value,threshold_type );
    return im2_aligned;
}


Mat ToeicScanner::DrawVerticalGrid(Mat img, bool isDraw)
{
    int colAxis = 145;
    int colRange = 57;
    int colWidth[10] = {116, 114, 113, 113, 112, 112, 114, 115, 115, 112};
    for(int colIndex = 0; colIndex < 10; colIndex++)
    {
        vector<double> cols = this->linspace(colAxis, colAxis + colRange, 4);
        for(int i = 0; i < cols.size(); i++)
        {
            this->xGrid.push_back(cols[i]);
            if(isDraw==true)
            {
                Point pt1 = Point(cols[i], 0);
                Point pt2 = Point(cols[i], img.size().height);
                line(img, pt1, pt2, Scalar(0, 255, 0), 2);
            }
        }
        colAxis += colWidth[colIndex];
    }
    return img;
}

Mat ToeicScanner::DrawHorizontalGrid(Mat img, bool isDraw)
{
    int rowAxis = 191;
    int rowRange = 190;
    int rowWidth[1] = {265};
    for(int rowIndex = 0; rowIndex < 2; rowIndex++)
    {
        vector<double> rows = this->linspace(rowAxis, rowAxis + rowRange, 10);
        for(int i = 0; i < rows.size(); i++)
        {
            this->yGrid.push_back(rows[i]);
            if(isDraw==true)
            {
                Point pt1 = Point(0, rows[i]);
                Point pt2 = Point( img.size().width, rows[i]);
                line(img, pt1, pt2, Scalar(0, 255, 0), 2);
            }
        }
        rowAxis += rowWidth[rowIndex];
    }
    return img;
}

Mat ToeicScanner::DrawCircle(Mat img)
{
    for(int xIndex = 0; xIndex < this->xGrid.size(); xIndex++)
    {
        for(int yIndex = 0; yIndex < this->yGrid.size(); yIndex++)
        {
            Point center = Point(this->xGrid[xIndex], this->yGrid[yIndex]);
            circle(img, center, this->circleRadius, Scalar(0), 2);
        }
    }
    return img;
}

vector<double> ToeicScanner::linspace(double a, double b, int n)
{
    vector<double> array;
    double step = (b-a) / (n-1);

    while(a <= b) {
        array.push_back(a);
        a += step;           // could recode to better handle rounding errors
    }
    return array;
}


int ToeicScanner::IndexOfSmallestElement(vector<int> array)
{
    int index = 0;
    double n = array[0] ;
    for (int i = 1; i < array.size(); ++i)
    {
        if (array[i] < n)
        {
            n = array[i] ;
            index = i ;
        }
    }


    if(array[index] < this->choosenThreshold)   // Selected
    {
        return index;
    }
    else                                        // Didnt select
    {
        return 4;                               // return X
    }

}

void ToeicScanner::DetectAnswer(Mat img)
{
    vector<int> meanStored4;
    int index = 1;
    // Listening Part
    cout << "==================Listening Part==================" << endl;
    for(int blockIndex = 0; blockIndex < 10; blockIndex++)
    {
        for(int y_ = 0; y_ < this->yGrid.size()/2; y_++)
        {
            cout << index <<" : ";
            for(int x_ = blockIndex*4; x_ < blockIndex*4 + 4; x_++)
            {
                int y = int(this->yGrid[y_] - circleRadius);
                int x = int(this->xGrid[x_] - circleRadius);
                int w = int(2*this->circleRadius);
                int h = int(2*this->circleRadius);
                Rect bubbleAnswer(x, y, w, h); // x, y, w, h

                Mat crop = img(bubbleAnswer);
                Scalar _mean = mean(crop);
//                cout << _mean.val[0] << endl;
                meanStored4.push_back(_mean.val[0]);
            }
            cout << this->answerKey[IndexOfSmallestElement(meanStored4)] << endl;
            answers.push_back(this->answerKey[IndexOfSmallestElement(meanStored4)]);
            meanStored4.clear();
            index++;
        }
    }

    // Reading Part
    cout << "==================Reading Part====================" << endl;
    for(int blockIndex = 0; blockIndex < 10; blockIndex++)
    {
        for(int y_ = 10; y_ < this->yGrid.size(); y_++)
        {
            cout << index <<" : ";
            for(int x_ = blockIndex*4; x_ < blockIndex*4 + 4; x_++)
            {
                int y = int(this->yGrid[y_] - circleRadius);
                int x = int(this->xGrid[x_] - circleRadius);
                int w = int(2*this->circleRadius);
                int h = int(2*this->circleRadius);
                Rect bubbleAnswer(x, y, w, h); // x, y, w, h

                Mat crop = img(bubbleAnswer);
                Scalar _mean = mean(crop);
                meanStored4.push_back(_mean.val[0]);
            }
            cout << this->answerKey[IndexOfSmallestElement(meanStored4)] << endl;
            answers.push_back(this->answerKey[IndexOfSmallestElement(meanStored4)]);
            meanStored4.clear();
            index++;
        }
    }
}

