package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyOval implements Shapes2D {
	private Point A, B;
	private int size;
	private Color color;
	private float angle = 0;
	private Point O;
	private int R;
	private float speed = 4;
	public MyOval() {
	}

	public MyOval(Point a, Point b, int size, Color color, float angle) {
		A = a;
		B = b;
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

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public Point getO() {
		return O;
	}

	public void setO(Point o) {
		O = o;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public void draw(Graphics g) {
		// R = (int) (float) (Math.abs((A.x + B.x) / 2 - B.x));
		R = (int) ((Math.sqrt(Math.pow((B.x - A.x), 2) + Math.pow((B.y - A.y), 2)) / Math.sqrt(2))) / 2;
		O = new Point((A.x + B.x) / 2, (A.y + B.y) / 2);
		int p = 3 - 2 * R;
		int x = 0;
		int y = R;
		while (x <= y) {

			if (p < 0) {
				p = p + 4 * x + 6; // x+ 10000 ra hinh thoi . x-100000 ra hinh vuong
			} else {
				p = p + 4 * (x - y) + 10;
				y--;
			}
			ve8diem(O.x, O.y, x, y, g);
			x++;
		}
//		g.setColor(Color.BLUE);
//		g.drawString("O", O.x, O.y);
	}

	@Override
	public void init(Point start, Point end, int size, Color color) {
		int shortEdge; // Cạnh lấy theo cạnh ngắn của HCN
		this.setA(start);
		if ((start.x <= end.x) && (start.y < end.y)) { // góc Đông Nam
			shortEdge = Math.min(end.x - start.x, end.y - start.y);
			this.setB(new Point(shortEdge + start.x, shortEdge + start.y));
		} else if ((start.x > end.x) && (start.y <= end.y)) { // góc Tây Nam
			shortEdge = Math.min(start.x - end.x, end.y - start.y);
			this.setB(new Point(start.x - shortEdge, start.y + shortEdge));
		} else if ((start.x >= end.x) && (start.y > end.y)) { // góc Tây Bắc
			shortEdge = Math.min(start.x - end.x, start.y - end.y);
			this.setB(new Point(start.x - shortEdge, start.y - shortEdge));
		} else if ((start.x < end.x) && (start.y >= end.y)) { // góc Đông Bắc
			shortEdge = Math.min(end.x - start.x, start.y - end.y);
			this.setB(new Point(start.x + shortEdge, start.y - shortEdge));
		}
		this.setSize(size);
		this.setColor(color);
	}

	@Override
	public boolean impact(Point p) {
		double OP = Math.sqrt(Math.pow((p.x - O.x), 2) + Math.pow((p.y - O.y), 2));
		return (OP <= R);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point p1 = new Point(Display.rotateAround(this.getA(), a));
		Point p2 = new Point(Display.rotateAround(this.getB(), a));
		setA(p1);
		setB(p2);
		this.setAngle(this.getAngle() + a);
	}

	public void ve8diem(int x0, int y0, int x, int y, Graphics g) {
		g.setColor(color);
		g.fillRect(x0 + x, y0 + y, size, size);
		g.fillRect(x0 - x, y0 + y, size, size);
		g.fillRect(x0 + x, y0 - y, size, size);
		g.fillRect(x0 - x, y0 - y, size, size);
		g.fillRect(x0 + y, y0 + x, size, size);
		g.fillRect(x0 - y, y0 + x, size, size);
		g.fillRect(x0 + y, y0 - x, size, size);
		g.fillRect(x0 - y, y0 - x, size, size);
	}

	@Override
	public void scale(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		int delta = (dx > dy) ? dx : dy;
		MyOval oval;
		Point p1 = (Display.rotateAround(this.getA(), this.getAngle() * (-1)));
		Point p2 = (Display.rotateAround(this.getB(), this.getAngle() * (-1)));
		if (p1.x < p2.x && p1.y < p2.y) {
			p1 = new Point(p1.x - delta, p1.y - delta);
			p2 = new Point(p2.x + delta, p2.y + delta);
		} else if (p1.x > p2.x && p1.y < p2.y) {
			p1 = new Point(p1.x + delta, p1.y - delta);
			p2 = new Point(p2.x - delta, p2.y + delta);
		} else if (p1.x > p2.x && p1.y > p2.y) {
			p1 = new Point(p1.x + delta, p1.y + delta);
			p2 = new Point(p2.x - delta, p2.y - delta);
		} else if (p1.x < p2.x && p1.y > p2.y) {
			p1 = new Point(p1.x - delta, p1.y + delta);
			p2 = new Point(p2.x + delta, p2.y - delta);
		}
		p1 = Display.rotateAround(p1, this.getAngle());
		p2 = Display.rotateAround(p2, this.getAngle());
		this.setA(p1);
		this.setB(p2);
	}

	@Override
	public void play(float angle) {
		this.setAngle(this.getAngle() + angle);
	}

}
