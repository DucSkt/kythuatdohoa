package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class MyFlower implements Shapes2D {
	private Point A, B;
	private int size;
	private Color color;
	private float angle = 0;
	private int r;
	private float speed = 4;
	private Point O;

	public MyFlower() {

	}

	public MyFlower(Point a, Point b, int size, Color color, float angle) {
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
		return r;
	}

	public void setR(int r) {
		r = r;
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
		Graphics2D g2d = (Graphics2D) g;
		// int length_X = (int) (Math.sqrt((B.x - A.x) * (B.x - A.x) - (B.y - A.y) *
		// (B.y - A.y)));
		// int length_Y = (int) (Math.sqrt((B.y - A.y) * (B.y - A.y) - (B.x - A.x) *
		// (B.x - A.x)));
		r = (int) ((Math.sqrt(Math.pow((B.x - A.x), 2) + Math.pow((B.y - A.y), 2)) / Math.sqrt(2))) / 2;
		O = new Point((A.x + B.x) / 2, (A.y + B.y) / 2);
		int p = 3 - 2 * r;
		int x = 0;
		int y = r;

		while (x <= y) {

			if (p < 0) {
				p = p + 4 * (x) + 6;
			} else {
				p = p + 4 * (x - y) + 10;
				y--;
			}

			ve8diem(O.x, O.y, x, y, g);
			g2d.setColor(color);
			x++;
		}
		g.fillOval(O.x - r, O.y - r, 2 * r + 1, 2 * r + 1);
		int x1 = 0;
		int y1 = r;
		while (x1 <= y1) {

			if (p < 0) {
				p = p + 4 * (x1) + 6;
			} else {
				p = p + 4 * (x1 - y1) + 10;
				y1--;
			}
			ve8diem(O.x, O.y - (r + r / 3), x1, y1, g);
			x1++;
		}

		int x2 = 0;
		int y2 = r;
		while (x2 <= y2) {

			if (p < 0) {
				p = p + 4 * (x2) + 6;
			} else {
				p = p + 4 * (x2 - y2) + 10;
				y2--;
			}
			ve8diem(O.x + (r + r / 3), O.y - r / 3, x2, y2, g);
			x2++;
		}

		int x3 = 0;
		int y3 = r;
		while (x3 <= y3) {

			if (p < 0) {
				p = p + 4 * (x3) + 6;
			} else {
				p = p + 4 * (x3 - y3) + 10;
				y3--;
			}
			ve8diem(O.x - (r + r / 3), O.y - r / 3, x3, y3, g);
			x3++;
		}

		int x4 = 0;
		int y4 = r;
		while (x4 <= y4) {

			if (p < 0) {
				p = p + 4 * (x4) + 6;
			} else {
				p = p + 4 * (x4 - y4) + 10;
				y4--;
			}
			ve8diem(O.x - r + r / 6, O.y + (r + r / 3), x4, y4, g);
			x4++;
		}

		int x5 = 0;
		int y5 = r;
		while (x5 <= y5) {

			if (p < 0) {
				p = p + 4 * (x5) + 6;
			} else {
				p = p + 5 * (x5 - y5) + 10;
				y5--;
			}
			ve8diem(O.x + r - r / 6, O.y + r + r / 3, x5, y5, g);
			x5++;
		}
		
//		g.setColor(Color.BLUE);
//		g.drawString("O", this.getO().x, this.getO().y);

	}

	public void ve8diem(int x0, int y0, int x, int y, Graphics g) {
		g.fillRect(x0 + x, y0 - y, size, size); // 1
		g.fillRect(x0 + y, y0 - x, size, size); // 2
		g.fillRect(x0 + y, y0 + x, size, size); // 3
		g.fillRect(x0 + x, y0 + y, size, size); // 4
		g.fillRect(x0 - x, y0 + y, size, size); // 5
		g.fillRect(x0 - y, y0 + x, size, size); // 6
		g.fillRect(x0 - y, y0 - x, size, size); // 7
		g.fillRect(x0 - x, y0 - y, size, size); // 8

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
		return (OP <= r * 2);
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
		// TODO Auto-generated method stub

	}
}
