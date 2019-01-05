package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyFourPointStar implements Shapes2D {
	private Point A, B;
	private int size;
	private Color color;
	private Point[] point = new Point[8];;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;

	public MyFourPointStar() {
	}

	public MyFourPointStar(Point a, Point b) {
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

	public void setPoint(Point[] p) {
		this.point = p;
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
			int length_X, length_Y;
			int space_X, space_Y; // Khoảng trống theo trục X và Y
			int middle_X;
			int middle_Y;

			// Chiều dài và rộng theo x và y
			length_X = (B.x - A.x);
			length_Y = (B.y - A.y);

			// Khoảng trống của trục x và y lấy theo tỉ lệ 25%
			space_X = length_X / 4;
			space_Y = length_Y / 4;

			// 4 đỉnh giữa của hình tứ giác
			middle_X = (A.x + B.x) / 2;
			middle_Y = (A.y + B.y) / 2;

			point[0] = new Point(middle_X, A.y);
			point[1] = new Point((length_X - space_X) / 2 + space_X + A.x, (length_Y - space_Y) / 2 + A.y);
			point[2] = new Point(B.x, middle_Y);
			point[3] = new Point((length_X - space_X) / 2 + space_X + A.x, (length_Y - space_Y) / 2 + space_Y + A.y);
			point[4] = new Point(middle_X, B.y);
			point[5] = new Point((length_X - space_X) / 2 + A.x, (length_Y - space_Y) / 2 + space_Y + A.y);
			point[6] = new Point(A.x, middle_Y);
			point[7] = new Point((length_X - space_X) / 2 + A.x, (length_Y - space_Y) / 2 + A.y);
		}
		// Nếu size pixel == 1 thì dùng hàm của hệ thống để chạy nhanh hơn
		// Còn > 1 phải dùng hàm tự định nghĩa mới set được size pixel
		if (size > 1) {
			MyLine line;
			for (int i = 0; i < 7; i++) {
				line = new MyLine(point[i], point[i + 1]);
				line.setColor(color);
				line.setSize(size);
				line.draw(g);
			}
			line = new MyLine(point[7], point[0]);
			line.setColor(color);
			line.setSize(size);
			line.draw(g);
		} else {
			g.setColor(color);
			for (int i = 0; i < 7; i++) {
				g.drawLine(point[i].x, point[i].y, point[i + 1].x, point[i + 1].y);
			}
			g.drawLine(point[7].x, point[7].y, point[0].x, point[0].y);
		}
		
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','E','F','G','H'};
//		for (int i = 0; i < 8; i++) {
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
		double ABH = Display.areaTriangle(point[0], point[1], point[7]);
		double PAB = Display.areaTriangle(p, point[0], point[1]);
		double PAH = Display.areaTriangle(p, point[0], point[7]);
		double PBH = Display.areaTriangle(p, point[1], point[7]);

		boolean t1 = (PAB + PAH + PBH == ABH);

		double BCD = Display.areaTriangle(point[1], point[2], point[3]);
		double PBC = Display.areaTriangle(p, point[1], point[2]);
		double PBD = Display.areaTriangle(p, point[1], point[3]);
		double PCD = Display.areaTriangle(p, point[2], point[3]);

		boolean t2 = (PBC + PBD + PCD == BCD);

		double FDE = Display.areaTriangle(point[5], point[3], point[4]);
		double PFD = Display.areaTriangle(p, point[5], point[3]);
		double PFE = Display.areaTriangle(p, point[5], point[4]);
		double PDE = Display.areaTriangle(p, point[3], point[4]);

		boolean t3 = (PFD + PFE + PDE == FDE);

		double GHF = Display.areaTriangle(point[6], point[7], point[5]);
		double PGH = Display.areaTriangle(p, point[6], point[7]);
		double PGF = Display.areaTriangle(p, point[6], point[5]);
		double PHF = Display.areaTriangle(p, point[7], point[5]);

		boolean t4 = (PGH + PGF + PHF == GHF);

		double HDF = Display.areaTriangle(point[7], point[3], point[5]);
		double PHD = Display.areaTriangle(p, point[7], point[3]);
		double PDF = Display.areaTriangle(p, point[3], point[5]);

		boolean t5 = (PHD + PHF + PDF == HDF);

		double HBD = Display.areaTriangle(point[7], point[1], point[3]);
		double PHB = Display.areaTriangle(p, point[7], point[1]);

		boolean t6 = (PHB + PHD + PBD == HBD);

		return (t1 || t2 || t3 || t4 || t5 || t6);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		for (int i = 0; i < 8; i++) {
			point[i] = new Point(point[i].x + dx, point[i].y + dy);
		}
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] p = new Point[8];
		for (int i = 0; i < 8; i++) {
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
		Point[] arr = new Point[8];
		for (int i = 0; i < 8; i++) {
			arr[i] = new Point(Display.rotateAround(point[i], this.getAngle() * (-1)));
		}
		// Sau khi tính độ xê lệch cho quay tọa độ về cũ
		point[0] = Display.rotateAround(new Point(arr[0].x, arr[0].y - dy * 4), this.getAngle());
		point[1] = Display.rotateAround(new Point(arr[1].x + dx, arr[1].y - dy), this.getAngle());
		point[2] = Display.rotateAround(new Point(arr[2].x + dx * 4, arr[2].y), this.getAngle());
		point[3] = Display.rotateAround(new Point(arr[3].x + dx, arr[3].y + dy), this.getAngle());
		point[4] = Display.rotateAround(new Point(arr[4].x, arr[4].y + dy * 4), this.getAngle());
		point[5] = Display.rotateAround(new Point(arr[5].x - dx, arr[5].y + dy), this.getAngle());
		point[6] = Display.rotateAround(new Point(arr[6].x - dx * 4, arr[6].y), this.getAngle());
		point[7] = Display.rotateAround(new Point(arr[7].x - dx, arr[7].y - dy), this.getAngle());
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub

	}

}
