cmake_minimum_required(VERSION 2.8.12)

project(Week2)

find_package(OpenCV REQUIRED)
include_directories(${OpenCV_INCLUDE_DIRS} toeicscanner.h)

add_executable(Week2 Week2.cpp toeicscanner.cpp)
target_link_libraries(Week2 ${OpenCV_LIBS})
