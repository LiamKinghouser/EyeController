package com.kinghouser.eyecontroller.eyetracker;

import com.kinghouser.eyecontroller.EyeController;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EyeTrackerThread extends Thread {

    public void run() {
        startTracking();
    }

    public void startTracking() {
        try {
            // Path jarPath = Paths.get(EyeController.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            // String jarDirPath = jarPath.getParent().toString();
            // String path = jarDirPath;
            System.load("/Users/kinghouser/IdeaProjects/EyeController/run/mods" + File.separator + "lib" + File.separator + "libopencv_java460.dylib");

            EyeTracker.ready = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load the cascade classifier for detecting faces
        Path jarPath = null;
        try {
            jarPath = Paths.get(EyeController.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        jarPath = Path.of("/Users/kinghouser/IdeaProjects/EyeController/run/mods/lib"); // delete later
        String jarDirPath = jarPath.getParent().toString();
        String xmlPath = jarDirPath + File.separator + "lib" + File.separator + "xml";
        CascadeClassifier faceDetector = new CascadeClassifier(xmlPath + File.separator + "haarcascades/haarcascade_frontalface_default.xml");

        // Load the cascade classifier for detecting eyes
        // CascadeClassifier eyeDetector = new CascadeClassifier(xmlPath + File.separator + "haarcascades/haarcascade_eye.xml");

        // Open the video capture device
        VideoCapture capture = new VideoCapture(0);

        // Loop over the frames in the video stream
        while (capture.isOpened()) {
            // Read a frame from the video stream
            Mat frame = new Mat();
            capture.read(frame);

            // Convert the frame to grayscale for processing
            Mat gray = new Mat();
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

            MatOfRect faces = new MatOfRect();
            if (faceDetector.empty()) continue;
            faceDetector.detectMultiScale(gray, faces);

            if (faces.toArray().length == 0) continue;
            Rect face = getLargest(faces);

            Imgproc.rectangle(frame, face, new Scalar(255, 0, 255));
            // mouseGlide(gray.width() - getCenterX(face), getCenterY(face), 250);
            int coordX = gray.width() - getCenterX(face);
            int coordY = getCenterY(face);
            int height = gray.height();
            int width = gray.width();
            // System.out.println(coordX + ", " + coordY);
            EyeTrackerUtils.updateCamera(coordY, coordX, height, width);

            Mat circles = new Mat();
            double minDist = (double)gray.rows() / 8;
            Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    minDist, // change this value to detect circles with different distances to each other
                    100.0, 30.0, 8, 20);

            for (int x = 0; x < circles.cols(); x++) {
                double[] c = circles.get(0, x);
                Point center = new Point(Math.round(c[0]), Math.round(c[1]));
                // circle center
                Imgproc.circle(frame, center, 1, new Scalar(0,100,100), 3, 8, 0 );
                // circle outline
                int radius = (int) Math.round(c[2]);
                Imgproc.circle(frame, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            }
        }
    }

    public static Rect getLargest(MatOfRect faces) {
        Rect largest = faces.toArray()[0];
        for (Rect face : faces.toArray()) {
            double area = face.area();
            if (area > largest.area()) largest = face;
        }
        return largest;
    }

    public static int getCenterX(Rect face) {
        return face.x + (face.width / 2);
    }

    public static int getCenterY(Rect face) {
        return face.y + (face.height / 2);
    }
}
