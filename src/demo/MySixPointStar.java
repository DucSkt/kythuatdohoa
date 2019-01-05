package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MySixPointStar implements Shapes2D {
	private Point A, B;
	private Point[] point = new Point[6];
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;

	public MySixPointStar() {
	}

	public MySixPointStar(Point a, Point b) {
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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
		MyLine line;
		if (angle == 0 && flag_Resize == false) {
			g.setColor(color);
			int middle_X, space_Y; // middle_X là điểm giữa HCN theo trục x, space_Y là chiều dài 50% ở giữa theo
									// trục Y
			int length_X, length_Y;

			length_X = (B.x - A.x);
			length_Y = (B.y - A.y);

			middle_X = (A.x + B.x) / 2;
			space_Y = length_Y / 2; // Lấy 50%

			point[0] = new Point(middle_X, A.y);
			point[1] = new Point(B.x, (length_Y - space_Y) / 2 + A.y);
			point[2] = new Point(B.x, (length_Y - space_Y) / 2 + space_Y + A.y);
			point[3] = new Point(middle_X, B.y);
			point[4] = new Point(A.x, (length_Y - space_Y) / 2 + space_Y + A.y);
			point[5] = new Point(A.x, (length_Y - space_Y) / 2 + A.y);

			// Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
			// Còn > 1 phải dùng hàm tự định nghĩa mới set được size pixel
			if (size > 1) {
				line = new MyLine(point[0], point[2]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[0], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[3]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[5]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[2], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[3], point[5]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			} else {
				g.drawLine(point[0].x, point[0].y, point[2].x, point[2].y);
				g.drawLine(point[0].x, point[0].y, point[4].x, point[4].y);
				g.drawLine(point[1].x, point[1].y, point[3].x, point[3].y);
				g.drawLine(point[1].x, point[1].y, point[5].x, point[5].y);
				g.drawLine(point[2].x, point[2].y, point[4].x, point[4].y);
				g.drawLine(point[3].x, point[3].y, point[5].x, point[5].y);
			}
		} else {
			if (size > 1) {
				line = new MyLine(point[0], point[2]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[0], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[3]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[5]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[2], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[3], point[5]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			} else {
				g.setColor(color);
				g.drawLine(point[0].x, point[0].y, point[2].x, point[2].y);
				g.drawLine(point[0].x, point[0].y, point[4].x, point[4].y);
				g.drawLine(point[1].x, point[1].y, point[3].x, point[3].y);
				g.drawLine(point[1].x, point[1].y, point[5].x, point[5].y);
				g.drawLine(point[2].x, point[2].y, point[4].x, point[4].y);
				g.drawLine(point[3].x, point[3].y, point[5].x, point[5].y);
			}
		}
		
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','E','F'};
//		for (int i = 0; i < 6; i++) {
//			g.drawString(String.valueOf(c[i]), point[i].x, point[i].y);
//		}

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
		double ACE = Display.areaTriangle(point[0], point[2], point[4]);
		double PAC = Display.areaTriangle(p, point[0], point[2]);
		double PAE = Display.areaTriangle(p, point[0], point[4]);
		double PCE = Display.areaTriangle(p, point[2], point[4]);
		boolean t1 = (PAC + PAE + PCE == ACE);

		double BDF = Display.areaTriangle(point[1], point[3], point[5]);
		double PBD = Display.areaTriangle(p, point[1], point[3]);
		double PBF = Display.areaTriangle(p, point[1], point[5]);
		double PDF = Display.areaTriangle(p, point[3], point[5]);
		boolean t2 = (PBD + PBF + PDF == BDF);

		return (t1 || t2);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		for (int i = 0; i < 6; i++) {
			point[i] = new Point(point[i].x + dx, point[i].y + dy);
		}
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] p = new Point[6];
		for (int i = 0; i < 6; i++) {
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
		Point[] arr = new Point[6];
		for (int i = 0; i < 6; i++) {
			arr[i] = new Point(Display.rotateAround(point[i], this.getAngle() * (-1)));
		}
		// Sau khi tính độ xê lệch cho quay tọa độ về cũ
		point[0] = Display.rotateAround(new Point(arr[0].x, arr[0].y - dy * 2), this.getAngle());
		point[1] = Display.rotateAround(new Point(arr[1].x + dx * 2, arr[1].y - dy), this.getAngle());
		point[2] = Display.rotateAround(new Point(arr[2].x + dx * 2, arr[2].y + dy), this.getAngle());
		point[3] = Display.rotateAround(new Point(arr[3].x, arr[3].y + dy * 2), this.getAngle());
		point[4] = Display.rotateAround(new Point(arr[4].x - dx * 2, arr[4].y + dy), this.getAngle());
		point[5] = Display.rotateAround(new Point(arr[5].x - dx * 2, arr[5].y - dy), this.getAngle());
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub
		
	}

}
