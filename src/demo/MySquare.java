package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MySquare implements Shapes2D {
	private Point A, B, C, D;
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	public MySquare() {
	}

	public MySquare(Point a, Point b, Point c, Point d) {
		A = a;
		B = b;
		C = c;
		D = d;
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

	public Point getD() {
		return D;
	}

	public void setD(Point d) {
		D = d;
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
		Point temp;
//		 Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
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
			line = new MyLine(C, D);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(D, A);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
		} else {
			g.drawLine(A.x, A.y, B.x, B.y);
			g.drawLine(B.x, B.y, C.x, C.y);
			g.drawLine(C.x, C.y, D.x, D.y);
			g.drawLine(D.x, D.y, A.x, A.y);
		}
		
//		g.setColor(Color.BLUE);
//		g.drawString("A", this.getA().x, this.getA().y);
//		g.drawString("B", this.getB().x, this.getB().y);
//		g.drawString("C", this.getC().x, this.getC().y);
//		g.drawString("D", this.getD().x, this.getD().y);
	}

	@Override
	public void init(Point start, Point end, int size, Color color) {
		int shortEdge; // Cạnh lấy theo cạnh ngắn của HCN

		this.setA(start);
		if ((start.x <= end.x) && (start.y < end.y)) { // góc Đông Nam
			shortEdge = Math.min(end.x - start.x, end.y - start.y);
			this.setC(new Point(shortEdge + start.x, shortEdge + start.y));
		} else if ((start.x > end.x) && (start.y <= end.y)) { // góc Tây Nam
			shortEdge = Math.min(start.x - end.x, end.y - start.y);
			this.setC(new Point(start.x - shortEdge, start.y + shortEdge));
		} else if ((start.x >= end.x) && (start.y > end.y)) { // góc Tây Bắc
			shortEdge = Math.min(start.x - end.x, start.y - end.y);
			this.setC(new Point(start.x - shortEdge, start.y - shortEdge));
		} else if ((start.x < end.x) && (start.y >= end.y)) { // góc Đông Bắc
			shortEdge = Math.min(end.x - start.x, start.y - end.y);
			this.setC(new Point(start.x + shortEdge, start.y - shortEdge));
		}

		this.setB(new Point(this.getC().x, start.y));
		this.setD(new Point(start.x, this.getC().y));
		this.setSize(size);
		this.setColor(color);
	}

	@Override
	public boolean impact(Point p) {
		double ABC = Display.areaTriangle(A, B, C);
		double PAB = Display.areaTriangle(p, A, B);
		double PAC = Display.areaTriangle(p, A, C);
		double PBC = Display.areaTriangle(p, B, C);

		boolean t1 = (PAB + PAC + PBC == ABC);

		double ACD = Display.areaTriangle(A, C, D);
		double PAD = Display.areaTriangle(p, A, D);
		double PCD = Display.areaTriangle(p, C, D);

		boolean t2 = (PAC + PAD + PCD == ACD);

		return (t1 || t2) ? true : false;
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		this.setC(new Point(this.getC().x + dx, this.getC().y + dy));
		this.setD(new Point(this.getD().x + dx, this.getD().y + dy));
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point p1 = new Point(Display.rotateAround(this.getA(), a));
		Point p2 = new Point(Display.rotateAround(this.getB(), a));
		Point p3 = new Point(Display.rotateAround(this.getC(), a));
		Point p4 = new Point(Display.rotateAround(this.getD(), a));
		setA(p1);
		setB(p2);
		setC(p3);
		setD(p4);
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		Point p1 = new Point(Display.rotateAround(this.getA(), this.getAngle() * (-1)));
		Point p2 = new Point(Display.rotateAround(this.getB(), this.getAngle() * (-1)));
		Point p3 = new Point(Display.rotateAround(this.getC(), this.getAngle() * (-1)));
		Point p4 = new Point(Display.rotateAround(this.getD(), this.getAngle() * (-1)));
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		int delta = (dx > dy) ? dx : dy;

		if (p1.x < p2.x && p1.y < p4.y) {
			p1 = new Point(p1.x - delta, p1.y - delta);
			p2 = new Point(p2.x + delta, p2.y - delta);
			p3 = new Point(p3.x + delta, p3.y + delta);
			p4 = new Point(p4.x - delta, p4.y + delta);
		} else if (p1.x > p2.x && p1.y < p4.y) {
			p1 = new Point(p1.x + delta, p1.y - delta);
			p2 = new Point(p2.x - delta, p2.y - delta);
			p3 = new Point(p3.x - delta, p3.y + delta);
			p4 = new Point(p4.x + delta, p4.y + delta);
		} else if (p1.x < p2.x && p1.y > p4.y) {
			p1 = new Point(p1.x - delta, p1.y + delta);
			p2 = new Point(p2.x + delta, p2.y + delta);
			p3 = new Point(p3.x + delta, p3.y - delta);
			p4 = new Point(p4.x - delta, p4.y - delta);
		} else if (p1.x > p2.x && p1.y > p4.y) {
			p1 = new Point(p1.x + delta, p1.y + delta);
			p2 = new Point(p2.x - delta, p2.y + delta);
			p3 = new Point(p3.x - delta, p3.y - delta);
			p4 = new Point(p4.x + delta, p4.y - delta);
		}

		setA(Display.rotateAround(p1, this.getAngle()));
		setB(Display.rotateAround(p2, this.getAngle()));
		setC(Display.rotateAround(p3, this.getAngle()));
		setD(Display.rotateAround(p4, this.getAngle()));
	}

	@Override
	public void play(float angle) {
		this.setAngle(this.getAngle() + angle);
	}

}
