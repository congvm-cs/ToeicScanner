#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"

#include <iostream>
#include <math.h>
#include <string.h>
#include "toeicscanner.h"

using namespace cv;
using namespace std;

int main(int argc, char** argv )
{
    if ( argc != 2 )
    {
        printf("usage: DisplayImage.out <Image_Path>\n");
        return -1;
    }
    Mat I = imread(argv[1], 1 );
    if ( !I.data )
    {
        printf("No image data \n");
        return -1;
    }

    Mat result;
    ToeicScanner scanner;
    result = scanner.Process(I);
//    scanner.GetAnswers();

    imshow("result", result);
    waitKey(0);
    destroyAllWindows();
    return 0;
}
