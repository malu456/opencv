package com.pkg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class OpenCVShapes extends JFrame {
	Mat image;
	Mat tempImage;
	JLabel imageView;
	private JMenuBar mb;
	private JMenu menu;
	private JMenuItem saveMenuItem;
	private Point originPoint;
     public OpenCVShapes() {
    	 image = Imgcodecs.imread("images/opendv-test.jpg");
    	 
    	 setUpView();
    	 loadImage(image);
    	 
    	 
    	 setSize(image.width(),image.height());
    	 setLocationRelativeTo(null);
    	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	  setVisible(true);
     }
	private void loadImage(Mat img) {
		final MatOfByte mof= new MatOfByte();
		Imgcodecs.imencode(".jpg", img, mof);
		final byte[] imageData = mof.toArray();
		final ImageIcon icon = new ImageIcon(imageData);
		imageView.setIcon(icon);
		
	}
	private void setUpView() {
		setLayout(null);
		imageView = new JLabel();
		imageView.setBounds(0,20,image.width(),image.height());
		imageView.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				originPoint = new Point(e.getX(),e.getY());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				image = tempImage.clone();
				
			}
		});
		
		imageView.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				tempImage = image.clone();
				final Point point = new Point(e.getX(),e.getY());
				
//				Imgproc.line(tempImage, originPoint, point, new Scalar(0,0,255), 5);
//				Imgproc.rectangle(tempImage, originPoint, point, new Scalar(255,0,0), 5);
				
//				drawing circle
				
				double ab2 = Math.pow(originPoint.x-point.x,2) + Math.pow(originPoint.y-point.y,2);
				int distance =(int) Math.sqrt(ab2);

				Imgproc.circle(tempImage, originPoint, distance, new Scalar(0,255,0), 5);
//				
//				double x = Math.abs(point.x-originPoint.x);
//				double y= Math.abs(point.y-originPoint.y);
//				Size size = new Size(x*2,y*2);
//				Imgproc.ellipse(tempImage, new RotatedRect(originPoint,size,5), new Scalar(255,255,0), 5);
				
				loadImage(tempImage);
			}
			
		});
		add(imageView);
		mb = new JMenuBar();
		menu = new JMenu("file");
		saveMenuItem = new JMenuItem("save");
		menu.add(saveMenuItem);
		mb.add(menu);
		mb.setBounds(0,0,image.width(),20);
		add(mb);
		
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Imgcodecs.imwrite("images/out.jpg",image);
			}
			
			
		});
	}
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
        	@Override
        	public void run() {
        		new OpenCVShapes();
        	}
        });
	}

}
