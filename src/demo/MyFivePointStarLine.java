package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collections;

public class MyFivePointStarLine implements Shapes2D {
	private Point A, B;
	private Point[] point = new Point[5]; // 5 phần tử đầu chứa điểm chính, 5 phần tử sau chứa phần tử phụ
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;
	public MyFivePointStarLine() {
	}

	public MyFivePointStarLine(Point a, Point b) {
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
			if (A.x > B.x) {
				Point temp = A;
				A = B;
				B = temp;
			}

			int r = (A.x + B.x) / 2 - A.x; // R bán kính
			int xt = (A.x + B.x) / 2; // xt,yt tọa độ tâm của hình chữ nhật
			int yt = (A.y + B.y) / 2;
			float radian = (float) ((72 * Math.PI) / 180); // Đổi độ sang radian
			float a = radian;

			point[0] = new Point(xt, yt - r);

			for (int i = 1; i < 5; i++) {
				int x = (int) (point[0].x * Math.cos(a) - point[0].y * Math.sin(a) + yt * Math.sin(a)
						+ xt * (1 - Math.cos(a)));
				int y = (int) (point[0].x * Math.sin(a) + point[0].y * Math.cos(a) + yt * (1 - Math.cos(a))
						- xt * Math.sin(a));
				point[i] = new Point(x, y);
				a += radian;
			}
			

			// Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
			// Còn > 1 phải dùng hàm tự định nghĩa mới set được size pixel
			if (size > 1) {
				line = new MyLine(point[0], point[2]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[0], point[3]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[3]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[2], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			} else {
				g.drawLine(point[0].x, point[0].y, point[2].x, point[2].y);
				g.drawLine(point[0].x, point[0].y, point[3].x, point[3].y);
				g.drawLine(point[1].x, point[1].y, point[3].x, point[3].y);
				g.drawLine(point[1].x, point[1].y, point[4].x, point[4].y);
				g.drawLine(point[2].x, point[2].y, point[4].x, point[4].y);
			}
		}else {
			if (size > 1) {
				line = new MyLine(point[0], point[2]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[0], point[3]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[3]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[1], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
				line = new MyLine(point[2], point[4]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			} else {
				g.setColor(color);
				g.drawLine(point[0].x, point[0].y, point[2].x, point[2].y);
				g.drawLine(point[0].x, point[0].y, point[3].x, point[3].y);
				g.drawLine(point[1].x, point[1].y, point[3].x, point[3].y);
				g.drawLine(point[1].x, point[1].y, point[4].x, point[4].y);
				g.drawLine(point[2].x, point[2].y, point[4].x, point[4].y);
			}
		}
		
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','E'};
//		for (int i = 0; i < 5; i++) {
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
		double ABD = Display.areaTriangle(point[0], point[1], point[3]);
		double PAB = Display.areaTriangle(p, point[0], point[1]);
		double PAD = Display.areaTriangle(p, point[0], point[3]);
		double PBD = Display.areaTriangle(p, point[1], point[3]);
		boolean t1 = (PAB + PAD + PBD == ABD);
		
		double ACE = Display.areaTriangle(point[0], point[2], point[4]);
		double PAC = Display.areaTriangle(p, point[0], point[2]);
		double PAE = Display.areaTriangle(p, point[0], point[4]);
		double PCE = Display.areaTriangle(p, point[2], point[4]);
		boolean t2 = (PAC + PAE + PCE == ACE);
		return (t1 || t2);
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
		}
		point = p;
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		flag_Resize = true;
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		Point[] p = new Point[6];
		p[0] = new Point(Display.rotateAround(point[3], this.getAngle() *(-1)));
		p[1] = new Point(Display.rotateAround(point[0], this.getAngle() *(-1)));
		p[2] = new Point(Display.rotateAround(point[2], this.getAngle() *(-1)));
		p[3] = new Point(Display.rotateAround(point[4], this.getAngle() *(-1)));
		p[4] = new Point(Display.rotateAround(point[1], this.getAngle() *(-1)));
		
		point[3] = Display.rotateAround(new Point(p[0].x - dx, p[0].y + dy), this.getAngle());
		point[0] = Display.rotateAround(new Point(p[1].x, p[1].y - dy), this.getAngle());
		point[2] = Display.rotateAround(new Point(p[2].x + dx, p[2].y + dy), this.getAngle());
		point[4] = Display.rotateAround(new Point(p[3].x - dx*3/2, p[3].y - dy/2), this.getAngle());
		point[1] = Display.rotateAround(new Point(p[4].x + dx*3/2, p[4].y -dy/2 ), this.getAngle());
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub
		
	}
}
