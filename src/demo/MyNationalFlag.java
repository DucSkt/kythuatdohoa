package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyNationalFlag implements Shapes2D {
	private Point A, B, C, D;
	private Point[] point_flag;
	private Point[] point_star;
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;

	public MyNationalFlag() {
	}

	public MyNationalFlag(Point a, Point b, Point c, Point d) {
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

	public Point[] getPoint_flag() {
		return point_flag;
	}

	public void setPoint_flag(Point[] point_flag) {
		this.point_flag = point_flag;
	}

	public Point[] getPoint_star() {
		return point_star;
	}

	public void setPoint_star(Point[] point_star) {
		this.point_star = point_star;
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
			point_flag = new Point[5];
			point_star = new Point[10];
			int length_X = (C.x - A.x);
			int length_Y = (C.y - A.y);
			int middle_X = (A.x + C.x) / 2;
			int middle_Y = (A.y + C.y) / 2;

			// Tính lá cờ

			point_flag[0] = new Point(A.x, A.y);
			point_flag[1] = new Point(B.x, B.y);
			point_flag[2] = new Point(C.x, middle_Y);
			point_flag[3] = new Point(A.x, middle_Y);
			point_flag[4] = new Point(A.x, C.y);

			// Tính ngôi sao
			Point star_A = new Point(middle_X - length_X / 8, A.y + length_Y / 8);
			Point star_B = new Point(middle_X + length_X / 8, middle_Y - length_Y / 8);

			int length_X_Star, length_Y_Star, middle_X_Star, space_X1, space_X2, space_X3, length_Y1, length_Y2,
					length_Y3, length_Y4;
			length_X_Star = star_B.x - star_A.x;
			length_Y_Star = star_B.y - star_A.y;
			middle_X_Star = (star_A.x + star_B.x) / 2;
			space_X1 = (int) (length_X_Star * 0.24);
			space_X2 = (int) (length_X_Star * 0.4);
			space_X3 = (int) (length_X_Star * 0.62);
			length_Y1 = (int) (length_Y_Star * 0.38);
			length_Y2 = (int) (length_Y_Star * 0.39);
			length_Y3 = (int) (length_Y_Star * 0.62);
			length_Y4 = (int) (length_Y_Star * 0.78);

			point_star[0] = new Point(middle_X_Star, star_A.y);
			point_star[1] = new Point((length_X_Star - space_X1) / 2 + space_X1 + star_A.x, length_Y1 + star_A.y);
			point_star[2] = new Point(star_B.x, length_Y2 + star_A.y);
			point_star[3] = new Point((length_X_Star - space_X2) / 2 + space_X2 + star_A.x, length_Y3 + star_A.y);
			point_star[4] = new Point((length_X_Star - space_X3) / 2 + space_X3 + star_A.x, star_B.y);
			point_star[5] = new Point(middle_X_Star, length_Y4 + star_A.y);
			point_star[6] = new Point((length_X_Star - space_X3) / 2 + star_A.x, star_B.y);
			point_star[7] = new Point((length_X_Star - space_X2) / 2 + star_A.x, length_Y3 + star_A.y);
			point_star[8] = new Point(star_A.x, length_Y2 + star_A.y);
			point_star[9] = new Point((length_X_Star - space_X1) / 2 + star_A.x, length_Y1 + star_A.y);
		}
		if (size > 1) {
			// Vẽ lá cờ
			for (int i = 0; i < 3; i++) {
				line = new MyLine(point_flag[i], point_flag[i + 1]);
				line.setColor(this.getColor());
				line.setSize(this.getSize());
				line.draw(g);
			}
			// Vẽ cây cờ
			line = new MyLine(point_flag[4], point_flag[0]);
			line.setColor(this.getColor());
			line.setSize(this.getSize() * 2);
			line.draw(g);
			// Vẽ ngôi sao
			for (int i = 0; i < 9; i++) {
				line = new MyLine(point_star[i], point_star[i + 1]);
				line.setColor(this.getColor());
				line.setSize(this.getSize());
				line.draw(g);
			}
			line = new MyLine(point_star[9], point_star[0]);
			line.setColor(this.getColor());
			line.setSize(this.getSize());
			line.draw(g);
		} else {
			// Vẽ lá cờ
			g.setColor(this.getColor());
			for (int i = 0; i < 3; i++) {
				g.drawLine(point_flag[i].x, point_flag[i].y, point_flag[i + 1].x, point_flag[i + 1].y);
			}
			// Vẽ cây cờ
			line = new MyLine(point_flag[0], point_flag[4]);
			line.setColor(this.getColor());
			line.setSize(this.getSize() * 2);
			line.draw(g);
			// Vẽ ngôi sao
			g.setColor(this.getColor());
			for (int i = 0; i < 9; i++) {
				g.drawLine(point_star[i].x, point_star[i].y, point_star[i + 1].x, point_star[i + 1].y);
			}
			g.drawLine(point_star[9].x, point_star[9].y, point_star[0].x, point_star[0].y);
		}
		
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','A'};
//		for (int i = 0; i < 5; i++) {
//			g.drawString(String.valueOf(c[i]), point_flag[i].x, point_flag[i].y);
//		}
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
		double ABC = Display.areaTriangle(point_flag[0], point_flag[1], point_flag[2]);
		double PAB = Display.areaTriangle(p, point_flag[0], point_flag[1]);
		double PAC = Display.areaTriangle(p, point_flag[0], point_flag[2]);
		double PBC = Display.areaTriangle(p, point_flag[1], point_flag[2]);
		boolean t1 = (PAB + PAC + PBC == ABC);

		double ACD = Display.areaTriangle(point_flag[0], point_flag[2], point_flag[3]);
		double PAD = Display.areaTriangle(p, point_flag[0], point_flag[3]);
		double PCD = Display.areaTriangle(p, point_flag[2], point_flag[3]);
		boolean t2 = (PAC + PAD + PCD == ACD);

		int AD = (int) Math.sqrt(
				Math.pow((point_flag[0].x - point_flag[4].x), 2) + Math.pow(point_flag[0].y - point_flag[4].y, 2));
		int PA = (int) Math.sqrt(Math.pow((point_flag[0].x - p.x), 2) + Math.pow(point_flag[0].y - p.y, 2));
		int PD = (int) Math.sqrt(Math.pow((p.x - point_flag[4].x), 2) + Math.pow(p.y - point_flag[4].y, 2));

		return (t1 || t2 || (((PA + PD) - AD) < size));
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		this.setC(new Point(this.getC().x + dx, this.getC().y + dy));
		this.setD(new Point(this.getD().x + dx, this.getD().y + dy));
		for (int i = 0; i < 5; i++) {
			point_flag[i] = new Point(point_flag[i].x + dx, point_flag[i].y + dy);
		}
		for (int i = 0; i < 10; i++) {
			point_star[i] = new Point(point_star[i].x + dx, point_star[i].y + dy);
		}
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] arr_PointFlag = new Point[5];
		Point[] arr_PointStar = new Point[10];
		for (int i = 0; i < 5; i++) {
			arr_PointFlag[i] = new Point(Display.rotateAround(point_flag[i], a));
		}
		for (int i = 0; i < 10; i++) {
			arr_PointStar[i] = new Point(Display.rotateAround(point_star[i], a));
		}
		point_flag = arr_PointFlag;
		point_star = arr_PointStar;
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		flag_Resize = true;
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		Point[] arr_PointFlag = new Point[5];
		Point[] arr_PointStar = new Point[10];
		for (int i = 0; i < 5; i++) {
			arr_PointFlag[i] = Display.rotateAround(point_flag[i], this.getAngle() * (-1));
		}
		for (int i = 0; i < 10; i++) {
			arr_PointStar[i] = Display.rotateAround(point_star[i], this.getAngle() * (-1));
		}
		int delta = (dx > dy) ? dx : dy;

		arr_PointFlag[0] = new Point(arr_PointFlag[0].x - delta * 3 / 2, arr_PointFlag[0].y - delta);
		arr_PointFlag[1] = new Point(arr_PointFlag[1].x + delta * 3 / 2, arr_PointFlag[1].y - delta);
		arr_PointFlag[2] = new Point(arr_PointFlag[2].x + delta * 3 / 2, arr_PointFlag[2].y + delta);
		arr_PointFlag[3] = new Point(arr_PointFlag[3].x - delta * 3 / 2, arr_PointFlag[3].y + delta);
		arr_PointFlag[4] = new Point(arr_PointFlag[4].x - delta * 3 / 2, arr_PointFlag[4].y + delta * 3);

		arr_PointStar[0] = new Point(arr_PointStar[0].x, arr_PointStar[0].y - delta * 7 / 10);
		arr_PointStar[1] = new Point(arr_PointStar[1].x + delta * 2 / 10, arr_PointStar[1].y - delta * 3 / 10);
		arr_PointStar[2] = new Point(arr_PointStar[2].x + delta * 75 / 100, arr_PointStar[2].y - delta * 3 / 10);
		arr_PointStar[3] = new Point(arr_PointStar[3].x + delta * 35 / 100, arr_PointStar[3].y + delta / 15);
		arr_PointStar[4] = new Point(arr_PointStar[4].x + delta * 5 / 10, arr_PointStar[4].y + delta * 6 / 10);
		arr_PointStar[5] = new Point(arr_PointStar[5].x, arr_PointStar[5].y + delta * 30 / 100);
		arr_PointStar[6] = new Point(arr_PointStar[6].x - delta * 5 / 10, arr_PointStar[6].y + delta * 6 / 10);
		arr_PointStar[7] = new Point(arr_PointStar[7].x - delta * 35 / 100, arr_PointStar[7].y + delta / 15);
		arr_PointStar[8] = new Point(arr_PointStar[8].x - delta * 75 / 100, arr_PointStar[8].y - delta * 3 / 10);
		arr_PointStar[9] = new Point(arr_PointStar[9].x - delta * 2 / 10, arr_PointStar[9].y - delta * 3 / 10);
		
		for (int i = 0; i < 5; i++) {
			arr_PointFlag[i] = Display.rotateAround(arr_PointFlag[i], this.getAngle());
		}
		for (int i = 0; i < 10; i++) {
			arr_PointStar[i] = Display.rotateAround(arr_PointStar[i], this.getAngle());
		}
		point_flag = arr_PointFlag;
		point_star = arr_PointStar;
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub

	}

}
