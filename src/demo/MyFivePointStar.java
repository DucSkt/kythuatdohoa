package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class MyFivePointStar implements Shapes2D {
	private Point A, B;
	private Point[] point = new Point[10];
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;
	public MyFivePointStar() {
	}

	public MyFivePointStar(Point a, Point b) {
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
		if (angle == 0 && flag_Resize == false) {
			g.setColor(color);
			MyLine line;
			int length_X, length_Y, middle_X, space_X1, space_X2, space_X3, length_Y1, length_Y2, length_Y3, length_Y4;
			length_X = B.x - A.x;
			length_Y = B.y - A.y;
			middle_X = (A.x + B.x) / 2;
			space_X1 = (int) (length_X * 0.24);
			space_X2 = (int) (length_X * 0.4);
			space_X3 = (int) (length_X * 0.62);
			length_Y1 = (int) (length_Y * 0.38);
			length_Y2 = (int) (length_Y * 0.39);
			length_Y3 = (int) (length_Y * 0.62);
			length_Y4 = (int) (length_Y * 0.78);
			point[0] = new Point(middle_X, A.y);
			point[1] = new Point((length_X - space_X1) / 2 + space_X1 + A.x, length_Y1 + A.y);
			point[2] = new Point(B.x, length_Y2 + A.y);
			point[3] = new Point((length_X - space_X2) / 2 + space_X2 + A.x, length_Y3 + A.y);
			point[4] = new Point((length_X - space_X3) / 2 + space_X3 + A.x, B.y);
			point[5] = new Point(middle_X, length_Y4 + A.y);
			point[6] = new Point((length_X - space_X3) / 2 + A.x, B.y);
			point[7] = new Point((length_X - space_X2) / 2 + A.x, length_Y3 + A.y);
			point[8] = new Point(A.x, length_Y2 + A.y);
			point[9] = new Point((length_X - space_X1) / 2 + A.x, length_Y1 + A.y);

			// Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
			// Còn > 1 phải dùng hàm tự định nghĩa mới set được size pixel
			if (size > 1) {
				for (int i = 0; i <= 8; i++) {
					line = new MyLine(point[i], point[i + 1]);
					line.setSize(size);
					line.setColor(color);
					line.draw(g);
				}
				line = new MyLine(point[9], point[0]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			} else {
				for (int i = 0; i <= 8; i++) {
					g.drawLine(point[i].x, point[i].y, point[i + 1].x, point[i + 1].y);
				}
				g.drawLine(point[9].x, point[9].y, point[0].x, point[0].y);
			}
		} else {
			if (size > 1) {
				MyLine line;
				for (int i = 0; i < 9; i++) {
					line = new MyLine(point[i], point[i + 1]);
					line.setColor(color);
					line.setSize(size);
					line.draw(g);
				}
				line = new MyLine(point[9], point[0]);
				line.setColor(color);
				line.setSize(size);
				line.draw(g);
			} else {
				g.setColor(color);
				for (int i = 0; i < 9; i++) {
					g.drawLine(point[i].x, point[i].y, point[i + 1].x, point[i + 1].y);
				}
				g.drawLine(point[9].x, point[9].y, point[0].x, point[0].y);
			}
		}
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','E','F','G','H','I','J'};
//		for (int i = 0; i < 10; i++) {
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
		double ABJ = Display.areaTriangle(point[0], point[1], point[9]);
		double PAB = Display.areaTriangle(p, point[0], point[1]);
		double PAJ = Display.areaTriangle(p, point[0], point[9]);
		double PBJ = Display.areaTriangle(p, point[1], point[9]);
		boolean t1 = (PAB + PAJ + PBJ == ABJ);

		double BCD = Display.areaTriangle(point[1], point[2], point[3]);
		double PBC = Display.areaTriangle(p, point[1], point[2]);
		double PBD = Display.areaTriangle(p, point[1], point[3]);
		double PCD = Display.areaTriangle(p, point[2], point[3]);
		boolean t2 = (PBC + PBD + PCD == BCD);

		double DEF = Display.areaTriangle(point[3], point[4], point[5]);
		double PDE = Display.areaTriangle(p, point[3], point[4]);
		double PDF = Display.areaTriangle(p, point[3], point[5]);
		double PEF = Display.areaTriangle(p, point[4], point[5]);
		boolean t3 = (PDE + PDF + PEF == DEF);

		double FGH = Display.areaTriangle(point[5], point[6], point[7]);
		double PFG = Display.areaTriangle(p, point[5], point[6]);
		double PFH = Display.areaTriangle(p, point[5], point[7]);
		double PGH = Display.areaTriangle(p, point[6], point[7]);
		boolean t4 = (PFG + PFH + PGH == FGH);

		double HIJ = Display.areaTriangle(point[7], point[8], point[9]);
		double PHI = Display.areaTriangle(p, point[7], point[8]);
		double PHJ = Display.areaTriangle(p, point[7], point[9]);
		double PIJ = Display.areaTriangle(p, point[8], point[9]);
		boolean t5 = (PHI + PHJ + PIJ == HIJ);

		double BDF = Display.areaTriangle(point[1], point[3], point[5]);
		double PBF = Display.areaTriangle(p, point[1], point[5]);
		boolean t6 = (PBD + PBF + PDF == BDF);

		double BFJ = Display.areaTriangle(point[1], point[5], point[9]);
		double PFJ = Display.areaTriangle(p, point[5], point[9]);
		boolean t7 = (PBF + PBJ + PFJ == BFJ);

		double FHJ = Display.areaTriangle(point[5], point[7], point[9]);
		boolean t8 = (PFH + PFJ + PHJ == FHJ);
		return (t1 || t2 || t3 || t4 || t5 || t6 || t7 || t8);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		for (int i = 0; i < 10; i++) {
			point[i] = new Point(point[i].x + dx, point[i].y + dy);
		}
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] p = new Point[10];
		for (int i = 0; i < 10; i++) {
			p[i] = new Point(Display.rotateAround(point[i], a));
			;
		}
		point = p;
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		flag_Resize = true;
		int dx = end.x-start.x;
		int dy = end.y-start.y;
		Point[] arr = new Point[10];
		for (int i = 0; i < 10; i++) {
			arr[i] = new Point(Display.rotateAround(point[i], this.getAngle() * (-1)));
		}
		// Sau khi tính độ xê lệch cho quay tọa độ về cũ
		point[0] = Display.rotateAround(new Point(arr[0].x, arr[0].y - dy * 2), this.getAngle());
		point[1] = Display.rotateAround(new Point(arr[1].x + dx, arr[1].y), this.getAngle());
		point[2] = Display.rotateAround(new Point(arr[2].x + dx * 4, arr[2].y), this.getAngle());
		point[3] = Display.rotateAround(new Point(arr[3].x + dx * 3 / 2, arr[3].y + dy), this.getAngle());
		point[4] = Display.rotateAround(new Point(arr[4].x + dx * 5 / 2, arr[4].y + dy * 3), this.getAngle());
		point[5] = Display.rotateAround(new Point(arr[5].x, arr[5].y + dy * 5 / 3), this.getAngle());
		point[6] = Display.rotateAround(new Point(arr[6].x - dx * 5 / 2, arr[6].y + dy * 3), this.getAngle());
		point[7] = Display.rotateAround(new Point(arr[7].x - dx * 3 / 2, arr[7].y + dy), this.getAngle());
		point[8] = Display.rotateAround(new Point(arr[8].x - dx * 4, arr[8].y), this.getAngle());
		point[9] = Display.rotateAround(new Point(arr[9].x - dx, arr[9].y), this.getAngle());
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub
		
	}

}
