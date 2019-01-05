package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyPentagon implements Shapes2D {
	private Point A, B;
	private Point[] point = new Point[5];
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;

	public MyPentagon() {
	}

	public MyPentagon(Point a, Point b) {
		A = a;
		B = b;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Point[] getPoint() {
		return point;
	}

	public void setPoint(Point[] point) {
		this.point = point;
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

	public boolean isFlag_Resize() {
		return flag_Resize;
	}

	public void setFlag_Resize(boolean flag_Resize) {
		this.flag_Resize = flag_Resize;
	}

	@Override
	public void draw(Graphics g) {
		if (angle == 0 && flag_Resize == false) { // Bằng 0 tức là hình chưa bị xoay vẽ theo A B
			g.setColor(color);
			MyLine line;
			int length_X, length_Y, middle_X;
			int space_X, space_Y; // space_X là chiều dài cạnh đáy dưới 60%, space Y là khoảng cách đính 1 đến 2
			// lấy 39%
			length_X = (B.x - A.x);
			length_Y = (B.y - A.y);
			space_X = (int) (length_X * 0.6);
			space_Y = (int) (length_Y * 0.39);
			middle_X = (A.x + B.x) / 2;

			point[0] = new Point(middle_X, A.y);
			point[1] = new Point(B.x, space_Y + A.y);
			point[2] = new Point(space_X + (length_X - space_X) / 2 + A.x, B.y);
			point[3] = new Point((length_X - space_X) / 2 + A.x, B.y);
			point[4] = new Point(A.x, space_Y + A.y);

			// Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
			// Còn > 1 phải dùng hàm tự định nghĩa mới set được size pixel
			if (size > 1) {
				for (int i = 0; i < 4; i++) {
					line = new MyLine(point[i], point[i + 1]);
					line.setSize(size);
					line.setColor(color);
					line.draw(g);
				}
				line = new MyLine(point[4], point[0]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			} else {
				for (int i = 0; i < 4; i++) {
					g.drawLine(point[i].x, point[i].y, point[i + 1].x, point[i + 1].y);
				}
				g.drawLine(point[4].x, point[4].y, point[0].x, point[0].y);
			}
		} else {
			if (size > 1) {
				MyLine line;
				for (int i = 0; i < 4; i++) {
					line = new MyLine(point[i], point[i + 1]);
					line.setColor(color);
					line.setSize(size);
					line.draw(g);
				}
				line = new MyLine(point[4], point[0]);
				line.setColor(color);
				line.setSize(size);
				line.draw(g);
			} else {
				g.setColor(color);
				for (int i = 0; i < 4; i++) {
					g.drawLine(point[i].x, point[i].y, point[i + 1].x, point[i + 1].y);
				}
				g.drawLine(point[4].x, point[4].y, point[0].x, point[0].y);
			}
		}
		
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','E'};
//		for (int i = 0; i < 5; i++) {
//			g.drawString(String.valueOf(c[i]), point[i].x, point[i].y);
//		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void init(Point start, Point end, int size, Color color) {
		this.setA(start);
		this.setB(end);
		this.setSize(size);
		this.setColor(color);
	}

	@Override
	public boolean impact(Point p) {
		double ABC = Display.areaTriangle(point[0], point[1], point[2]);
		double PAB = Display.areaTriangle(p, point[0], point[1]);
		double PAC = Display.areaTriangle(p, point[0], point[2]);
		double PBC = Display.areaTriangle(p, point[1], point[2]);
		boolean t1 = (PAB + PAC + PBC == ABC);

		double ACD = Display.areaTriangle(point[0], point[2], point[3]);
		double PAD = Display.areaTriangle(p, point[0], point[3]);
		double PCD = Display.areaTriangle(p, point[2], point[3]);
		boolean t2 = (PAC + PAD + PCD == ACD);

		double ADE = Display.areaTriangle(point[0], point[3], point[4]);
		double PAE = Display.areaTriangle(p, point[0], point[4]);
		double PDE = Display.areaTriangle(p, point[3], point[4]);
		boolean t3 = (PAD + PAE + PDE == ADE);
		return (t1 || t2 || t3);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		for (int i = 0; i < 5; i++) {
			point[i] = new Point(point[i].x + dx, point[i].y + dy);
		}
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] p = new Point[5];
		for (int i = 0; i < 5; i++) {
			p[i] = new Point(Display.rotateAround(point[i], a));
			;
		}
		point = p;
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		flag_Resize = true;
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		Point[] arr = new Point[5];
		for (int i = 0; i < 5; i++) {
			arr[i] = new Point(Display.rotateAround(point[i], angle * (-1)));
		}
		arr[0] = Display.rotateAround(new Point(arr[0].x, arr[0].y - dy), angle);
		arr[1] = Display.rotateAround(new Point(arr[1].x + dx, arr[1].y + dy), angle);
		arr[2] = Display.rotateAround(new Point(arr[2].x + dx, arr[2].y + dy * 3), angle);
		arr[3] = Display.rotateAround(new Point(arr[3].x - dx, arr[3].y + dy * 3), angle);
		arr[4] = Display.rotateAround(new Point(arr[4].x - dx, arr[4].y + dy), angle);
		point = arr;
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub
		
	}

}
