package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyTriangleIsosceles implements Shapes2D {
	private Point A, B, C;
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	public MyTriangleIsosceles() {
	}

	public MyTriangleIsosceles(Point a, Point b, Point c) {
		A = a;
		B = b;
		C = c;
	}

	public Point getA() {
		return A;
	}

	public void setA(Point a) {
		A = a;
	}

	public Point getB() {
		return B;
	}

	public void setB(Point b) {
		B = b;
	}

	public Point getC() {
		return C;
	}

	public void setC(Point c) {
		C = c;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		if (angle > 360) {
            angle -= 360;
        }
        this.angle = angle;
	}
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		MyLine line;
		// Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
		// Còn > 1 phải dùng hàm tự định nghĩa mới set được size pixel
		if (size > 1) {
			line = new MyLine(A, B);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(B, C);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(C, A);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
		} else {
			g.drawLine(A.x, A.y, B.x, B.y);
			g.drawLine(B.x, B.y, C.x, C.y);
			g.drawLine(C.x, C.y, A.x, A.y);
		}
		
//		g.setColor(Color.BLUE);
//		g.drawString("A", this.getA().x, this.getA().y);
//		g.drawString("B", this.getB().x, this.getB().y);
//		g.drawString("C", this.getC().x, this.getC().y);
	}

	@Override
	public void init(Point start, Point end, int size, Color color) {
		this.setA(new Point((start.x + end.x) / 2, start.y));
		this.setB(end);
		this.setC(new Point(start.x, end.y));
		this.setSize(size);
		this.setColor(color);
	}

	@Override
	public boolean impact(Point p) {
		double ABC = Display.areaTriangle(A, B, C);
		double PAB = Display.areaTriangle(p, A, B);
		double PAC = Display.areaTriangle(p, A, C);
		double PBC = Display.areaTriangle(p, B, C);
		return (PAB + PAC + PBC == ABC);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		this.setC(new Point(this.getC().x + dx, this.getC().y + dy));
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point p1 = new Point(Display.rotateAround(this.getA(), a));
		Point p2 = new Point(Display.rotateAround(this.getB(), a));
		Point p3 = new Point(Display.rotateAround(this.getC(), a));
		setA(p1);
		setB(p2);
		setC(p3);
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		Point p1 = new Point(Display.rotateAround(this.getA(), this.getAngle() * (-1)));
		Point p2 = new Point(Display.rotateAround(this.getB(), this.getAngle() * (-1)));
		Point p3 = new Point(Display.rotateAround(this.getC(), this.getAngle() * (-1)));
		p1 = new Point(p1.x, p1.y - dy);
		p2 = new Point(p2.x + dx, p2.y + dy);
		p3 = new Point(p3.x - dx, p3.y + dy);
		this.setA(Display.rotateAround(p1, this.getAngle()));
		this.setB(Display.rotateAround(p2, this.getAngle()));
		this.setC(Display.rotateAround(p3, this.getAngle()));
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub
		
	}

}
