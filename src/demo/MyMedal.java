package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyMedal implements Shapes2D {
	private Point A, B, C, D;
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private boolean flag_Resize = false;
	private Point[] point;
	private Point[] point_top;
	private Point[] point_bottom;
	private Point O;
	private int R;

	public MyMedal() {
	}

	public MyMedal(Point a, Point b, Point c, Point d) {
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

	public boolean isFlag_Resize() {
		return flag_Resize;
	}

	public void setFlag_Resize(boolean flag_Resize) {
		this.flag_Resize = flag_Resize;
	}

	public Point getO() {
		return O;
	}

	public void setO(Point o) {
		O = o;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
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

	public Point[] getPoint() {
		return point;
	}

	public void setPoint(Point[] point) {
		this.point = point;
	}

	public Point[] getPoint_top() {
		return point_top;
	}

	public void setPoint_top(Point[] point_top) {
		this.point_top = point_top;
	}

	public Point[] getPoint_bottom() {
		return point_bottom;
	}

	public void setPoint_bottom(Point[] point_bottom) {
		this.point_bottom = point_bottom;
	}

	@Override
	public void draw(Graphics g) {
		MyLine line;
		if (angle == 0 && flag_Resize == false) {
			point = new Point[6]; // 6 điểm trụ trong
			int length_X = (C.x - A.x);
			int length_Y = (C.y - A.y);
			int middle_X = (A.x + C.x) / 2;
			int middle_Y = (A.y + C.y) / 2;

			// Tính 6 điểm trụ hình chữ nhật đứng bên trong
			point[0] = new Point(middle_X - length_X / 4, A.y);
			point[1] = new Point(middle_X + length_X / 4, A.y);
			point[2] = new Point(middle_X + length_X / 4, middle_Y);
			point[3] = new Point(middle_X + length_X / 4, C.y);
			point[4] = new Point(middle_X - length_X / 4, C.y);
			point[5] = new Point(middle_X - length_X / 4, middle_Y);

			// Tính phần nửa trên là phần hình ngũ giác ngược
			point_top = new Point[6];
			point_top[0] = new Point(point[0].x + length_X / 20, point[0].y);
			point_top[1] = new Point(point[1].x - length_X / 20, point[1].y);
			point_top[2] = new Point(middle_X + length_X / 4, (int) (middle_Y - length_Y * 0.15));
			point_top[3] = new Point(middle_X, middle_Y);
			point_top[4] = new Point(middle_X - length_X / 4, (int) (middle_Y - length_Y * 0.15));
			point_top[5] = new Point(middle_X + length_X / 10, point[0].y);

			// Tính nửa phần dưới gồm hình ngôi sao kết hợp hình tròn
			point_bottom = new Point[5];
			int r = (point[5].x + point[3].x) / 2 - point[5].x; // R bán kính
			int xt = (point[5].x + point[3].x) / 2; // xt,yt tọa độ tâm của hình chữ nhật
			int yt = (point[5].y + point[3].y) / 2;
			float radian = (float) ((72 * Math.PI) / 180); // Đổi độ sang radian
			float a = radian;
			point_bottom[0] = new Point(xt, yt - r);
			for (int i = 1; i < 5; i++) {
				int x = (int) (point_bottom[0].x * Math.cos(a) - point_bottom[0].y * Math.sin(a) + yt * Math.sin(a)
						+ xt * (1 - Math.cos(a)));
				int y = (int) (point_bottom[0].x * Math.sin(a) + point_bottom[0].y * Math.cos(a)
						+ yt * (1 - Math.cos(a)) - xt * Math.sin(a));
				point_bottom[i] = new Point(x, y);
				a += radian;
			}
		}
		if (size > 1) {
			// Vẽ nửa phần trên
			for (int i = 0; i < 4; i++) {
				line = new MyLine(point_top[i], point_top[i + 1]);
				line.setSize(this.getSize());
				line.setColor(this.getColor());
				line.draw(g);
			}
			line = new MyLine(point_top[4], point_top[0]);
			line.setSize(this.getSize());
			line.setColor(this.getColor());
			line.draw(g);
			line = new MyLine(point_top[0], point_top[3]);
			line.setSize(this.getSize());
			line.setColor(this.getColor());
			line.draw(g);
			line = new MyLine(point_top[5], point_top[2]);
			line.setSize(this.getSize());
			line.setColor(this.getColor());
			line.draw(g);

			// Vẽ nửa phần dưới
			line = new MyLine(point_bottom[0], point_bottom[2]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point_bottom[0], point_bottom[3]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point_bottom[1], point_bottom[3]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point_bottom[1], point_bottom[4]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point_bottom[2], point_bottom[4]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);

			MyOval oval = new MyOval(point[5], point[3], this.getSize(), this.getColor(), this.getAngle());
			oval.draw(g);
			O = oval.getO();
			R = oval.getR();
		} else {
			g.setColor(this.getColor());
			// Vẽ nửa phần trên
			for (int i = 0; i < 4; i++) {
				g.drawLine(point_top[i].x, point_top[i].y, point_top[i + 1].x, point_top[i + 1].y);
			}
			g.drawLine(point_top[0].x, point_top[0].y, point_top[3].x, point_top[3].y);
			g.drawLine(point_top[4].x, point_top[4].y, point_top[0].x, point_top[0].y);
			g.drawLine(point_top[5].x, point_top[5].y, point_top[2].x, point_top[2].y);

			// Vẽ nửa phần dưới
			g.drawLine(point_bottom[0].x, point_bottom[0].y, point_bottom[2].x, point_bottom[2].y);
			g.drawLine(point_bottom[0].x, point_bottom[0].y, point_bottom[3].x, point_bottom[3].y);
			g.drawLine(point_bottom[1].x, point_bottom[1].y, point_bottom[3].x, point_bottom[3].y);
			g.drawLine(point_bottom[1].x, point_bottom[1].y, point_bottom[4].x, point_bottom[4].y);
			g.drawLine(point_bottom[2].x, point_bottom[2].y, point_bottom[4].x, point_bottom[4].y);
			MyOval oval = new MyOval(point[5], point[3], this.getSize(), this.getColor(), this.getAngle());
			oval.draw(g);
			O = oval.getO();
			R = oval.getR();
		}

		// g.setColor(Color.BLUE);
		// char [] c = {'A','B','C','D','E','F'};
		// for (int i = 0; i < 6; i++) {
		// g.drawString(String.valueOf(c[i]), point[i].x, point[i].y);
		// }
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
		double ABD = Display.areaTriangle(point[0], point[1], point[3]);
		double PAB = Display.areaTriangle(p, point[0], point[1]);
		double PAD = Display.areaTriangle(p, point[0], point[3]);
		double PBD = Display.areaTriangle(p, point[1], point[3]);

		boolean t1 = (PAB + PAD + PBD == ABD);

		double ADE = Display.areaTriangle(point[0], point[3], point[4]);
		double PAE = Display.areaTriangle(p, point[0], point[4]);
		double PDE = Display.areaTriangle(p, point[3], point[4]);

		boolean t2 = (PAD + PAE + PDE == ADE);
		return (t1 || t2);
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
			point_top[i] = new Point(point_top[i].x + dx, point_top[i].y + dy);
			point_bottom[i] = new Point(point_bottom[i].x + dx, point_bottom[i].y + dy);
			point[i] = new Point(point[i].x + dx, point[i].y + dy);
		}
		point_top[5] = new Point(point_top[5].x + dx, point_top[5].y + dy);
		point[5] = new Point(point[5].x + dx, point[5].y + dy);
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] arr_Point = new Point[6];
		Point[] arr_PointTop = new Point[6];
		Point[] arr_PointBottom = new Point[5];
		for (int i = 0; i < 5; i++) {
			arr_PointTop[i] = new Point(Display.rotateAround(point_top[i], a));
			arr_PointBottom[i] = new Point(Display.rotateAround(point_bottom[i], a));
			arr_Point[i] = new Point(Display.rotateAround(point[i], a));
		}
		arr_Point[5] = new Point(Display.rotateAround(point[5], a));
		arr_PointTop[5] = new Point(Display.rotateAround(point_top[5], a));
		point = arr_Point;
		point_top = arr_PointTop;
		point_bottom = arr_PointBottom;
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		flag_Resize = true;
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		Point[] p = new Point[6];
		Point[] arr_Point = new Point[6];
		Point[] arr_PointTop = new Point[6];
		Point[] arr_PointBottom = new Point[5];
		for (int i = 0; i < 5; i++) {
			arr_PointTop[i] = new Point(Display.rotateAround(point_top[i], this.getAngle() * (-1)));
			arr_PointBottom[i] = new Point(Display.rotateAround(point_bottom[i], this.getAngle() * (-1)));
			arr_Point[i] = new Point(Display.rotateAround(point[i], this.getAngle() * (-1)));
		}
		arr_Point[5] = new Point(Display.rotateAround(point[5], this.getAngle() * (-1)));
		arr_PointTop[5] = new Point(Display.rotateAround(point_top[5], this.getAngle() * (-1)));

		int delta = (dx > dy) ? dx : dy;

		arr_Point[0] = new Point(arr_Point[0].x - delta, arr_Point[0].y - delta * 2);
		arr_Point[1] = new Point(arr_Point[1].x + delta, arr_Point[1].y - delta * 2);
		arr_Point[2] = new Point(arr_Point[2].x + delta, arr_Point[2].y);
		arr_Point[3] = new Point(arr_Point[3].x + delta, arr_Point[3].y + delta * 2);
		arr_Point[4] = new Point(arr_Point[4].x - delta, arr_Point[4].y + delta * 2);
		arr_Point[5] = new Point(arr_Point[5].x - delta, arr_Point[5].y);

		arr_PointTop[0] = new Point(arr_PointTop[0].x - delta, arr_PointTop[0].y - delta * 2);
		arr_PointTop[1] = new Point(arr_PointTop[1].x + delta, arr_PointTop[1].y - delta * 2);
		arr_PointTop[2] = new Point(arr_PointTop[2].x + delta, arr_PointTop[2].y - delta / 2);
		arr_PointTop[3] = new Point(arr_PointTop[3].x, arr_PointTop[3].y);
		arr_PointTop[4] = new Point(arr_PointTop[4].x - delta, arr_PointTop[4].y - delta / 2);
		arr_PointTop[5] = new Point(arr_PointTop[5].x + delta / 2, arr_PointTop[5].y - delta * 2);

		arr_PointBottom[0] = new Point(arr_PointBottom[0].x, arr_PointBottom[0].y);
		arr_PointBottom[1] = new Point(arr_PointBottom[1].x + delta * 95 / 100, arr_PointBottom[1].y + delta * 2 / 3);
		arr_PointBottom[2] = new Point(arr_PointBottom[2].x + delta * 2 / 3, arr_PointBottom[2].y + delta * 175 / 100);
		arr_PointBottom[3] = new Point(arr_PointBottom[3].x - delta * 2 / 3, arr_PointBottom[3].y + delta * 175 / 100);
		arr_PointBottom[4] = new Point(arr_PointBottom[4].x - delta * 95 / 100, arr_PointBottom[4].y + delta * 2 / 3);

		for (int i = 0; i < 5; i++) {
			arr_PointTop[i] = Display.rotateAround(arr_PointTop[i], this.getAngle());
			arr_PointBottom[i] = Display.rotateAround(arr_PointBottom[i], this.getAngle());
			arr_Point[i] = Display.rotateAround(arr_Point[i], this.getAngle());
		}
		arr_Point[5] = Display.rotateAround(arr_Point[5], this.getAngle());
		arr_PointTop[5] = Display.rotateAround(arr_PointTop[5], this.getAngle());

		point = arr_Point;
		point_top = arr_PointTop;
		point_bottom = arr_PointBottom;
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub

	}

}
