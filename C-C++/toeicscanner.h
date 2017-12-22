#ifndef TOEICSCANNER_H
#define TOEICSCANNER_H

#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/core.hpp>
#include <opencv2/opencv.hpp>
#include <iostream>
using namespace std;
using namespace cv;

class ToeicScanner
{
private:
    Mat inputImage;                     // store configured input image
    Mat colorInputImage;                // store configured input image with color
    Mat templateImage;                  // store configured template image
    vector<vector<Point> > squares;     // store points of squares (bounding box)
    vector<int> xGrid;                  // store bubble answer by x axis
    vector<int> yGrid;                  // store bubble answer by y axis
    int circleRadius;                   // store bubble answer radius
    char answerKey[5] = {'A', 'B', 'C', 'D', 'X'};
    vector<char> answers;               // store bubble answer from paper
    int choosenThreshold = 50;

    void LoadTemplate();
    Mat LoadInputImage(Mat img);
    Mat Preprocess(Mat img);
    Mat Detect(Mat img);
    Mat Align(Mat img);
    Mat DrawVerticalGrid(Mat img, bool isDraw = false);
    Mat DrawHorizontalGrid(Mat img, bool isDraw = false);
    Mat DrawCircle(Mat img);
    vector<double> linspace(double a, double b, int n);
    int IndexOfSmallestElement(vector<int> array);
    void DetectAnswer(Mat img);

public:
    ToeicScanner();
    Mat Process(Mat img);
    vector<char> GetAnswers();
};

#endif // TOEICSCANNER_H
