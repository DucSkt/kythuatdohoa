package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MyPinwheel implements Shapes2D {
	private Point A, B;
	private Point[] point = new Point[9];
	private int size;
	private Color color;
	private float angle = 0;
	private Point O;
	private int R;
	private float speed = 4;
	private boolean flag_Resize = false;

	public MyPinwheel() {
	}

	public MyPinwheel(Point a, Point b, int size, Color color, float angle, Point o, int r) {
		A = a;
		B = b;
		this.size = size;
		this.color = color;
		this.angle = angle;
		O = o;
		R = r;
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

	public Point[] getPoint() {
		return point;
	}

	public void setPoint(Point[] point) {
		this.point = point;
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
			MyLine line;
			int length_X = (B.x - A.x);
			int length_Y = (B.y - A.y);
//			O = new Point(length_X, length_Y);
			int middle_X = (A.x + B.x) / 2;
			int middle_Y = (A.y + B.y) / 2;
			point[0] = new Point(middle_X, A.y);
			point[1] = new Point(middle_X + length_X / 4, middle_Y - length_Y / 4);
			point[2] = new Point(B.x, middle_Y);
			point[3] = new Point(middle_X + length_X / 4, middle_Y + length_Y / 4);
			point[4] = new Point(middle_X, B.y);
			point[5] = new Point(middle_X - length_X / 4, middle_Y + length_Y / 4);
			point[6] = new Point(A.x, middle_Y);
			point[7] = new Point(middle_X - length_X / 4, middle_Y - length_Y / 4);
			point[8] = new Point(middle_X, middle_Y); // Tâm
		}

		if (size > 1) {
			MyLine line;
			line = new MyLine(point[0], point[4]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point[2], point[6]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point[1], point[5]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(point[3], point[7]);
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			for (int i = 0; i < 8; i += 2) {
				line = new MyLine(point[i], point[i + 1]);
				line.setSize(size);
				line.setColor(color);
				line.draw(g);
			}
		} else {
			g.setColor(color);
			g.drawLine(point[0].x, point[0].y, point[4].x, point[4].y);
			g.drawLine(point[2].x, point[2].y, point[6].x, point[6].y);
			g.drawLine(point[1].x, point[1].y, point[5].x, point[5].y);
			g.drawLine(point[3].x, point[3].y, point[7].x, point[7].y);
			for (int i = 0; i < 8; i += 2) {
				g.drawLine(point[i].x, point[i].y, point[i + 1].x, point[i + 1].y);
			}
		}

		// int r = (int)(float)(Math.abs((A.x+B.x)/2 - B.x));
		// int p = 3-2*r;
		// int x=0;
		// int y=r;
		// while(x <= y ) {
		// y--;
		// ve4diem((A.x+B.x)/2, (A.y+B.y)/2, x , y ,g);
		// x++;
		// }
		// int x1=0;
		// int y1=r;
		// while(x1 <= y1 ) {
		// y1--;
		// ve4diem((A.x+B.x)/2, (A.y+B.y)/2, x1 , y1 ,g);
		// x++;
		// }
		// int x2=0;
		// int y2=r;
		// float m,n;
		// m = A.x;
		// n = A.y;
		// while(x2<=y2){
		// g.fillRect(((int)m+B.x) /2, ((int)n+B.y)/2,size,size);
		// m += 1;
		// n += 1;
		// x2++;
		// }
		// int x3=0;
		// int y3=r;
		// float m1 ,n1 ;
		// m1 = A.x;
		// n1 = A.y;
		// while(x3<y3){
		// g.fillRect(((int)m1+B.x)/2, ((int)n1+B.y)/2,size,size);
		// m1 -= 1;
		// n1 += 1;
		// x3++;
		//
		// }
		// int x4=0;
		// int y4=r;
		// float m2 ,n2 ;
		// m2= A.x;
		// n2= A.y;
		// while(x4<=y4){
		// g.fillRect(((int)m2+B.x)/2, ((int)n2+B.y)/2,size,size);
		// m2 -= 1;
		// n2 -= 1;
		// x4++;
		//
		// }
		// int x5=0;
		// int y5=r;
		// float m3 ,n3 ;
		// m3 = A.x;
		// n3 = A.y;
		// while(x5<=y5){
		// g.fillRect(((int)m3+B.x)/2, ((int)n3+B.y)/2,size,size);
		// m3 += 1;
		// n3 -= 1;
		// x5++;
		//
		// }
		
//		g.setColor(Color.BLUE);
//		char [] c = {'A','B','C','D','E','F','G','H','I'};
//		for (int i = 0; i < 9; i++) {
//			g.drawString(String.valueOf(c[i]), point[i].x, point[i].y);
//		}
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
		double ABO = Display.areaTriangle(point[0], point[1], point[8]);
		double PAB = Display.areaTriangle(p, point[0], point[1]);
		double PAO = Display.areaTriangle(p, point[0], point[8]);
		double PBO = Display.areaTriangle(p, point[1], point[8]);

		boolean t1 = (PAB + PAO + PBO == ABO);

		double CDO = Display.areaTriangle(point[2], point[3], point[8]);
		double PCD = Display.areaTriangle(p, point[2], point[3]);
		double PCO = Display.areaTriangle(p, point[2], point[8]);
		double PDO = Display.areaTriangle(p, point[3], point[8]);

		boolean t2 = (PCD + PCO + PDO == CDO);

		double EFO = Display.areaTriangle(point[4], point[5], point[8]);
		double PEF = Display.areaTriangle(p, point[4], point[5]);
		double PEO = Display.areaTriangle(p, point[4], point[8]);
		double PFO = Display.areaTriangle(p, point[5], point[8]);

		boolean t3 = (PEF + PEO + PFO == EFO);

		double GHO = Display.areaTriangle(point[6], point[7], point[8]);
		double PGH = Display.areaTriangle(p, point[6], point[7]);
		double PGO = Display.areaTriangle(p, point[6], point[8]);
		double PHO = Display.areaTriangle(p, point[7], point[8]);

		boolean t4 = (PGH + PGO + PHO == GHO);
		return (t1 || t2 || t3 || t4);
	}

	@Override
	public void move(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.setA(new Point(this.getA().x + dx, this.getA().y + dy));
		this.setB(new Point(this.getB().x + dx, this.getB().y + dy));
		for (int i = 0; i < 9; i++) {
			point[i] = new Point(point[i].x + dx, point[i].y + dy);
		}
	}

	@Override
	public void rotate(Point start, Point end) {
		float a = Display.angleBetweenTwoLines(start, end);
		Point[] p = new Point[9];
		for (int i = 0; i < 9; i++) {
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
		int delta = (dx > dy) ? dx : dy;
		Point[] arr = new Point[9];
		for (int i = 0; i < 9; i++) {
			arr[i] = Display.rotateAround(point[i], this.getAngle() * (-1));
		}
		point[0] = Display.rotateAround(new Point(arr[0].x, arr[0].y - delta*2), this.getAngle());
		point[1] = Display.rotateAround(new Point(arr[1].x + delta, arr[1].y - delta), this.getAngle());
		point[2] = Display.rotateAround(new Point(arr[2].x + delta*2, arr[2].y), this.getAngle());
		point[3] = Display.rotateAround(new Point(arr[3].x + delta, arr[3].y + delta), this.getAngle());
		point[4] = Display.rotateAround(new Point(arr[4].x, arr[4].y + delta*2), this.getAngle());
		point[5] = Display.rotateAround(new Point(arr[5].x - delta, arr[5].y + delta), this.getAngle());
		point[6] = Display.rotateAround(new Point(arr[6].x - delta*2, arr[6].y), this.getAngle());
		point[7] = Display.rotateAround(new Point(arr[7].x - delta, arr[7].y - delta), this.getAngle());
		point[8] = Display.rotateAround(arr[8], this.getAngle());
	}

	@Override
	public void play(float angle) {
		// TODO Auto-generated method stub

	}

	public void ve4diem(int x0, int y0, int x, int y, Graphics g) {
		g.fillRect(x0 + y, y0 - x, size, size); // 2
		g.fillRect(x0 + x, y0 + y, size, size); // 4
		g.fillRect(x0 - y, y0 + x, size, size); // 6
		g.fillRect(x0 - x, y0 - y, size, size); // 8

	}

}
