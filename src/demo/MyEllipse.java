package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyEllipse implements Shapes2D {
	private Point A, B, C, D;
	private int size;
	private Color color;
	private float angle = 0;
	private float speed = 4;
	private int R1;
	private int R2;

	public MyEllipse() {
	}

	public MyEllipse(Point a, Point b, Point c, Point d, int size, Color color, float angle) {
		A = a;
		B = b;
		C = c;
		D = d;
		this.size = size;
		this.color = color;
		this.angle = angle;
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

	public int getR1() {
		return R1;
	}

	public void setR1(int r1) {
		R1 = r1;
	}

	public int getR2() {
		return R2;
	}

	public void setR2(int r2) {
		R2 = r2;
	}

	@Override
	public void draw(Graphics g) {
		float dx = (float) this.getA().distance(this.getB()) / 2;
		float dy = (float) this.getA().distance(this.getD()) / 2;
		float rxSq = dx * dx;
		float rySq = dy * dy;
		float x1 = 0;
		float y1 = dy;
		float p;
		float px = 0;
		float py = 2 * rxSq * y1;
		R1 = (int) dx;
		R2 = (int) dy;
		g.setColor(this.getColor());
		Point p1 = new Point(Display.rotateAround(this.getO(), this.getAngle() * (-1)));
		// Region 1
		p = rySq - (rxSq * dy) + (float) (0.25 * rxSq);

		while (px < py) {
			x1++;
			px = px + 2 * rySq;
			if (p < 0) {
				p = p + rySq + px;
			} else {
				y1--;
				py = py - 2 * rxSq;
				p = p + rySq + px - py;
			}
			ve4diem(g, p1.x, p1.y, (int) x1, (int) y1, this.getAngle());
		}

		// Region 2
		p = (float) (rySq * (x1 + 0.5) * (x1 + 0.5) + rxSq * (y1 - 1) * (y1 - 1) - rxSq * rySq);
		while (y1 > 0) {
			y1--;
			py = py - 2 * rxSq;
			if (p > 0) {
				p = p + rxSq - py;
			} else {
				x1++;
				px = px + 2 * rySq;
				p = p + rxSq - py + px;
			}
			ve4diem(g, p1.x, p1.y, (int) x1, (int) y1, this.getAngle());
		}
//		g.setColor(Color.BLUE);
//		g.drawString("A", this.getA().x, this.getA().y);
//		g.drawString("B", this.getB().x, this.getB().y);
//		g.drawString("C", this.getC().x, this.getC().y);
//		g.drawString("D", this.getD().x, this.getD().y);
	}

	@Override
	public void init(Point start, Point end, int size, Color color) {
		this.setA(start);
		this.setB(new Point(end.x, start.y));
		this.setC(end);
		this.setD(new Point(start.x, end.y));
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
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point p1 = new Point(Display.rotateAround(this.getA(), a));
		Point p2 = new Point(Display.rotateAround(this.getB(), a));
		Point p3 = new Point(Display.rotateAround(this.getC(), a));
		Point p4 = new Point(Display.rotateAround(this.getD(), a));
		this.setA(p1);
		this.setB(p2);
		this.setC(p3);
		this.setD(p4);
		this.setAngle(this.getAngle() + a);
	}

	@Override
	public void scale(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		Point p1 = new Point(Display.rotateAround(this.getA(), this.getAngle() * (-1)));
		Point p2 = new Point(Display.rotateAround(this.getB(), this.getAngle() * (-1)));
		Point p3 = new Point(Display.rotateAround(this.getC(), this.getAngle() * (-1)));
		Point p4 = new Point(Display.rotateAround(this.getD(), this.getAngle() * (-1)));

		if (p1.x < p2.x && p1.y < p4.y) {
			p1 = new Point(p1.x - dx, p1.y - dy);
			p2 = new Point(p2.x + dx, p2.y - dy);
			p3 = new Point(p3.x + dx, p3.y + dy);
			p4 = new Point(p4.x - dx, p4.y + dy);
		} else if (p1.x > p2.x && p1.y < p4.y) {
			p1 = new Point(p1.x + dx, p1.y - dy);
			p2 = new Point(p2.x - dx, p2.y - dy);
			p3 = new Point(p3.x - dx, p3.y + dy);
			p4 = new Point(p4.x + dx, p4.y + dy);
		} else if (p1.x < p2.x && p1.y > p4.y) {
			p1 = new Point(p1.x - dx, p1.y + dy);
			p2 = new Point(p2.x + dx, p2.y + dy);
			p3 = new Point(p3.x + dx, p3.y - dy);
			p4 = new Point(p4.x - dx, p4.y - dy);
		} else if (p1.x > p2.x && p1.y > p4.y) {
			p1 = new Point(p1.x + dx, p1.y + dy);
			p2 = new Point(p2.x - dx, p2.y + dy);
			p3 = new Point(p3.x - dx, p3.y - dy);
			p4 = new Point(p4.x + dx, p4.y - dy);
		}

		setA(Display.rotateAround(p1, this.getAngle()));
		setB(Display.rotateAround(p2, this.getAngle()));
		setC(Display.rotateAround(p3, this.getAngle()));
		setD(Display.rotateAround(p4, this.getAngle()));
	}

	public void ve4diem(Graphics g, int xc, int yc, int x, int y, float angle) {
		Point p1 = new Point(xc + x, yc + y);
		Point p2 = new Point(xc - x, yc + y);
		Point p3 = new Point(xc - x, yc - y);
		Point p4 = new Point(xc + x, yc - y);

		p1 = Display.rotateAround(p1, angle);
		p2 = Display.rotateAround(p2, angle);
		p3 = Display.rotateAround(p3, angle);
		p4 = Display.rotateAround(p4, angle);

		g.fillRect(p1.x, p1.y, size, size);
		g.fillRect(p2.x, p2.y, size, size);
		g.fillRect(p3.x, p3.y, size, size);
		g.fillRect(p4.x, p4.y, size, size);
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub

	}

	public Point getO() {
		Point p = new Point();
		p.x = Math.min(this.getA().x, this.getC().x) + Math.abs(this.getA().x - this.getC().x) / 2;
		p.y = Math.min(this.getA().y, this.getC().y) + Math.abs(this.getA().y - this.getC().y) / 2;
		return p;
	}
}
