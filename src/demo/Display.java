package demo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Font;
import java.awt.event.MouseMotionAdapter;
import javax.swing.border.BevelBorder;
import java.awt.geom.AffineTransform;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.JEditorPane;

public class Display extends JFrame {

	/**
	 * @author NGUYEN HUY
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private Point move_start; // Lưu trữ tọa độ bắt đầu của drawPanel so với JFrame

	// Khởi tạo các đối tượng
	private Point startPoint = null, endPoint = null; // Lưu trữ điểm bắt đầu và kết thúc khi kéo thả chuột
	private String selectButton = ""; // Lưu trữ đánh dấu button nào được chọn bằng string tự định nghĩa
	// Interface Shapes2D giúp mảng lưu trữ các hình dạng khác nhau về cùng 1 mảng
	private ArrayList<Shapes2D> paint2D = new ArrayList<Shapes2D>();
	private Stack<Shapes2D> stack = new Stack<Shapes2D>(); // Stack dùng cho Undo và Redo
	private Shapes2D shape_temp = null; // Biến tạm lưu trữ hình dx chọn khi chọn move,delete,rotate,..
	private Shapes2D temp_Projection = null; // Tạo thêm 1 đối tượng khi dùng phép chiếu và thêm vào ArrayList
	private Point localtionFrame = null; // Cập nhật tọa độ của JFrame mỗi khi user di chuyển khung hình
	private static Point centerAxisPoint = null; // Lưu trữ tâm của drawPanel
	private static Point flag_Point = null; // Lưu trữ tọa độ của Flag khi FlagButton bật
	private int size; // Size của nét vẽ
	private int speed;
	private Color color = Color.BLACK; // Màu của nét vẽ

	// Khởi tạo các thực thể và các button
	private MyLine myLine;
	private MyOval myOval;
	private MyEllipse myEllipse;
	private MyRect myRect;
	private MySquare mySquare;
	private MyParallelogram myParallelogram;
	private MyTriangleSquare myTriangleSquare;
	private MyTriangleIsosceles myTriangleIsosceles;
	private MyRhombus myRhombus;
	private MyPentagon myPentagon;
	private MyHexagon myHexagon;
	private MyFourPointStar myFourPointStar;
	private MyFivePointStar myFivePointStar;
	private MyFivePointStarLine myFivePointStarLine;
	private MySixPointStar mySixPointStar;
	private MyPencil myPencil;
	private MyFlower myFlower;
	private MyPinwheel myPinwheel;
	private MyMedal myMedal;
	private MyNationalFlag myNationalFlag;

	private JButton btnLine;
	private JButton btnOval;
	private JButton btnEllipse;
	private JButton btnRectangle;
	private JButton btnSquare;
	private JButton btnTriangleSquare;
	private JButton btnParallelogram;
	private JButton btnTriangleIsosceles;
	private JButton btnRhombus;
	private JButton btnPentagon;
	private JButton btnHexagon;
	private JButton btnFourPointStar;
	private JButton btnFivePointStar;
	private JButton btnFivePointStarLine;
	private JButton btnSixPointStar;
	private JSlider sliderSize;
	private JButton btnPencil;
	private JButton btnColor;
	private JButton btnUndo;
	private JButton btnRedo;
	private JButton btnClear;
	private JLabel lbSize;
	private JLabel lblSize;
	private JPanel drawPanel;
	private JButton btnMove;
	private JButton btnRotate;
	private JButton btnScale;
	private JButton btnDelete;
	private JButton btnOxProjection;
	private JButton btnOyProjection;
	private JButton btnOxyProjection;
	private JButton btnFlag;
	private JLabel lbLogo;

	private static boolean flagBoolean;
	private boolean playBoolean;
	private Cursor flag;
	private Cursor rotate;
	private Cursor scale;
	private Cursor delete;
	private Cursor default_cursor;
	private Cursor pencil;
	private Cursor fillColor;

	private JLabel lbMove;
	private JLabel lbDrag;
	private JLabel lbAngle;
	private JLabel lbBackground;
	private JButton btnPlay;
	private JButton btnConfig;
	private JButton btnFlower;
	private JButton btnPinwheel;
	private JButton btnInformation;
	private JLabel lbSpeed;
	private JSlider sliderSpeed;
	private JButton btnMedal;
	private JButton btnNationalFlag;

	public static void main(String[] args) {
		Display frame = new Display();
		String str = "Nguyễn Hà Minh Huy     N15DCCN116 \nĐặng Hoàng Khang       N15DCCN073 "
				+ "\nTrần Nhật Tường           N15DCCN089 \nCao Thị Kim Ngân          N15DCCN119"
				+ "\nĐặng Chí Bảo                  N15DCCN067 \nVõ Trần Thành                N15DCCN123";
		frame.setVisible(true);
		JOptionPane.showConfirmDialog(null, str, "Nhóm 19", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon("Image/infor.png"));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawAxisCoordinates(g); // Vẽ trục tọa độ

		if (startPoint != null && endPoint != null) {
			if ("Line".equals(selectButton)) {
				myLine = new MyLine();
				myLine.init(startPoint, endPoint, size, color);
				myLine.draw(g);
				stack.clear();
			} else if ("Oval".equals(selectButton)) {
				myOval = new MyOval();
				myOval.init(startPoint, endPoint, size, color);
				myOval.draw(g);
				stack.clear();
			} else if ("Ellipse".equals(selectButton)) {
				myEllipse = new MyEllipse();
				myEllipse.init(startPoint, endPoint, size, color);
				myEllipse.draw(g);
				stack.clear();
			} else if ("Rectangle".equals(selectButton)) {
				myRect = new MyRect();
				myRect.init(startPoint, endPoint, size, color);
				myRect.draw(g);
				stack.clear();
			} else if ("Square".equals(selectButton)) {
				mySquare = new MySquare();
				mySquare.init(startPoint, endPoint, size, color);
				mySquare.draw(g);
				stack.clear();
			} else if ("Parallelogram".equals(selectButton)) {
				myParallelogram = new MyParallelogram();
				myParallelogram.init(startPoint, endPoint, size, color);
				myParallelogram.draw(g);
				stack.clear();
			} else if ("TriangleSquare".equals(selectButton)) {
				myTriangleSquare = new MyTriangleSquare();
				myTriangleSquare.init(startPoint, endPoint, size, color);
				myTriangleSquare.draw(g);
				stack.clear();
			} else if ("TriangleIsosceles".equals(selectButton)) {
				myTriangleIsosceles = new MyTriangleIsosceles();
				myTriangleIsosceles.init(startPoint, endPoint, size, color);
				myTriangleIsosceles.draw(g);
				stack.clear();
			} else if ("Rhombus".equals(selectButton)) {
				myRhombus = new MyRhombus();
				myRhombus.init(startPoint, endPoint, size, color);
				myRhombus.draw(g);
				stack.clear();
			} else if ("Pentagon".equals(selectButton)) {
				myPentagon = new MyPentagon();
				myPentagon.init(startPoint, endPoint, size, color);
				myPentagon.draw(g);
				stack.clear();
			} else if ("Hexagon".equals(selectButton)) {
				myHexagon = new MyHexagon();
				myHexagon.init(startPoint, endPoint, size, color);
				myHexagon.draw(g);
				stack.clear();
			} else if ("FourPointStar".equals(selectButton)) {
				myFourPointStar = new MyFourPointStar();
				myFourPointStar.init(startPoint, endPoint, size, color);
				myFourPointStar.draw(g);
				stack.clear();
			} else if ("FivePointStar".equals(selectButton)) {
				myFivePointStar = new MyFivePointStar();
				myFivePointStar.init(startPoint, endPoint, size, color);
				myFivePointStar.draw(g);
				stack.clear();
			} else if ("FivePointStarLine".equals(selectButton)) {
				myFivePointStarLine = new MyFivePointStarLine();
				myFivePointStarLine.init(startPoint, endPoint, size, color);
				myFivePointStarLine.draw(g);
				stack.clear();
			} else if ("SixPointStar".equals(selectButton)) {
				mySixPointStar = new MySixPointStar();
				mySixPointStar.init(startPoint, endPoint, size, color);
				mySixPointStar.draw(g);
				stack.clear();
			} else if ("Pencil".equals(selectButton)) {
				myPencil.init(startPoint, endPoint, size, color);
				myPencil.draw(g);
				stack.clear();
			} else if ("Flower".equals(selectButton)) {
				myFlower = new MyFlower();
				myFlower.init(startPoint, endPoint, size, color);
				myFlower.draw(g);
				stack.clear();
			} else if ("Pinwheel".equals(selectButton)) {
				myPinwheel = new MyPinwheel();
				myPinwheel.init(startPoint, endPoint, size, color);
				myPinwheel.draw(g);
				stack.clear();
			} else if ("Medal".equals(selectButton)) {
				myMedal = new MyMedal();
				myMedal.init(startPoint, endPoint, size, color);
				myMedal.draw(g);
				stack.clear();
			} else if ("NationalFlag".equals(selectButton)) {
				myNationalFlag = new MyNationalFlag();
				myNationalFlag.init(startPoint, endPoint, size, color);
				myNationalFlag.draw(g);
				stack.clear();
			} else if ("Move".equals(selectButton)) {
				int dx = endPoint.x - startPoint.x;
				int dy = endPoint.y - startPoint.y;
				if (shape_temp instanceof MyLine) {
					MyLine temp = (MyLine) shape_temp;
					if (temp.impact(startPoint)) {
						if (temp.getSize() > 1) {
							MyLine line = new MyLine(new Point(temp.getA().x + dx, temp.getA().y + dy),
									new Point(temp.getB().x + dx, temp.getB().y + dy));
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
						} else {
							g.setColor(color);
							g.drawLine(temp.getA().x + dx, temp.getA().y + dy, temp.getB().x + dx, temp.getB().y + dy);
						}
					}
				} else if (shape_temp instanceof MyOval) {
					MyOval temp = (MyOval) shape_temp;
					if (temp.impact(startPoint)) {
						int x = temp.getA().x;
						int y = temp.getA().y;
						MyOval oval = new MyOval();
						oval.setA(new Point(x + dx, y + dy));
						oval.setB(new Point(temp.getB().x + dx, temp.getB().y + dy));
						oval.setSize(temp.getSize());
						oval.setColor(temp.getColor());
						oval.draw(g);
					}
				} else if (shape_temp instanceof MyEllipse) {
					MyEllipse temp = (MyEllipse) shape_temp;
					if (temp.impact(startPoint)) {
						MyEllipse ellipse = new MyEllipse();
						ellipse.setA(new Point(temp.getA().x + dx, temp.getA().y + dy));
						ellipse.setB(new Point(temp.getB().x + dx, temp.getB().y + dy));
						ellipse.setC(new Point(temp.getC().x + dx, temp.getC().y + dy));
						ellipse.setD(new Point(temp.getD().x + dx, temp.getD().y + dy));
						ellipse.setSize(temp.getSize());
						ellipse.setColor(temp.getColor());
						ellipse.draw(g);
					}
				} else if (shape_temp instanceof MyRect) {
					MyRect temp = (MyRect) shape_temp;
					if (temp.impact(startPoint)) {
						executePainting4Diem(temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), dx, dy, g);
					}
				} else if (shape_temp instanceof MySquare) {
					MySquare temp = (MySquare) shape_temp;
					if (temp.impact(startPoint)) {
						executePainting4Diem(temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), dx, dy, g);
					}
				} else if (shape_temp instanceof MyParallelogram) {
					MyParallelogram temp = (MyParallelogram) shape_temp;
					if (temp.impact(startPoint)) {
						executePainting4Diem(temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), dx, dy, g);
					}
				} else if (shape_temp instanceof MyTriangleSquare) {
					MyTriangleSquare temp = (MyTriangleSquare) shape_temp;
					if (temp.impact(startPoint)) {
						executePainting3Diem(temp.getA(), temp.getB(), temp.getC(), temp.getColor(), temp.getSize(), dx,
								dy, g);
					}
				} else if (shape_temp instanceof MyTriangleIsosceles) {
					MyTriangleIsosceles temp = (MyTriangleIsosceles) shape_temp;
					if (temp.impact(startPoint)) {
						executePainting3Diem(temp.getA(), temp.getB(), temp.getC(), temp.getColor(), temp.getSize(), dx,
								dy, g);
					}
				} else if (shape_temp instanceof MyRhombus) {
					MyRhombus temp = (MyRhombus) shape_temp;
					if (temp.impact(startPoint)) {
						executePainting4Diem(temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), dx, dy, g);
					}
				} else if (shape_temp instanceof MyPentagon) {
					MyPentagon temp = (MyPentagon) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executePaintingMultiplePoint(arr_Point, dx, dy, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyHexagon) {
					MyHexagon temp = (MyHexagon) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executePaintingMultiplePoint(arr_Point, dx, dy, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFourPointStar) {
					MyFourPointStar temp = (MyFourPointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executePaintingMultiplePoint(arr_Point, dx, dy, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFivePointStar) {
					MyFivePointStar temp = (MyFivePointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executePaintingMultiplePoint(arr_Point, dx, dy, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFivePointStarLine) {
					MyFivePointStarLine temp = (MyFivePointStarLine) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						int[] arr_Point_X = new int[6];
						int[] arr_Point_Y = new int[6];
						arr_Point_X[0] = arr_Point[3].x + dx;
						arr_Point_Y[0] = arr_Point[3].y + dy;
						arr_Point_X[1] = arr_Point[0].x + dx;
						arr_Point_Y[1] = arr_Point[0].y + dy;
						arr_Point_X[2] = arr_Point[2].x + dx;
						arr_Point_Y[2] = arr_Point[2].y + dy;
						arr_Point_X[3] = arr_Point[4].x + dx;
						arr_Point_Y[3] = arr_Point[4].y + dy;
						arr_Point_X[4] = arr_Point[1].x + dx;
						arr_Point_Y[4] = arr_Point[1].y + dy;
						arr_Point_X[5] = arr_Point[3].x + dx;
						arr_Point_Y[5] = arr_Point[3].y + dy;
						executeDrawStarLine(arr_Point_X, arr_Point_Y, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MySixPointStar) {
					MySixPointStar temp = (MySixPointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						MyLine line;
						if (temp.getSize() > 1) {
							for (int i = 0; i < 4; i += 2) {
								line = new MyLine();
								line.setA(new Point(arr_Point[i].x + dx, arr_Point[i].y + dy));
								line.setB(new Point(arr_Point[i + 2].x + dx, arr_Point[i + 2].y + dy));
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							line = new MyLine();
							line.setA(new Point(arr_Point[4].x + dx, arr_Point[4].y + dy));
							line.setB(new Point(arr_Point[0].x + dx, arr_Point[0].y + dy));
							line.setColor(temp.getColor());
							line.setSize(temp.getSize());
							line.draw(g);
							for (int i = 1; i < 5; i += 2) {
								line = new MyLine();
								line.setA(new Point(arr_Point[i].x + dx, arr_Point[i].y + dy));
								line.setB(new Point(arr_Point[i + 2].x + dx, arr_Point[i + 2].y + dy));
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							line = new MyLine();
							line.setA(new Point(arr_Point[5].x + dx, arr_Point[5].y + dy));
							line.setB(new Point(arr_Point[1].x + dx, arr_Point[1].y + dy));
							line.setColor(temp.getColor());
							line.setSize(temp.getSize());
							line.draw(g);
						} else {
							g.setColor(temp.getColor());
							for (int i = 0; i < 4; i += 2) {
								g.drawLine(arr_Point[i].x + dx, arr_Point[i].y + dy, arr_Point[i + 2].x + dx,
										arr_Point[i + 2].y + dy);
							}
							g.drawLine(arr_Point[4].x + dx, arr_Point[4].y + dy, arr_Point[0].x + dx,
									arr_Point[0].y + dy);
							for (int i = 1; i < 5; i += 2) {
								g.drawLine(arr_Point[i].x + dx, arr_Point[i].y + dy, arr_Point[i + 2].x + dx,
										arr_Point[i + 2].y + dy);
							}
							g.drawLine(arr_Point[5].x + dx, arr_Point[5].y + dy, arr_Point[1].x + dx,
									arr_Point[1].y + dy);
						}
					}
				} else if (shape_temp instanceof MyFlower) {
					MyFlower temp = (MyFlower) shape_temp;
					if (temp.impact(startPoint)) {
						int x = temp.getA().x;
						int y = temp.getA().y;
						MyFlower flower = new MyFlower();
						flower.setA(new Point(x + dx, y + dy));
						flower.setB(new Point(temp.getB().x + dx, temp.getB().y + dy));
						flower.setSize(temp.getSize());
						flower.setColor(color);
						flower.draw(g);
					}
				} else if (shape_temp instanceof MyPinwheel) {
					MyPinwheel temp = (MyPinwheel) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[9];
						for (int i = 0; i < 9; i++) {
							arr[i] = new Point(arr_Point[i].x + dx, arr_Point[i].y + dy);
						}
						if (temp.getSize() > 1) {
							MyLine line;
							line = new MyLine(arr[0], arr[4]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[2], arr[6]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[1], arr[5]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[3], arr[7]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							for (int i = 0; i < 8; i += 2) {
								line = new MyLine(arr[i], arr[i + 1]);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							}
						} else {
							g.setColor(temp.getColor());
							g.drawLine(arr[0].x, arr[0].y, arr[4].x, arr[4].y);
							g.drawLine(arr[2].x, arr[2].y, arr[6].x, arr[6].y);
							g.drawLine(arr[1].x, arr[1].y, arr[5].x, arr[5].y);
							g.drawLine(arr[3].x, arr[3].y, arr[7].x, arr[7].y);
							for (int i = 0; i < 8; i += 2) {
								g.drawLine(arr[i].x, arr[i].y, arr[i + 1].x, arr[i + 1].y);
							}
						}
					}
				} else if (shape_temp instanceof MyMedal) {
					MyMedal temp = (MyMedal) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] point = temp.getPoint();
						Point[] point_top = temp.getPoint_top();
						Point[] point_bottom = temp.getPoint_bottom();

						Point[] arr_Point = new Point[6];
						Point[] arr_PointTop = new Point[6];
						Point[] arr_PointBottom = new Point[5];
						for (int i = 0; i < 5; i++) {
							arr_PointTop[i] = new Point(point_top[i].x + dx, point_top[i].y + dy);
							arr_PointBottom[i] = new Point(point_bottom[i].x + dx, point_bottom[i].y + dy);
							arr_Point[i] = new Point(point[i].x + dx, point[i].y + dy);
						}
						arr_PointTop[5] = new Point(point_top[5].x + dx, point_top[5].y + dy);
						arr_Point[5] = new Point(point[5].x + dx, point[5].y + dy);

						// Vẽ hình
						MyLine line;
						if (size > 1) {
							// Vẽ nửa phần trên
							for (int i = 0; i < 4; i++) {
								line = new MyLine(arr_PointTop[i], arr_PointTop[i + 1]);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							}
							line = new MyLine(arr_PointTop[4], arr_PointTop[0]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr_PointTop[0], arr_PointTop[3]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr_PointTop[5], arr_PointTop[2]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);

							// Vẽ nửa phần dưới
							line = new MyLine(arr_PointBottom[0], arr_PointBottom[2]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[0], arr_PointBottom[3]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[1], arr_PointBottom[3]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[1], arr_PointBottom[4]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[2], arr_PointBottom[4]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);

							MyOval oval = new MyOval(arr_Point[5], arr_Point[3], temp.getSize(), temp.getColor(),
									temp.getAngle());
							oval.draw(g);
						} else {
							g.setColor(temp.getColor());
							// Vẽ nửa phần trên
							for (int i = 0; i < 4; i++) {
								g.drawLine(arr_PointTop[i].x, arr_PointTop[i].y, arr_PointTop[i + 1].x,
										arr_PointTop[i + 1].y);
							}
							g.drawLine(arr_PointTop[4].x, arr_PointTop[4].y, arr_PointTop[0].x, arr_PointTop[0].y);
							g.drawLine(arr_PointTop[0].x, arr_PointTop[0].y, arr_PointTop[3].x, arr_PointTop[3].y);
							g.drawLine(arr_PointTop[5].x, arr_PointTop[5].y, arr_PointTop[2].x, arr_PointTop[2].y);

							// Vẽ nửa phần dưới
							g.drawLine(arr_PointBottom[0].x, arr_PointBottom[0].y, arr_PointBottom[2].x,
									arr_PointBottom[2].y);
							g.drawLine(arr_PointBottom[0].x, arr_PointBottom[0].y, arr_PointBottom[3].x,
									arr_PointBottom[3].y);
							g.drawLine(arr_PointBottom[1].x, arr_PointBottom[1].y, arr_PointBottom[3].x,
									arr_PointBottom[3].y);
							g.drawLine(arr_PointBottom[1].x, arr_PointBottom[1].y, arr_PointBottom[4].x,
									arr_PointBottom[4].y);
							g.drawLine(arr_PointBottom[2].x, arr_PointBottom[2].y, arr_PointBottom[4].x,
									arr_PointBottom[4].y);
							MyOval oval = new MyOval(arr_Point[5], arr_Point[3], temp.getSize(), temp.getColor(),
									temp.getAngle());
							oval.draw(g);
						}
					}
				} else if (shape_temp instanceof MyNationalFlag) {
					MyNationalFlag temp = (MyNationalFlag) shape_temp;
					if (temp.impact(startPoint)) {
						MyLine line;
						Point[] point_Flag = temp.getPoint_flag();
						Point[] point_Star = temp.getPoint_star();

						Point[] arr_PointFlag = new Point[5];
						Point[] arr_PointStar = new Point[10];
						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = new Point(point_Flag[i].x + dx, point_Flag[i].y + dy);
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = new Point(point_Star[i].x + dx, point_Star[i].y + dy);
						}
						// Vẽ hình
						if (size > 1) {
							// Vẽ lá cờ
							for (int i = 0; i < 3; i++) {
								line = new MyLine(arr_PointFlag[i], arr_PointFlag[i + 1]);
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							// Vẽ cây cờ
							line = new MyLine(arr_PointFlag[4], arr_PointFlag[0]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize() * 2);
							line.draw(g);
							// Vẽ ngôi sao
							for (int i = 0; i < 9; i++) {
								line = new MyLine(arr_PointStar[i], arr_PointStar[i + 1]);
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							line = new MyLine(arr_PointStar[9], arr_PointStar[0]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize());
							line.draw(g);
						} else {
							// Vẽ lá cờ
							g.setColor(temp.getColor());
							for (int i = 0; i < 3; i++) {
								g.drawLine(arr_PointFlag[i].x, arr_PointFlag[i].y, arr_PointFlag[i + 1].x,
										arr_PointFlag[i + 1].y);
							}
							// Vẽ cây cờ
							line = new MyLine(arr_PointFlag[0], arr_PointFlag[4]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize() * 2);
							line.draw(g);
							// Vẽ ngôi sao
							g.setColor(temp.getColor());
							for (int i = 0; i < 9; i++) {
								g.drawLine(arr_PointStar[i].x, arr_PointStar[i].y, arr_PointStar[i + 1].x,
										arr_PointStar[i + 1].y);
							}
							g.drawLine(arr_PointStar[9].x, arr_PointStar[9].y, arr_PointStar[0].x, arr_PointStar[0].y);
						}
					}
				}
			} else if ("Rotate".equals(selectButton)) {
				float angle = angleBetweenTwoLines(startPoint, endPoint);
				lbAngle.setVisible(true);
				lbAngle.setText(Math.round(angle * 100.0) / 100.0 + " Độ");
				if (shape_temp instanceof MyLine) {
					MyLine temp = (MyLine) shape_temp;
					if (temp.impact(startPoint)) {

					}
				} else if (shape_temp instanceof MyOval) {
					MyOval temp = (MyOval) shape_temp;
					if (temp.impact(startPoint)) {
						Point A = rotateAround(temp.getA(), angle);
						Point B = rotateAround(temp.getB(), angle);
						MyOval oval = new MyOval();
						oval.setA(A);
						oval.setB(B);
						oval.setSize(temp.getSize());
						oval.setColor(temp.getColor());
						oval.draw(g);
					}
				} else if (shape_temp instanceof MyEllipse) {
					MyEllipse temp = (MyEllipse) shape_temp;
					if (temp.impact(startPoint)) {
						float a = angle + temp.getAngle();
						if (a > 360) {
							a -= 360;
						}
						drawEllip(g, temp, a);
					}
				} else if (shape_temp instanceof MyRect) {
					MyRect temp = (MyRect) shape_temp;
					if (temp.impact(startPoint)) {
						execute4DiemOfRotate(angle, temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), g);
					}
				} else if (shape_temp instanceof MySquare) {
					MySquare temp = (MySquare) shape_temp;
					if (temp.impact(startPoint)) {
						execute4DiemOfRotate(angle, temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), g);
					}
				} else if (shape_temp instanceof MyParallelogram) {
					MyParallelogram temp = (MyParallelogram) shape_temp;
					if (temp.impact(startPoint)) {
						execute4DiemOfRotate(angle, temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), g);
					}
				} else if (shape_temp instanceof MyTriangleSquare) {
					MyTriangleSquare temp = (MyTriangleSquare) shape_temp;
					if (temp.impact(startPoint)) {
						execute3DiemOfRotate(angle, temp.getA(), temp.getB(), temp.getC(), temp.getColor(),
								temp.getSize(), g);
					}
				} else if (shape_temp instanceof MyTriangleIsosceles) {
					MyTriangleIsosceles temp = (MyTriangleIsosceles) shape_temp;
					if (temp.impact(startPoint)) {
						execute3DiemOfRotate(angle, temp.getA(), temp.getB(), temp.getC(), temp.getColor(),
								temp.getSize(), g);
					}
				} else if (shape_temp instanceof MyRhombus) {
					MyRhombus temp = (MyRhombus) shape_temp;
					if (temp.impact(startPoint)) {
						execute4DiemOfRotate(angle, temp.getA(), temp.getB(), temp.getC(), temp.getD(), temp.getColor(),
								temp.getSize(), g);
					}
				} else if (shape_temp instanceof MyPentagon) {
					MyPentagon temp = (MyPentagon) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executeMultipleOfRotate(arr_Point, angle, temp.getSize(), temp.getColor(), g);

					}
				} else if (shape_temp instanceof MyHexagon) {
					MyHexagon temp = (MyHexagon) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executeMultipleOfRotate(arr_Point, angle, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFourPointStar) {
					MyFourPointStar temp = (MyFourPointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executeMultipleOfRotate(arr_Point, angle, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFivePointStar) {
					MyFivePointStar temp = (MyFivePointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						executeMultipleOfRotate(arr_Point, angle, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFivePointStarLine) {
					MyFivePointStarLine temp = (MyFivePointStarLine) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] p = new Point[5];
						p[0] = new Point(arr_Point[3]);
						p[1] = new Point(arr_Point[0]);
						p[2] = new Point(arr_Point[2]);
						p[3] = new Point(arr_Point[4]);
						p[4] = new Point(arr_Point[1]);
						executeMultipleOfRotate(p, angle, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MySixPointStar) {
					MySixPointStar temp = (MySixPointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr_triangle_1 = new Point[3];
						Point[] arr_triangle_2 = new Point[3];
						arr_triangle_1[0] = new Point(arr_Point[0]);
						arr_triangle_1[1] = new Point(arr_Point[2]);
						arr_triangle_1[2] = new Point(arr_Point[4]);
						arr_triangle_2[0] = new Point(arr_Point[1]);
						arr_triangle_2[1] = new Point(arr_Point[3]);
						arr_triangle_2[2] = new Point(arr_Point[5]);
						executeMultipleOfRotate(arr_triangle_1, angle, temp.getSize(), temp.getColor(), g);
						executeMultipleOfRotate(arr_triangle_2, angle, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFlower) {
					MyFlower temp = (MyFlower) shape_temp;
					if (temp.impact(startPoint)) {
						Point A = rotateAround(temp.getA(), angle);
						Point B = rotateAround(temp.getB(), angle);
						MyFlower flower = new MyFlower();
						flower.setA(A);
						flower.setB(B);
						flower.setSize(temp.getSize());
						flower.setColor(temp.getColor());
						flower.draw(g);
					}
				} else if (shape_temp instanceof MyPinwheel) {
					MyPinwheel temp = (MyPinwheel) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[9];
						for (int i = 0; i < 9; i++) {
							arr[i] = new Point(rotateAround(arr_Point[i], angle));
						}
						if (temp.getSize() > 1) {
							MyLine line;
							line = new MyLine(arr[0], arr[4]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[2], arr[6]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[1], arr[5]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[3], arr[7]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							for (int i = 0; i < 8; i += 2) {
								line = new MyLine(arr[i], arr[i + 1]);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							}
						} else {
							g.setColor(temp.getColor());
							g.drawLine(arr[0].x, arr[0].y, arr[4].x, arr[4].y);
							g.drawLine(arr[2].x, arr[2].y, arr[6].x, arr[6].y);
							g.drawLine(arr[1].x, arr[1].y, arr[5].x, arr[5].y);
							g.drawLine(arr[3].x, arr[3].y, arr[7].x, arr[7].y);
							for (int i = 0; i < 8; i += 2) {
								g.drawLine(arr[i].x, arr[i].y, arr[i + 1].x, arr[i + 1].y);
							}
						}
					}
				} else if (shape_temp instanceof MyMedal) {
					MyMedal temp = (MyMedal) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] point = temp.getPoint();
						Point[] point_top = temp.getPoint_top();
						Point[] point_bottom = temp.getPoint_bottom();

						Point[] arr_Point = new Point[6];
						Point[] arr_PointTop = new Point[6];
						Point[] arr_PointBottom = new Point[5];
						for (int i = 0; i < 5; i++) {
							arr_PointTop[i] = new Point(rotateAround(point_top[i], angle));
							arr_PointBottom[i] = new Point(rotateAround(point_bottom[i], angle));
							arr_Point[i] = new Point(rotateAround(point[i], angle));
						}
						arr_Point[5] = new Point(rotateAround(point[5], angle));
						arr_PointTop[5] = new Point(rotateAround(point_top[5], angle));

						// Vẽ hình

						MyLine line;
						if (size > 1) {
							// Vẽ nửa phần trên
							for (int i = 0; i < 4; i++) {
								line = new MyLine(arr_PointTop[i], arr_PointTop[i + 1]);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							}
							line = new MyLine(arr_PointTop[4], arr_PointTop[0]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr_PointTop[0], arr_PointTop[3]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr_PointTop[5], arr_PointTop[2]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);

							// Vẽ nửa phần dưới
							line = new MyLine(arr_PointBottom[0], arr_PointBottom[2]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[0], arr_PointBottom[3]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[1], arr_PointBottom[3]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[1], arr_PointBottom[4]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[2], arr_PointBottom[4]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);

							MyOval oval = new MyOval(arr_Point[5], arr_Point[3], temp.getSize(), temp.getColor(),
									temp.getAngle());
							oval.draw(g);
						} else {
							g.setColor(temp.getColor());
							// Vẽ nửa phần trên
							for (int i = 0; i < 4; i++) {
								g.drawLine(arr_PointTop[i].x, arr_PointTop[i].y, arr_PointTop[i + 1].x,
										arr_PointTop[i + 1].y);
							}
							g.drawLine(arr_PointTop[4].x, arr_PointTop[4].y, arr_PointTop[0].x, arr_PointTop[0].y);
							g.drawLine(arr_PointTop[0].x, arr_PointTop[0].y, arr_PointTop[3].x, arr_PointTop[3].y);
							g.drawLine(arr_PointTop[5].x, arr_PointTop[5].y, arr_PointTop[2].x, arr_PointTop[2].y);

							// Vẽ nửa phần dưới
							g.drawLine(arr_PointBottom[0].x, arr_PointBottom[0].y, arr_PointBottom[2].x,
									arr_PointBottom[2].y);
							g.drawLine(arr_PointBottom[0].x, arr_PointBottom[0].y, arr_PointBottom[3].x,
									arr_PointBottom[3].y);
							g.drawLine(arr_PointBottom[1].x, arr_PointBottom[1].y, arr_PointBottom[3].x,
									arr_PointBottom[3].y);
							g.drawLine(arr_PointBottom[1].x, arr_PointBottom[1].y, arr_PointBottom[4].x,
									arr_PointBottom[4].y);
							g.drawLine(arr_PointBottom[2].x, arr_PointBottom[2].y, arr_PointBottom[4].x,
									arr_PointBottom[4].y);
							MyOval oval = new MyOval(arr_Point[5], arr_Point[3], temp.getSize(), temp.getColor(),
									temp.getAngle());
							oval.draw(g);
						}
					}
				} else if (shape_temp instanceof MyNationalFlag) {
					MyNationalFlag temp = (MyNationalFlag) shape_temp;
					if (temp.impact(startPoint)) {
						MyLine line;
						Point[] point_Flag = temp.getPoint_flag();
						Point[] point_Star = temp.getPoint_star();

						Point[] arr_PointFlag = new Point[5];
						Point[] arr_PointStar = new Point[10];
						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = new Point(rotateAround(point_Flag[i], angle));
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = new Point(rotateAround(point_Star[i], angle));
						}
						// Vẽ hình
						if (size > 1) {
							// Vẽ lá cờ
							for (int i = 0; i < 3; i++) {
								line = new MyLine(arr_PointFlag[i], arr_PointFlag[i + 1]);
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							// Vẽ cây cờ
							line = new MyLine(arr_PointFlag[4], arr_PointFlag[0]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize() * 2);
							line.draw(g);
							// Vẽ ngôi sao
							for (int i = 0; i < 9; i++) {
								line = new MyLine(arr_PointStar[i], arr_PointStar[i + 1]);
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							line = new MyLine(arr_PointStar[9], arr_PointStar[0]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize());
							line.draw(g);
						} else {
							// Vẽ lá cờ
							g.setColor(temp.getColor());
							for (int i = 0; i < 3; i++) {
								g.drawLine(arr_PointFlag[i].x, arr_PointFlag[i].y, arr_PointFlag[i + 1].x,
										arr_PointFlag[i + 1].y);
							}
							// Vẽ cây cờ
							line = new MyLine(arr_PointFlag[0], arr_PointFlag[4]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize() * 2);
							line.draw(g);
							// Vẽ ngôi sao
							g.setColor(temp.getColor());
							for (int i = 0; i < 9; i++) {
								g.drawLine(arr_PointStar[i].x, arr_PointStar[i].y, arr_PointStar[i + 1].x,
										arr_PointStar[i + 1].y);
							}
							g.drawLine(arr_PointStar[9].x, arr_PointStar[9].y, arr_PointStar[0].x, arr_PointStar[0].y);
						}
					}
				}
			} else if ("Scale".equals(selectButton)) {
				int dx = endPoint.x - startPoint.x;
				int dy = endPoint.y - startPoint.y;
				if (shape_temp instanceof MyLine) {
					MyLine temp = (MyLine) shape_temp;
					if (temp.impact(startPoint)) {
						g.setColor(temp.getColor());
						double AP = Math.sqrt(Math.pow((temp.getA().x - startPoint.x), 2)
								+ Math.pow(temp.getA().y - startPoint.y, 2));
						double PB = Math.sqrt(Math.pow((startPoint.x - temp.getB().x), 2)
								+ Math.pow(startPoint.y - temp.getB().y, 2));
						MyLine line;
						if (AP < PB) {
							if (temp.getSize() > 1) {
								line = new MyLine(temp.getB(), endPoint);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							} else {
								g.setColor(temp.getColor());
								g.drawLine(temp.getB().x, temp.getB().y, endPoint.x, endPoint.y);
							}

						} else {
							if (temp.getSize() > 1) {
								line = new MyLine(temp.getA(), endPoint);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							} else {
								g.setColor(temp.getColor());
								g.drawLine(temp.getA().x, temp.getA().y, endPoint.x, endPoint.y);
							}
						}
					}
				} else if (shape_temp instanceof MyOval) {
					MyOval temp = (MyOval) shape_temp;
					if (temp.impact(startPoint)) {
						int delta = (dx > dy) ? dx : dy;
						MyOval oval;
						Point p1 = (rotateAround(temp.getA(), temp.getAngle() * (-1)));
						Point p2 = (rotateAround(temp.getB(), temp.getAngle() * (-1)));
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
						p1 = rotateAround(p1, temp.getAngle());
						p2 = rotateAround(p2, temp.getAngle());
						oval = new MyOval(p1, p2, temp.getSize(), temp.getColor(), temp.getAngle());
						oval.draw(g);
					}
				} else if (shape_temp instanceof MyEllipse) {
					MyEllipse temp = (MyEllipse) shape_temp;
					if (temp.impact(startPoint)) {
						Point p1 = new Point(rotateAround(temp.getA(), temp.getAngle() * (-1)));
						Point p2 = new Point(rotateAround(temp.getB(), temp.getAngle() * (-1)));
						Point p3 = new Point(rotateAround(temp.getC(), temp.getAngle() * (-1)));
						Point p4 = new Point(rotateAround(temp.getD(), temp.getAngle() * (-1)));

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

						p1 = rotateAround(p1, temp.getAngle());
						p2 = rotateAround(p2, temp.getAngle());
						p3 = rotateAround(p3, temp.getAngle());
						p4 = rotateAround(p4, temp.getAngle());
						MyEllipse ellipse = new MyEllipse(p1, p2, p3, p4, temp.getSize(), temp.getColor(),
								temp.getAngle());
						ellipse.draw(g);
					}

				} else if (shape_temp instanceof MyRect) {
					MyRect temp = (MyRect) shape_temp;
					if (temp.impact(startPoint)) {
						execute4DiemOfScale(temp.getAngle(), temp.getA(), temp.getB(), temp.getC(), temp.getD(),
								temp.getSize(), temp.getColor(), dx, dy, g);
					}
				} else if (shape_temp instanceof MySquare) {
					MySquare temp = (MySquare) shape_temp;
					if (temp.impact(startPoint)) {
						// Đối với hình vuông độ lệch lấy theo max của x or y
						int delta = (dx > dy) ? dx : dy;
						execute4DiemOfScale(temp.getAngle(), temp.getA(), temp.getB(), temp.getC(), temp.getD(),
								temp.getSize(), temp.getColor(), delta, delta, g);
					}
				} else if (shape_temp instanceof MyParallelogram) {
					MyParallelogram temp = (MyParallelogram) shape_temp;
					if (temp.impact(startPoint)) {
						execute4DiemOfScale(temp.getAngle(), temp.getA(), temp.getB(), temp.getC(), temp.getD(),
								temp.getSize(), temp.getColor(), dx, dy, g);
					}
				} else if (shape_temp instanceof MyTriangleSquare) {
					MyTriangleSquare temp = (MyTriangleSquare) shape_temp;
					if (temp.impact(startPoint)) {
						Point p1 = new Point(rotateAround(temp.getA(), temp.getAngle() * (-1)));
						Point p2 = new Point(rotateAround(temp.getB(), temp.getAngle() * (-1)));
						Point p3 = new Point(rotateAround(temp.getC(), temp.getAngle() * (-1)));
						p1 = rotateAround(new Point(p1.x - dx, p1.y - dy), temp.getAngle());
						p2 = rotateAround(new Point(p2.x + dx, p2.y + dy), temp.getAngle());
						p3 = rotateAround(new Point(p3.x - dx, p3.y + dy), temp.getAngle());
						executePainting3Diem(p1, p2, p3, temp.getColor(), temp.getSize(), 0, 0, g);
					}
				} else if (shape_temp instanceof MyTriangleIsosceles) {
					MyTriangleIsosceles temp = (MyTriangleIsosceles) shape_temp;
					if (temp.impact(startPoint)) {
						Point p1 = new Point(rotateAround(temp.getA(), temp.getAngle() * (-1)));
						Point p2 = new Point(rotateAround(temp.getB(), temp.getAngle() * (-1)));
						Point p3 = new Point(rotateAround(temp.getC(), temp.getAngle() * (-1)));
						p1 = rotateAround(new Point(p1.x, p1.y - dy), temp.getAngle());
						p2 = rotateAround(new Point(p2.x + dx, p2.y + dy), temp.getAngle());
						p3 = rotateAround(new Point(p3.x - dx, p3.y + dy), temp.getAngle());
						executePainting3Diem(p1, p2, p3, temp.getColor(), temp.getSize(), 0, 0, g);
					}

				} else if (shape_temp instanceof MyRhombus) {
					MyRhombus temp = (MyRhombus) shape_temp;
					if (temp.impact(startPoint)) {
						Point p1 = new Point(rotateAround(temp.getA(), temp.getAngle() * (-1)));
						Point p2 = new Point(rotateAround(temp.getB(), temp.getAngle() * (-1)));
						Point p3 = new Point(rotateAround(temp.getC(), temp.getAngle() * (-1)));
						Point p4 = new Point(rotateAround(temp.getD(), temp.getAngle() * (-1)));

						if (p4.x < p2.x && p1.y < p3.y) {
							p1 = new Point(p1.x, p1.y - dy);
							p2 = new Point(p2.x + dx, p2.y + dy);
							p3 = new Point(p3.x, p3.y + dy + dy + dy);
							p4 = new Point(p4.x - dx, p4.y + dy);
						} else if (p4.x > p2.x && p1.y < p3.y) {
							p1 = new Point(p1.x, p1.y - dy);
							p2 = new Point(p2.x - dx, p2.y + dy);
							p3 = new Point(p3.x, p3.y + dy + dy + dy);
							p4 = new Point(p4.x + dx, p4.y + dy);
						} else if (p4.x < p2.x && p1.y > p3.y) {
							p1 = new Point(p1.x, p1.y + dy + dy + dy);
							p2 = new Point(p2.x + dx, p2.y + dy);
							p3 = new Point(p3.x, p3.y - dy);
							p4 = new Point(p4.x - dx, p4.y + dy);
						} else if (p4.x > p2.x && p1.y > p3.y) {
							p1 = new Point(p1.x, p1.y + dy + dy + dy);
							p2 = new Point(p2.x - dx, p2.y + dy);
							p3 = new Point(p3.x, p3.y - dy);
							p4 = new Point(p4.x + dx, p4.y + dy);
						}

						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						p1 = rotateAround(p1, temp.getAngle());
						p2 = rotateAround(p2, temp.getAngle());
						p3 = rotateAround(p3, temp.getAngle());
						p4 = rotateAround(p4, temp.getAngle());
						executePainting4Diem(p1, p2, p3, p4, temp.getColor(), temp.getSize(), 0, 0, g);
					}
				} else if (shape_temp instanceof MyPentagon) {
					MyPentagon temp = (MyPentagon) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[5];
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(rotateAround(arr_Point[i], temp.getAngle() * (-1)));
						}
						arr[0] = new Point(arr[0].x, arr[0].y - dy);
						arr[1] = new Point(arr[1].x + dx, arr[1].y + dy);
						arr[2] = new Point(arr[2].x + dx, arr[2].y + dy * 3);
						arr[3] = new Point(arr[3].x - dx, arr[3].y + dy * 3);
						arr[4] = new Point(arr[4].x - dx, arr[4].y + dy);
						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						for (int i = 0; i < 5; i++) {
							arr[i] = rotateAround(arr[i], temp.getAngle());
						}
						executePaintingMultiplePoint(arr, 0, 0, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyHexagon) {
					MyHexagon temp = (MyHexagon) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[6];
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(rotateAround(arr_Point[i], temp.getAngle() * (-1)));
						}
						arr[0] = new Point(arr[0].x, arr[0].y - dy * 2);
						arr[1] = new Point(arr[1].x + dx, arr[1].y - dy);
						arr[2] = new Point(arr[2].x + dx, arr[2].y + dy);
						arr[3] = new Point(arr[3].x, arr[3].y + dy * 2);
						arr[4] = new Point(arr[4].x - dx, arr[4].y + dy);
						arr[5] = new Point(arr[5].x - dx, arr[5].y - dy);
						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						for (int i = 0; i < 6; i++) {
							arr[i] = rotateAround(arr[i], temp.getAngle());
						}
						executePaintingMultiplePoint(arr, 0, 0, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFourPointStar) {
					MyFourPointStar temp = (MyFourPointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[8];
						for (int i = 0; i < 8; i++) {
							arr[i] = new Point(rotateAround(arr_Point[i], temp.getAngle() * (-1)));
						}
						arr[0] = new Point(arr[0].x, arr[0].y - dy * 4);
						arr[1] = new Point(arr[1].x + dx, arr[1].y - dy);
						arr[2] = new Point(arr[2].x + dx * 4, arr[2].y);
						arr[3] = new Point(arr[3].x + dx, arr[3].y + dy);
						arr[4] = new Point(arr[4].x, arr[4].y + dy * 4);
						arr[5] = new Point(arr[5].x - dx, arr[5].y + dy);
						arr[6] = new Point(arr[6].x - dx * 4, arr[6].y);
						arr[7] = new Point(arr[7].x - dx, arr[7].y - dy);
						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						for (int i = 0; i < 8; i++) {
							arr[i] = rotateAround(arr[i], temp.getAngle());
						}
						executePaintingMultiplePoint(arr, 0, 0, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFivePointStar) {
					MyFivePointStar temp = (MyFivePointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[10];
						for (int i = 0; i < 10; i++) {
							arr[i] = new Point(rotateAround(arr_Point[i], temp.getAngle() * (-1)));
						}
						arr[0] = new Point(arr[0].x, arr[0].y - dy * 2);
						arr[1] = new Point(arr[1].x + dx, arr[1].y);
						arr[2] = new Point(arr[2].x + dx * 4, arr[2].y);
						arr[3] = new Point(arr[3].x + dx * 3 / 2, arr[3].y + dy);
						arr[4] = new Point(arr[4].x + dx * 5 / 2, arr[4].y + dy * 3);
						arr[5] = new Point(arr[5].x, arr[5].y + dy * 5 / 3);
						arr[6] = new Point(arr[6].x - dx * 5 / 2, arr[6].y + dy * 3);
						arr[7] = new Point(arr[7].x - dx * 3 / 2, arr[7].y + dy);
						arr[8] = new Point(arr[8].x - dx * 4, arr[8].y);
						arr[9] = new Point(arr[9].x - dx, arr[9].y);
						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						for (int i = 0; i < 10; i++) {
							arr[i] = rotateAround(arr[i], temp.getAngle());
						}
						executePaintingMultiplePoint(arr, 0, 0, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MyFivePointStarLine) {
					MyFivePointStarLine temp = (MyFivePointStarLine) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] p = new Point[6];
						p[0] = new Point(rotateAround(arr_Point[3], temp.getAngle() * (-1)));
						p[1] = new Point(rotateAround(arr_Point[0], temp.getAngle() * (-1)));
						p[2] = new Point(rotateAround(arr_Point[2], temp.getAngle() * (-1)));
						p[3] = new Point(rotateAround(arr_Point[4], temp.getAngle() * (-1)));
						p[4] = new Point(rotateAround(arr_Point[1], temp.getAngle() * (-1)));

						p[0] = rotateAround(new Point(p[0].x - dx, p[0].y + dy), temp.getAngle());
						p[1] = rotateAround(new Point(p[1].x, p[1].y - dy), temp.getAngle());
						p[2] = rotateAround(new Point(p[2].x + dx, p[2].y + dy), temp.getAngle());
						p[3] = rotateAround(new Point(p[3].x - dx * 3 / 2, p[3].y - dy / 2), temp.getAngle());
						p[4] = rotateAround(new Point(p[4].x + dx * 3 / 2, p[4].y - dy / 2), temp.getAngle());
						p[5] = p[0];
						executePaintingMultiplePoint(p, 0, 0, temp.getSize(), temp.getColor(), g);
					}
				} else if (shape_temp instanceof MySixPointStar) {
					MySixPointStar temp = (MySixPointStar) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[6];
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(rotateAround(arr_Point[i], temp.getAngle() * (-1)));
						}
						arr[0] = new Point(arr[0].x, arr[0].y - dy * 2);
						arr[1] = new Point(arr[1].x + dx * 2, arr[1].y - dy);
						arr[2] = new Point(arr[2].x + dx * 2, arr[2].y + dy);
						arr[3] = new Point(arr[3].x, arr[3].y + dy * 2);
						arr[4] = new Point(arr[4].x - dx * 2, arr[4].y + dy);
						arr[5] = new Point(arr[5].x - dx * 2, arr[5].y - dy);
						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						for (int i = 0; i < 6; i++) {
							arr[i] = rotateAround(arr[i], temp.getAngle());
						}
						executePainting3Diem(arr[0], arr[2], arr[4], temp.getColor(), temp.getSize(), 0, 0, g);
						executePainting3Diem(arr[1], arr[3], arr[5], temp.getColor(), temp.getSize(), 0, 0, g);
					}
				} else if (shape_temp instanceof MyFlower) {
					MyFlower temp = (MyFlower) shape_temp;
					if (temp.impact(startPoint)) {
						int delta = (dx > dy) ? dx : dy;
						MyFlower flower;
						Point p1 = (rotateAround(temp.getA(), temp.getAngle() * (-1)));
						Point p2 = (rotateAround(temp.getB(), temp.getAngle() * (-1)));
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
						p1 = rotateAround(p1, temp.getAngle());
						p2 = rotateAround(p2, temp.getAngle());
						flower = new MyFlower(p1, p2, temp.getSize(), temp.getColor(), temp.getAngle());
						flower.draw(g);
					}
				} else if (shape_temp instanceof MyPinwheel) {
					MyPinwheel temp = (MyPinwheel) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] arr_Point = temp.getPoint();
						Point[] arr = new Point[9];
						int delta = (dx > dy) ? dx : dy;
						for (int i = 0; i < 9; i++) {
							arr[i] = rotateAround(arr_Point[i], temp.getAngle() * (-1));
						}
						arr[0] = new Point(arr[0].x, arr[0].y - delta * 2);
						arr[1] = new Point(arr[1].x + delta, arr[1].y - delta);
						arr[2] = new Point(arr[2].x + delta * 2, arr[2].y);
						arr[3] = new Point(arr[3].x + delta, arr[3].y + delta);
						arr[4] = new Point(arr[4].x, arr[4].y + delta * 2);
						arr[5] = new Point(arr[5].x - delta, arr[5].y + delta);
						arr[6] = new Point(arr[6].x - delta * 2, arr[6].y);
						arr[7] = new Point(arr[7].x - delta, arr[7].y - delta);
						// Sau khi tính độ xê lệch cho quay tọa độ về cũ
						for (int i = 0; i < 9; i++) {
							arr[i] = rotateAround(arr[i], temp.getAngle());
						}
						if (temp.getSize() > 1) {
							MyLine line;
							line = new MyLine(arr[0], arr[4]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[2], arr[6]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[1], arr[5]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr[3], arr[7]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							for (int i = 0; i < 8; i += 2) {
								line = new MyLine(arr[i], arr[i + 1]);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							}
						} else {
							g.setColor(temp.getColor());
							g.drawLine(arr[0].x, arr[0].y, arr[4].x, arr[4].y);
							g.drawLine(arr[2].x, arr[2].y, arr[6].x, arr[6].y);
							g.drawLine(arr[1].x, arr[1].y, arr[5].x, arr[5].y);
							g.drawLine(arr[3].x, arr[3].y, arr[7].x, arr[7].y);
							for (int i = 0; i < 8; i += 2) {
								g.drawLine(arr[i].x, arr[i].y, arr[i + 1].x, arr[i + 1].y);
							}
						}
					}
				} else if (shape_temp instanceof MyMedal) {
					MyMedal temp = (MyMedal) shape_temp;
					if (temp.impact(startPoint)) {
						Point[] point = temp.getPoint();
						Point[] point_top = temp.getPoint_top();
						Point[] point_bottom = temp.getPoint_bottom();

						Point[] arr_Point = new Point[6];
						Point[] arr_PointTop = new Point[6];
						Point[] arr_PointBottom = new Point[5];
						for (int i = 0; i < 5; i++) {
							arr_PointTop[i] = rotateAround(point_top[i], temp.getAngle() * (-1));
							arr_PointBottom[i] = rotateAround(point_bottom[i], temp.getAngle() * (-1));
							arr_Point[i] = rotateAround(point[i], temp.getAngle() * (-1));
						}
						arr_Point[5] = rotateAround(point[5], temp.getAngle() * (-1));
						arr_PointTop[5] = rotateAround(point_top[5], temp.getAngle() * (-1));

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
						arr_PointBottom[1] = new Point(arr_PointBottom[1].x + delta * 95 / 100,
								arr_PointBottom[1].y + delta * 2 / 3);
						arr_PointBottom[2] = new Point(arr_PointBottom[2].x + delta * 2 / 3,
								arr_PointBottom[2].y + delta * 175 / 100);
						arr_PointBottom[3] = new Point(arr_PointBottom[3].x - delta * 2 / 3,
								arr_PointBottom[3].y + delta * 175 / 100);
						arr_PointBottom[4] = new Point(arr_PointBottom[4].x - delta * 95 / 100,
								arr_PointBottom[4].y + delta * 2 / 3);

						for (int i = 0; i < 5; i++) {
							arr_PointTop[i] = rotateAround(arr_PointTop[i], temp.getAngle());
							arr_PointBottom[i] = rotateAround(arr_PointBottom[i], temp.getAngle());
							arr_Point[i] = rotateAround(arr_Point[i], temp.getAngle());
						}
						arr_Point[5] = rotateAround(arr_Point[5], temp.getAngle());
						arr_PointTop[5] = rotateAround(arr_PointTop[5], temp.getAngle());

						// Vẽ hình
						MyLine line;
						if (size > 1) {
							// Vẽ nửa phần trên
							for (int i = 0; i < 4; i++) {
								line = new MyLine(arr_PointTop[i], arr_PointTop[i + 1]);
								line.setSize(temp.getSize());
								line.setColor(temp.getColor());
								line.draw(g);
							}
							line = new MyLine(arr_PointTop[4], arr_PointTop[0]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr_PointTop[0], arr_PointTop[3]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);
							line = new MyLine(arr_PointTop[5], arr_PointTop[2]);
							line.setSize(temp.getSize());
							line.setColor(temp.getColor());
							line.draw(g);

							// Vẽ nửa phần dưới
							line = new MyLine(arr_PointBottom[0], arr_PointBottom[2]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[0], arr_PointBottom[3]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[1], arr_PointBottom[3]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[1], arr_PointBottom[4]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);
							line = new MyLine(arr_PointBottom[2], arr_PointBottom[4]);
							line.setSize(size);
							line.setColor(color);
							line.draw(g);

							MyOval oval = new MyOval(arr_Point[5], arr_Point[3], temp.getSize(), temp.getColor(),
									temp.getAngle());
							oval.draw(g);
						} else {
							g.setColor(temp.getColor());
							// Vẽ nửa phần trên
							for (int i = 0; i < 4; i++) {
								g.drawLine(arr_PointTop[i].x, arr_PointTop[i].y, arr_PointTop[i + 1].x,
										arr_PointTop[i + 1].y);
							}
							g.drawLine(arr_PointTop[4].x, arr_PointTop[4].y, arr_PointTop[0].x, arr_PointTop[0].y);
							g.drawLine(arr_PointTop[0].x, arr_PointTop[0].y, arr_PointTop[3].x, arr_PointTop[3].y);
							g.drawLine(arr_PointTop[5].x, arr_PointTop[5].y, arr_PointTop[2].x, arr_PointTop[2].y);

							// // Vẽ nửa phần dưới
							g.drawLine(arr_PointBottom[0].x, arr_PointBottom[0].y, arr_PointBottom[2].x,
									arr_PointBottom[2].y);
							g.drawLine(arr_PointBottom[0].x, arr_PointBottom[0].y, arr_PointBottom[3].x,
									arr_PointBottom[3].y);
							g.drawLine(arr_PointBottom[1].x, arr_PointBottom[1].y, arr_PointBottom[3].x,
									arr_PointBottom[3].y);
							g.drawLine(arr_PointBottom[1].x, arr_PointBottom[1].y, arr_PointBottom[4].x,
									arr_PointBottom[4].y);
							g.drawLine(arr_PointBottom[2].x, arr_PointBottom[2].y, arr_PointBottom[4].x,
									arr_PointBottom[4].y);
							MyOval oval = new MyOval(arr_Point[5], arr_Point[3], temp.getSize(), temp.getColor(),
									temp.getAngle());
							oval.draw(g);
						}
					}
				} else if (shape_temp instanceof MyNationalFlag) {
					MyNationalFlag temp = (MyNationalFlag) shape_temp;
					if (temp.impact(startPoint)) {
						MyLine line;
						Point[] point_Flag = temp.getPoint_flag();
						Point[] point_Star = temp.getPoint_star();

						Point[] arr_PointFlag = new Point[5];
						Point[] arr_PointStar = new Point[10];
						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = new Point(rotateAround(point_Flag[i], temp.getAngle() * (-1)));
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = new Point(rotateAround(point_Star[i], temp.getAngle() * (-1)));
						}
						int delta = (dx > dy) ? dx : dy;

						arr_PointFlag[0] = new Point(arr_PointFlag[0].x - delta * 3 / 2, arr_PointFlag[0].y - delta);
						arr_PointFlag[1] = new Point(arr_PointFlag[1].x + delta * 3 / 2, arr_PointFlag[1].y - delta);
						arr_PointFlag[2] = new Point(arr_PointFlag[2].x + delta * 3 / 2, arr_PointFlag[2].y + delta);
						arr_PointFlag[3] = new Point(arr_PointFlag[3].x - delta * 3 / 2, arr_PointFlag[3].y + delta);
						arr_PointFlag[4] = new Point(arr_PointFlag[4].x - delta * 3 / 2,
								arr_PointFlag[4].y + delta * 3);

						arr_PointStar[0] = new Point(arr_PointStar[0].x, arr_PointStar[0].y - delta * 7 / 10);
						arr_PointStar[1] = new Point(arr_PointStar[1].x + delta * 2 / 10,
								arr_PointStar[1].y - delta * 3 / 10);
						arr_PointStar[2] = new Point(arr_PointStar[2].x + delta * 75 / 100,
								arr_PointStar[2].y - delta * 3 / 10);
						arr_PointStar[3] = new Point(arr_PointStar[3].x + delta * 35 / 100,
								arr_PointStar[3].y + delta / 15);
						arr_PointStar[4] = new Point(arr_PointStar[4].x + delta * 5 / 10,
								arr_PointStar[4].y + delta * 6 / 10);
						arr_PointStar[5] = new Point(arr_PointStar[5].x, arr_PointStar[5].y + delta * 30 / 100);
						arr_PointStar[6] = new Point(arr_PointStar[6].x - delta * 5 / 10,
								arr_PointStar[6].y + delta * 6 / 10);
						arr_PointStar[7] = new Point(arr_PointStar[7].x - delta * 35 / 100,
								arr_PointStar[7].y + delta / 15);
						arr_PointStar[8] = new Point(arr_PointStar[8].x - delta * 75 / 100,
								arr_PointStar[8].y - delta * 3 / 10);
						arr_PointStar[9] = new Point(arr_PointStar[9].x - delta * 2 / 10,
								arr_PointStar[9].y - delta * 3 / 10);

						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = rotateAround(arr_PointFlag[i], temp.getAngle());
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = rotateAround(arr_PointStar[i], temp.getAngle());
						}

						// Vẽ hình
						if (size > 1) {
							// Vẽ lá cờ
							for (int i = 0; i < 3; i++) {
								line = new MyLine(arr_PointFlag[i], arr_PointFlag[i + 1]);
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							// Vẽ cây cờ
							line = new MyLine(arr_PointFlag[4], arr_PointFlag[0]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize() * 2);
							line.draw(g);
							// Vẽ ngôi sao
							for (int i = 0; i < 9; i++) {
								line = new MyLine(arr_PointStar[i], arr_PointStar[i + 1]);
								line.setColor(temp.getColor());
								line.setSize(temp.getSize());
								line.draw(g);
							}
							line = new MyLine(arr_PointStar[9], arr_PointStar[0]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize());
							line.draw(g);
						} else {
							// Vẽ lá cờ
							g.setColor(temp.getColor());
							for (int i = 0; i < 3; i++) {
								g.drawLine(arr_PointFlag[i].x, arr_PointFlag[i].y, arr_PointFlag[i + 1].x,
										arr_PointFlag[i + 1].y);
							}
							// Vẽ cây cờ
							line = new MyLine(arr_PointFlag[0], arr_PointFlag[4]);
							line.setColor(temp.getColor());
							line.setSize(temp.getSize() * 2);
							line.draw(g);
							// Vẽ ngôi sao
							g.setColor(temp.getColor());
							for (int i = 0; i < 9; i++) {
								g.drawLine(arr_PointStar[i].x, arr_PointStar[i].y, arr_PointStar[i + 1].x,
										arr_PointStar[i + 1].y);
							}
							g.drawLine(arr_PointStar[9].x, arr_PointStar[9].y, arr_PointStar[0].x, arr_PointStar[0].y);
						}
					}
				}
			} else if ("Ox Projection".equals(selectButton) || "Oy Projection".equals(selectButton)
					|| "Oxy Projection".equals(selectButton)) {
				if (shape_temp instanceof MyLine) {
					MyLine temp = (MyLine) shape_temp;
					MyLine line = new MyLine();
					if ("Ox Projection".equals(selectButton)) {
						line.setA(new Point(convertX(temp.getA())));
						line.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						line.setA(new Point(convertY(temp.getA())));
						line.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						line.setA(new Point(convertXY(temp.getA())));
						line.setB(new Point(convertXY(temp.getB())));
					}
					line.setAngle(360 - temp.getAngle());
					line.setSize(temp.getSize());
					line.setColor(temp.getColor());
					line.draw(g);
					temp_Projection = line;
				} else if (shape_temp instanceof MyOval) {
					MyOval temp = (MyOval) shape_temp;
					MyOval oval = new MyOval();
					if ("Ox Projection".equals(selectButton)) {
						oval.setA(new Point(convertX(temp.getA())));
						oval.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						oval.setA(new Point(convertY(temp.getA())));
						oval.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						oval.setA(new Point(convertXY(temp.getA())));
						oval.setB(new Point(convertXY(temp.getB())));
					}
					oval.setAngle(360 - temp.getAngle());
					oval.setSize(temp.getSize());
					oval.setColor(temp.getColor());
					oval.draw(g);
					temp_Projection = oval;
				} else if (shape_temp instanceof MyEllipse) {
					MyEllipse temp = (MyEllipse) shape_temp;
					MyEllipse ellipse = new MyEllipse();
					if ("Ox Projection".equals(selectButton)) {
						ellipse.setA(new Point(convertX(temp.getA())));
						ellipse.setB(new Point(convertX(temp.getB())));
						ellipse.setC(new Point(convertX(temp.getC())));
						ellipse.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						ellipse.setA(new Point(convertY(temp.getA())));
						ellipse.setB(new Point(convertY(temp.getB())));
						ellipse.setC(new Point(convertY(temp.getC())));
						ellipse.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						ellipse.setA(new Point(convertXY(temp.getA())));
						ellipse.setB(new Point(convertXY(temp.getB())));
						ellipse.setC(new Point(convertXY(temp.getC())));
						ellipse.setD(new Point(convertXY(temp.getD())));
					}
					ellipse.setAngle(360 - temp.getAngle());
					ellipse.setSize(temp.getSize());
					ellipse.setColor(temp.getColor());
					ellipse.draw(g);
					temp_Projection = ellipse;
				} else if (shape_temp instanceof MyRect) {
					MyRect temp = (MyRect) shape_temp;
					MyRect rectangle = new MyRect();
					if ("Ox Projection".equals(selectButton)) {
						rectangle.setA(new Point(convertX(temp.getA())));
						rectangle.setB(new Point(convertX(temp.getB())));
						rectangle.setC(new Point(convertX(temp.getC())));
						rectangle.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						rectangle.setA(new Point(convertY(temp.getA())));
						rectangle.setB(new Point(convertY(temp.getB())));
						rectangle.setC(new Point(convertY(temp.getC())));
						rectangle.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						rectangle.setA(new Point(convertXY(temp.getA())));
						rectangle.setB(new Point(convertXY(temp.getB())));
						rectangle.setC(new Point(convertXY(temp.getC())));
						rectangle.setD(new Point(convertXY(temp.getD())));
					}
					rectangle.setAngle(360 - temp.getAngle());
					rectangle.setSize(temp.getSize());
					rectangle.setColor(temp.getColor());
					rectangle.draw(g);
					temp_Projection = rectangle;
				} else if (shape_temp instanceof MySquare) {
					MySquare temp = (MySquare) shape_temp;
					MySquare square = new MySquare();
					if ("Ox Projection".equals(selectButton)) {
						square.setA(new Point(convertX(temp.getA())));
						square.setB(new Point(convertX(temp.getB())));
						square.setC(new Point(convertX(temp.getC())));
						square.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						square.setA(new Point(convertY(temp.getA())));
						square.setB(new Point(convertY(temp.getB())));
						square.setC(new Point(convertY(temp.getC())));
						square.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						square.setA(new Point(convertXY(temp.getA())));
						square.setB(new Point(convertXY(temp.getB())));
						square.setC(new Point(convertXY(temp.getC())));
						square.setD(new Point(convertXY(temp.getD())));
					}
					square.setAngle(360 - temp.getAngle());
					square.setSize(temp.getSize());
					square.setColor(temp.getColor());
					square.draw(g);
					temp_Projection = square;
				} else if (shape_temp instanceof MyParallelogram) {
					MyParallelogram temp = (MyParallelogram) shape_temp;
					MyParallelogram parallelo = new MyParallelogram();
					if ("Ox Projection".equals(selectButton)) {
						parallelo.setA(new Point(convertX(temp.getA())));
						parallelo.setB(new Point(convertX(temp.getB())));
						parallelo.setC(new Point(convertX(temp.getC())));
						parallelo.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						parallelo.setA(new Point(convertY(temp.getA())));
						parallelo.setB(new Point(convertY(temp.getB())));
						parallelo.setC(new Point(convertY(temp.getC())));
						parallelo.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						parallelo.setA(new Point(convertXY(temp.getA())));
						parallelo.setB(new Point(convertXY(temp.getB())));
						parallelo.setC(new Point(convertXY(temp.getC())));
						parallelo.setD(new Point(convertXY(temp.getD())));
					}
					parallelo.setAngle(360 - temp.getAngle());
					parallelo.setSize(temp.getSize());
					parallelo.setColor(temp.getColor());
					parallelo.draw(g);
					temp_Projection = parallelo;
				} else if (shape_temp instanceof MyTriangleSquare) {
					MyTriangleSquare temp = (MyTriangleSquare) shape_temp;
					MyTriangleSquare triSquare = new MyTriangleSquare();
					if ("Ox Projection".equals(selectButton)) {
						triSquare.setA(new Point(convertX(temp.getA())));
						triSquare.setB(new Point(convertX(temp.getB())));
						triSquare.setC(new Point(convertX(temp.getC())));
					} else if ("Oy Projection".equals(selectButton)) {
						triSquare.setA(new Point(convertY(temp.getA())));
						triSquare.setB(new Point(convertY(temp.getB())));
						triSquare.setC(new Point(convertY(temp.getC())));
					} else if ("Oxy Projection".equals(selectButton)) {
						triSquare.setA(new Point(convertXY(temp.getA())));
						triSquare.setB(new Point(convertXY(temp.getB())));
						triSquare.setC(new Point(convertXY(temp.getC())));
					}
					triSquare.setAngle(360 - temp.getAngle());
					triSquare.setSize(temp.getSize());
					triSquare.setColor(temp.getColor());
					triSquare.draw(g);
					temp_Projection = triSquare;
				} else if (shape_temp instanceof MyTriangleIsosceles) {
					MyTriangleIsosceles temp = (MyTriangleIsosceles) shape_temp;
					MyTriangleIsosceles triIsosceles = new MyTriangleIsosceles();
					if ("Ox Projection".equals(selectButton)) {
						triIsosceles.setA(new Point(convertX(temp.getA())));
						triIsosceles.setB(new Point(convertX(temp.getB())));
						triIsosceles.setC(new Point(convertX(temp.getC())));
					} else if ("Oy Projection".equals(selectButton)) {
						triIsosceles.setA(new Point(convertY(temp.getA())));
						triIsosceles.setB(new Point(convertY(temp.getB())));
						triIsosceles.setC(new Point(convertY(temp.getC())));
					} else if ("Oxy Projection".equals(selectButton)) {
						triIsosceles.setA(new Point(convertXY(temp.getA())));
						triIsosceles.setB(new Point(convertXY(temp.getB())));
						triIsosceles.setC(new Point(convertXY(temp.getC())));
					}
					triIsosceles.setAngle(360 - temp.getAngle());
					triIsosceles.setSize(temp.getSize());
					triIsosceles.setColor(temp.getColor());
					triIsosceles.draw(g);
					temp_Projection = triIsosceles;
				} else if (shape_temp instanceof MyRhombus) {
					MyRhombus temp = (MyRhombus) shape_temp;
					MyRhombus rhombus = new MyRhombus();
					if ("Ox Projection".equals(selectButton)) {
						rhombus.setA(new Point(convertX(temp.getA())));
						rhombus.setB(new Point(convertX(temp.getB())));
						rhombus.setC(new Point(convertX(temp.getC())));
						rhombus.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						rhombus.setA(new Point(convertY(temp.getA())));
						rhombus.setB(new Point(convertY(temp.getB())));
						rhombus.setC(new Point(convertY(temp.getC())));
						rhombus.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						rhombus.setA(new Point(convertXY(temp.getA())));
						rhombus.setB(new Point(convertXY(temp.getB())));
						rhombus.setC(new Point(convertXY(temp.getC())));
						rhombus.setD(new Point(convertXY(temp.getD())));
					}
					rhombus.setAngle(360 - temp.getAngle());
					rhombus.setSize(temp.getSize());
					rhombus.setColor(temp.getColor());
					rhombus.draw(g);
					temp_Projection = rhombus;
				} else if (shape_temp instanceof MyPentagon) {
					MyPentagon temp = (MyPentagon) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[5];
					MyPentagon pentagon = new MyPentagon();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						pentagon.setA(new Point(convertX(temp.getA())));
						pentagon.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						pentagon.setA(new Point(convertY(temp.getA())));
						pentagon.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						pentagon.setA(new Point(convertXY(temp.getA())));
						pentagon.setB(new Point(convertXY(temp.getB())));
					}
					pentagon.setPoint(arr);
					pentagon.setAngle(360 - temp.getAngle());
					pentagon.setSize(temp.getSize());
					pentagon.setColor(temp.getColor());
					pentagon.draw(g);
					temp_Projection = pentagon;
				} else if (shape_temp instanceof MyHexagon) {
					MyHexagon temp = (MyHexagon) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[6];
					MyHexagon hexagon = new MyHexagon();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						hexagon.setA(new Point(convertX(temp.getA())));
						hexagon.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						hexagon.setA(new Point(convertY(temp.getA())));
						hexagon.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						hexagon.setA(new Point(convertXY(temp.getA())));
						hexagon.setB(new Point(convertXY(temp.getB())));
					}
					hexagon.setPoint(arr);
					hexagon.setAngle(360 - temp.getAngle());
					hexagon.setSize(temp.getSize());
					hexagon.setColor(temp.getColor());
					hexagon.draw(g);
					temp_Projection = hexagon;
				} else if (shape_temp instanceof MyFourPointStar) {
					MyFourPointStar temp = (MyFourPointStar) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[8];
					MyFourPointStar fourStart = new MyFourPointStar();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 8; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						fourStart.setA(new Point(convertX(temp.getA())));
						fourStart.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 8; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						fourStart.setA(new Point(convertY(temp.getA())));
						fourStart.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 8; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						fourStart.setA(new Point(convertXY(temp.getA())));
						fourStart.setB(new Point(convertXY(temp.getB())));
					}
					fourStart.setPoint(arr);
					fourStart.setAngle(360 - temp.getAngle());
					fourStart.setSize(temp.getSize());
					fourStart.setColor(temp.getColor());
					fourStart.draw(g);
					temp_Projection = fourStart;
				} else if (shape_temp instanceof MyFivePointStar) {
					MyFivePointStar temp = (MyFivePointStar) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[10];
					MyFivePointStar fiveStart = new MyFivePointStar();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 10; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						fiveStart.setA(new Point(convertX(temp.getA())));
						fiveStart.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 10; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						fiveStart.setA(new Point(convertY(temp.getA())));
						fiveStart.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 10; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						fiveStart.setA(new Point(convertXY(temp.getA())));
						fiveStart.setB(new Point(convertXY(temp.getB())));
					}
					fiveStart.setPoint(arr);
					fiveStart.setAngle(360 - temp.getAngle());
					fiveStart.setSize(temp.getSize());
					fiveStart.setColor(temp.getColor());
					fiveStart.draw(g);
					temp_Projection = fiveStart;
				} else if (shape_temp instanceof MyFivePointStarLine) {
					MyFivePointStarLine temp = (MyFivePointStarLine) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[5];
					MyFivePointStarLine fiveStartLine = new MyFivePointStarLine();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						fiveStartLine.setA(new Point(convertX(temp.getA())));
						fiveStartLine.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						fiveStartLine.setA(new Point(convertY(temp.getA())));
						fiveStartLine.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						fiveStartLine.setA(new Point(convertXY(temp.getA())));
						fiveStartLine.setB(new Point(convertXY(temp.getB())));
					}
					fiveStartLine.setPoint(arr);
					fiveStartLine.setAngle(360 - temp.getAngle());
					fiveStartLine.setSize(temp.getSize());
					fiveStartLine.setColor(temp.getColor());
					fiveStartLine.draw(g);
					temp_Projection = fiveStartLine;
				} else if (shape_temp instanceof MySixPointStar) {
					MySixPointStar temp = (MySixPointStar) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[6];
					MySixPointStar sixStartLine = new MySixPointStar();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						sixStartLine.setA(new Point(convertX(temp.getA())));
						sixStartLine.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						sixStartLine.setA(new Point(convertY(temp.getA())));
						sixStartLine.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 6; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						sixStartLine.setA(new Point(convertXY(temp.getA())));
						sixStartLine.setB(new Point(convertXY(temp.getB())));
					}
					sixStartLine.setPoint(arr);
					sixStartLine.setAngle(360 - temp.getAngle());
					sixStartLine.setSize(temp.getSize());
					sixStartLine.setColor(temp.getColor());
					sixStartLine.draw(g);
					temp_Projection = sixStartLine;
				} else if (shape_temp instanceof MyFlower) {
					MyFlower temp = (MyFlower) shape_temp;
					MyFlower flower = new MyFlower();
					if ("Ox Projection".equals(selectButton)) {
						flower.setA(new Point(convertX(temp.getA())));
						flower.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						flower.setA(new Point(convertY(temp.getA())));
						flower.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						flower.setA(new Point(convertXY(temp.getA())));
						flower.setB(new Point(convertXY(temp.getB())));
					}
					flower.setAngle(360 - temp.getAngle());
					flower.setSize(temp.getSize());
					flower.setColor(temp.getColor());
					flower.draw(g);
					temp_Projection = flower;
				} else if (shape_temp instanceof MyPinwheel) {
					MyPinwheel temp = (MyPinwheel) shape_temp;
					Point[] arr_Point = temp.getPoint();
					Point[] arr = new Point[9];
					MyPinwheel pinwheel = new MyPinwheel();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 9; i++) {
							arr[i] = new Point(convertX(arr_Point[i]));
						}
						pinwheel.setA(new Point(convertX(temp.getA())));
						pinwheel.setB(new Point(convertX(temp.getB())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 9; i++) {
							arr[i] = new Point(convertY(arr_Point[i]));
						}
						pinwheel.setA(new Point(convertY(temp.getA())));
						pinwheel.setB(new Point(convertY(temp.getB())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 9; i++) {
							arr[i] = new Point(convertXY(arr_Point[i]));
						}
						pinwheel.setA(new Point(convertXY(temp.getA())));
						pinwheel.setB(new Point(convertXY(temp.getB())));
					}
					pinwheel.setPoint(arr);
					pinwheel.setAngle(360 - temp.getAngle());
					pinwheel.setSize(temp.getSize());
					pinwheel.setColor(temp.getColor());
					pinwheel.draw(g);
					temp_Projection = pinwheel;
				} else if (shape_temp instanceof MyMedal) {
					MyMedal temp = (MyMedal) shape_temp;
					Point[] point = temp.getPoint();
					Point[] point_top = temp.getPoint_top();
					Point[] point_bottom = temp.getPoint_bottom();

					Point[] arr_Point = new Point[6];
					Point[] arr_PointTop = new Point[6];
					Point[] arr_PointBottom = new Point[5];

					MyMedal medal = new MyMedal();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr_Point[i] = new Point(convertX(point[i]));
							arr_PointTop[i] = new Point(convertX(point_top[i]));
							arr_PointBottom[i] = new Point(convertX(point_bottom[i]));
						}
						arr_Point[5] = new Point(convertX(point[5]));
						arr_PointTop[5] = new Point(convertX(point_top[5]));
						medal.setA(new Point(convertX(temp.getA())));
						medal.setB(new Point(convertX(temp.getB())));
						medal.setC(new Point(convertX(temp.getC())));
						medal.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr_Point[i] = new Point(convertY(point[i]));
							arr_PointTop[i] = new Point(convertY(point_top[i]));
							arr_PointBottom[i] = new Point(convertY(point_bottom[i]));
						}
						arr_Point[5] = new Point(convertY(point[5]));
						arr_PointTop[5] = new Point(convertY(point_top[5]));
						medal.setA(new Point(convertY(temp.getA())));
						medal.setB(new Point(convertY(temp.getB())));
						medal.setC(new Point(convertY(temp.getC())));
						medal.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr_Point[i] = new Point(convertXY(point[i]));
							arr_PointTop[i] = new Point(convertXY(point_top[i]));
							arr_PointBottom[i] = new Point(convertXY(point_bottom[i]));
						}
						arr_Point[5] = new Point(convertXY(point[5]));
						arr_PointTop[5] = new Point(convertXY(point_top[5]));
						medal.setA(new Point(convertXY(temp.getA())));
						medal.setB(new Point(convertXY(temp.getB())));
						medal.setC(new Point(convertXY(temp.getC())));
						medal.setD(new Point(convertXY(temp.getD())));
					}
					medal.setPoint(arr_Point);
					medal.setPoint_top(arr_PointTop);
					medal.setPoint_bottom(arr_PointBottom);
					medal.setAngle(360 - temp.getAngle());
					medal.setSize(temp.getSize());
					medal.setColor(temp.getColor());
					medal.draw(g);
					temp_Projection = medal;

				} else if (shape_temp instanceof MyNationalFlag) {
					MyNationalFlag temp = (MyNationalFlag) shape_temp;
					Point[] point_Flag = temp.getPoint_flag();
					Point[] point_Star = temp.getPoint_star();
					Point[] arr_PointFlag = new Point[5];
					Point[] arr_PointStar = new Point[10];

					MyNationalFlag nationFlag = new MyNationalFlag();
					if ("Ox Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = new Point(convertX(point_Flag[i]));
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = new Point(convertX(point_Star[i]));
						}
						nationFlag.setA(new Point(convertX(temp.getA())));
						nationFlag.setB(new Point(convertX(temp.getB())));
						nationFlag.setC(new Point(convertX(temp.getC())));
						nationFlag.setD(new Point(convertX(temp.getD())));
					} else if ("Oy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = new Point(convertY(point_Flag[i]));
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = new Point(convertY(point_Star[i]));
						}
						nationFlag.setA(new Point(convertY(temp.getA())));
						nationFlag.setB(new Point(convertY(temp.getB())));
						nationFlag.setC(new Point(convertY(temp.getC())));
						nationFlag.setD(new Point(convertY(temp.getD())));
					} else if ("Oxy Projection".equals(selectButton)) {
						for (int i = 0; i < 5; i++) {
							arr_PointFlag[i] = new Point(convertXY(point_Flag[i]));
						}
						for (int i = 0; i < 10; i++) {
							arr_PointStar[i] = new Point(convertXY(point_Star[i]));
						}
						nationFlag.setA(new Point(convertXY(temp.getA())));
						nationFlag.setB(new Point(convertXY(temp.getB())));
						nationFlag.setC(new Point(convertXY(temp.getC())));
						nationFlag.setD(new Point(convertXY(temp.getD())));
					}
					nationFlag.setPoint_flag(arr_PointFlag);
					nationFlag.setPoint_star(arr_PointStar);
					nationFlag.setAngle(360 - temp.getAngle());
					nationFlag.setSize(temp.getSize());
					nationFlag.setColor(temp.getColor());
					nationFlag.draw(g);
					temp_Projection = nationFlag;
				}
			} else if ("Information".equals(selectButton)) {
				if (shape_temp instanceof MyLine) {

				} else if (shape_temp instanceof MyOval) {

				} else if (shape_temp instanceof MyEllipse) {

				} else if (shape_temp instanceof MyRect) {

				} else if (shape_temp instanceof MySquare) {

				}
			}
		}

		if (flagBoolean && flag_Point != null) {
			MyLine line = new MyLine();
			line.Net_Dut(g, flag_Point.x, flag_Point.y, flag_Point.x, move_start.y);
			line.Net_Dut(g, flag_Point.x, flag_Point.y, flag_Point.x, move_start.y + drawPanel.getHeight());
			line.Net_Dut(g, flag_Point.x, flag_Point.y, move_start.x, flag_Point.y);
			line.Net_Dut(g, flag_Point.x, flag_Point.y, move_start.x + drawPanel.getWidth(), flag_Point.y);

			Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
			Image image = toolkit.getImage("Image/flag.png");
			g.setColor(Color.darkGray);
			g.fillOval(flag_Point.x - 3, flag_Point.y - 4, 8, 8);
			g.drawImage(image, flag_Point.x - 4, flag_Point.y - 30, this);

			g.setColor(Color.RED);
			g.drawString("O'", flag_Point.x - 15, flag_Point.y + 15);
			g.drawString("X'", drawPanel.getWidth() + move_start.x - 20, flag_Point.y - 7);
			g.drawString("Y'", flag_Point.x + 5, move_start.y + 20);

		}

		if (playBoolean) {
			paint2D.forEach((shape) -> {
				if (shape instanceof MyLine) {
					MyLine line = (MyLine) shape;
					line.setA(rotateAround(line.getA(), line.getAngle() * (-1)));
					line.setB(rotateAround(line.getB(), line.getAngle() * (-1)));
					line.setAngle(line.getAngle() + line.getSpeed());
					line.draw(g);
				} else if (shape instanceof MyOval) {
					MyOval oval = (MyOval) shape;
					oval.setA(rotateAround(oval.getA(), oval.getAngle() * (-1)));
					oval.setB(rotateAround(oval.getB(), oval.getAngle() * (-1)));
					oval.setAngle(oval.getAngle() + oval.getSpeed());
					oval.draw(g);
				} else if (shape instanceof MyEllipse) {
					MyEllipse ellipse = (MyEllipse) shape;
					ellipse.setA(rotateAround(ellipse.getA(), ellipse.getAngle() * (-1)));
					ellipse.setB(rotateAround(ellipse.getB(), ellipse.getAngle() * (-1)));
					ellipse.setC(rotateAround(ellipse.getC(), ellipse.getAngle() * (-1)));
					ellipse.setD(rotateAround(ellipse.getD(), ellipse.getAngle() * (-1)));
					ellipse.setAngle(ellipse.getAngle() + ellipse.getSpeed());
					ellipse.draw(g);
				} else if (shape instanceof MyRect) {
					MyRect rect = (MyRect) shape;
					rect.setA(rotateAround(rect.getA(), rect.getAngle() * (-1)));
					rect.setB(rotateAround(rect.getB(), rect.getAngle() * (-1)));
					rect.setC(rotateAround(rect.getC(), rect.getAngle() * (-1)));
					rect.setD(rotateAround(rect.getD(), rect.getAngle() * (-1)));
					rect.setAngle(rect.getAngle() + rect.getSpeed());
					rect.draw(g);
				} else if (shape instanceof MySquare) {
					MySquare square = (MySquare) shape;
					square.setA(rotateAround(square.getA(), square.getAngle() * (-1)));
					square.setB(rotateAround(square.getB(), square.getAngle() * (-1)));
					square.setC(rotateAround(square.getC(), square.getAngle() * (-1)));
					square.setD(rotateAround(square.getD(), square.getAngle() * (-1)));
					square.setAngle(square.getAngle() + square.getSpeed());
					square.draw(g);
				} else if (shape instanceof MyTriangleSquare) {
					MyTriangleSquare triSquare = (MyTriangleSquare) shape;
					triSquare.setA(rotateAround(triSquare.getA(), triSquare.getAngle() * (-1)));
					triSquare.setB(rotateAround(triSquare.getB(), triSquare.getAngle() * (-1)));
					triSquare.setC(rotateAround(triSquare.getC(), triSquare.getAngle() * (-1)));
					triSquare.setAngle(triSquare.getAngle() + triSquare.getSpeed());
					triSquare.draw(g);

				} else if (shape instanceof MyTriangleIsosceles) {
					MyTriangleIsosceles triIsosceles = (MyTriangleIsosceles) shape;
					triIsosceles.setA(rotateAround(triIsosceles.getA(), triIsosceles.getAngle() * (-1)));
					triIsosceles.setB(rotateAround(triIsosceles.getB(), triIsosceles.getAngle() * (-1)));
					triIsosceles.setC(rotateAround(triIsosceles.getC(), triIsosceles.getAngle() * (-1)));
					triIsosceles.setAngle(triIsosceles.getAngle() + triIsosceles.getSpeed());
					triIsosceles.draw(g);
				} else if (shape instanceof MyParallelogram) {
					MyParallelogram paralle = (MyParallelogram) shape;
					paralle.setA(rotateAround(paralle.getA(), paralle.getAngle() * (-1)));
					paralle.setB(rotateAround(paralle.getB(), paralle.getAngle() * (-1)));
					paralle.setC(rotateAround(paralle.getC(), paralle.getAngle() * (-1)));
					paralle.setD(rotateAround(paralle.getD(), paralle.getAngle() * (-1)));
					paralle.setAngle((paralle.getAngle() + paralle.getSpeed()));
					paralle.draw(g);
				} else if (shape instanceof MyRhombus) {
					MyRhombus rhombus = (MyRhombus) shape;
					rhombus.setA(rotateAround(rhombus.getA(), rhombus.getAngle() * (-1)));
					rhombus.setB(rotateAround(rhombus.getB(), rhombus.getAngle() * (-1)));
					rhombus.setC(rotateAround(rhombus.getC(), rhombus.getAngle() * (-1)));
					rhombus.setD(rotateAround(rhombus.getD(), rhombus.getAngle() * (-1)));
					rhombus.setAngle((rhombus.getAngle() + rhombus.getSpeed()));
					rhombus.draw(g);
				} else if (shape instanceof MyPentagon) {
					MyPentagon pentagon = (MyPentagon) shape;
					Point[] arr_Point = pentagon.getPoint();
					Point[] arr = new Point[5];
					for (int i = 0; i < 5; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], pentagon.getAngle() * (-1)));
					}
					pentagon.setA(rotateAround(pentagon.getA(), pentagon.getAngle() * (-1)));
					pentagon.setB(rotateAround(pentagon.getB(), pentagon.getAngle() * (-1)));
					pentagon.setPoint(arr);
					pentagon.setAngle(pentagon.getAngle() + pentagon.getSpeed());
					pentagon.draw(g);
				} else if (shape instanceof MyHexagon) {
					MyHexagon hexagon = (MyHexagon) shape;
					Point[] arr_Point = hexagon.getPoint();
					Point[] arr = new Point[6];
					for (int i = 0; i < 6; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], hexagon.getAngle() * (-1)));
					}
					hexagon.setA(rotateAround(hexagon.getA(), hexagon.getAngle() * (-1)));
					hexagon.setB(rotateAround(hexagon.getB(), hexagon.getAngle() * (-1)));
					hexagon.setPoint(arr);
					hexagon.setAngle(hexagon.getAngle() + hexagon.getSpeed());
					hexagon.draw(g);
				} else if (shape instanceof MyFourPointStar) {
					MyFourPointStar fourPoint = (MyFourPointStar) shape;
					Point[] arr_Point = fourPoint.getPoint();
					Point[] arr = new Point[8];
					for (int i = 0; i < 8; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], fourPoint.getAngle() * (-1)));
					}
					fourPoint.setA(rotateAround(fourPoint.getA(), fourPoint.getAngle() * (-1)));
					fourPoint.setB(rotateAround(fourPoint.getB(), fourPoint.getAngle() * (-1)));
					fourPoint.setPoint(arr);
					fourPoint.setAngle(fourPoint.getAngle() + fourPoint.getSpeed());
					fourPoint.draw(g);
				} else if (shape instanceof MyFivePointStar) {
					MyFivePointStar fivePoint = (MyFivePointStar) shape;
					Point[] arr_Point = fivePoint.getPoint();
					Point[] arr = new Point[10];
					for (int i = 0; i < 10; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], fivePoint.getAngle() * (-1)));
					}
					fivePoint.setA(rotateAround(fivePoint.getA(), fivePoint.getAngle() * (-1)));
					fivePoint.setB(rotateAround(fivePoint.getB(), fivePoint.getAngle() * (-1)));
					fivePoint.setPoint(arr);
					fivePoint.setAngle(fivePoint.getAngle() + fivePoint.getSpeed());
					fivePoint.draw(g);
				} else if (shape instanceof MyFivePointStarLine) {
					MyFivePointStarLine fivePointLine = (MyFivePointStarLine) shape;
					Point[] arr_Point = fivePointLine.getPoint();
					Point[] arr = new Point[5];
					for (int i = 0; i < 5; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], fivePointLine.getAngle() * (-1)));
					}
					fivePointLine.setA(rotateAround(fivePointLine.getA(), fivePointLine.getAngle() * (-1)));
					fivePointLine.setB(rotateAround(fivePointLine.getB(), fivePointLine.getAngle() * (-1)));
					fivePointLine.setPoint(arr);
					fivePointLine.setAngle(fivePointLine.getAngle() + fivePointLine.getSpeed());
					fivePointLine.draw(g);
				} else if (shape instanceof MySixPointStar) {
					MySixPointStar sixPoint = (MySixPointStar) shape;
					Point[] arr_Point = sixPoint.getPoint();
					Point[] arr = new Point[6];
					for (int i = 0; i < 6; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], sixPoint.getAngle() * (-1)));
					}
					sixPoint.setA(rotateAround(sixPoint.getA(), sixPoint.getAngle() * (-1)));
					sixPoint.setB(rotateAround(sixPoint.getB(), sixPoint.getAngle() * (-1)));
					sixPoint.setPoint(arr);
					sixPoint.setAngle(sixPoint.getAngle() + sixPoint.getSpeed());
					sixPoint.draw(g);
				} else if (shape instanceof MyFlower) {
					MyFlower flower = (MyFlower) shape;
					flower.setA(rotateAround(flower.getA(), flower.getAngle() * (-1)));
					flower.setB(rotateAround(flower.getB(), flower.getAngle() * (-1)));
					flower.setO(rotateAround(flower.getO(), flower.getAngle() * (-1)));
					flower.setAngle(flower.getAngle() + flower.getSpeed());
					flower.draw(g);
				} else if (shape instanceof MyPinwheel) {
					MyPinwheel pinwheel = (MyPinwheel) shape;
					Point[] arr_Point = pinwheel.getPoint();
					Point[] arr = new Point[9];
					for (int i = 0; i < 9; i++) {
						arr[i] = new Point(rotateAround(arr_Point[i], pinwheel.getAngle() * (-1)));
					}
					pinwheel.setA(rotateAround(pinwheel.getA(), pinwheel.getAngle() * (-1)));
					pinwheel.setB(rotateAround(pinwheel.getB(), pinwheel.getAngle() * (-1)));
					pinwheel.setPoint(arr);
					pinwheel.setAngle(pinwheel.getAngle() + pinwheel.getSpeed());
					pinwheel.draw(g);
				} else if (shape instanceof MyMedal) {
					MyMedal medal = (MyMedal) shape;
					Point[] point = medal.getPoint();
					Point[] point_top = medal.getPoint_top();
					Point[] point_bottom = medal.getPoint_bottom();

					Point[] arr_Point = new Point[6];
					Point[] arr_PointTop = new Point[6];
					Point[] arr_PointBottom = new Point[5];

					for (int i = 0; i < 5; i++) {
						arr_Point[i] = new Point(rotateAround(point[i], medal.getAngle() * (-1)));
						arr_PointTop[i] = new Point(rotateAround(point_top[i], medal.getAngle() * (-1)));
						arr_PointBottom[i] = new Point(rotateAround(point_bottom[i], medal.getAngle() * (-1)));
					}
					arr_Point[5] = new Point(rotateAround(point[5], medal.getAngle() * (-1)));
					arr_PointTop[5] = new Point(rotateAround(point_top[5], medal.getAngle() * (-1)));

					medal.setA(rotateAround(medal.getA(), medal.getAngle() * (-1)));
					medal.setB(rotateAround(medal.getB(), medal.getAngle() * (-1)));
					medal.setC(rotateAround(medal.getC(), medal.getAngle() * (-1)));
					medal.setD(rotateAround(medal.getD(), medal.getAngle() * (-1)));
					medal.setPoint(arr_Point);
					medal.setPoint_top(arr_PointTop);
					medal.setPoint_bottom(arr_PointBottom);
					medal.setAngle(medal.getAngle() + medal.getSpeed());
					medal.draw(g);
				} else if (shape instanceof MyNationalFlag) {
					MyNationalFlag nationFlag = (MyNationalFlag) shape;
					Point[] point_flag = nationFlag.getPoint_flag();
					Point[] point_star = nationFlag.getPoint_star();
					Point[] arr_PointFlag = new Point[5];
					Point[] arr_PointStar = new Point[10];
					for (int i = 0; i < 5; i++) {
						arr_PointFlag[i] = new Point(rotateAround(point_flag[i], nationFlag.getAngle() * (-1)));
					}
					for (int i = 0; i < 10; i++) {
						arr_PointStar[i] = new Point(rotateAround(point_star[i], nationFlag.getAngle() * (-1)));
					}
					nationFlag.setA(rotateAround(nationFlag.getA(), nationFlag.getAngle() * (-1)));
					nationFlag.setB(rotateAround(nationFlag.getB(), nationFlag.getAngle() * (-1)));
					nationFlag.setC(rotateAround(nationFlag.getC(), nationFlag.getAngle() * (-1)));
					nationFlag.setD(rotateAround(nationFlag.getD(), nationFlag.getAngle() * (-1)));
					nationFlag.setPoint_flag(arr_PointFlag);
					nationFlag.setPoint_star(arr_PointStar);
					nationFlag.setAngle(nationFlag.getAngle() + nationFlag.getSpeed());
					nationFlag.draw(g);
				}

				try {
					Thread.sleep(sliderSpeed.getMaximum() - speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
			});
		} else {
			paint2D.forEach((shape) -> {
				shape.draw(g);
			});
		}
	}

	/**
	 * Create the frame.
	 */
	public Display() {

		// Nếu user di chuyển JFrame phải cập nhật lại vị trí JFrame so với screenWindow
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				localtionFrame = new Point(getLocation()); // Cập nhật lại tọa độ mới của khung jFrame so với window
			}
		});

		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC KHỞI TẠO KHUNG HÌNH, TITLE VÀ ADD CÁC SỰ KIỆN LISTENNER//////////////
		////////////////////////////////////////////////////////////////////////////////
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 5, 1200, 720);
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		setContentPane(mainPanel);
//		this.setResizable(false);
		this.setTitle("Kỹ thuật đồ họa");
		this.setIconImage(new ImageIcon("Image/icon.png").getImage());

		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC DESIGN CÁC CURSOR ///////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////
		Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		Image flagImage = toolkit.getImage("Image/flag.png");
		this.flag = toolkit.createCustomCursor(flagImage, new Point(4, 30), "");
		Image rotateImage = toolkit.getImage("Image/rotate_cursor.png");
		this.rotate = toolkit.createCustomCursor(rotateImage, new Point(0, 0), "");
		Image scaleImage = toolkit.getImage("Image/scale.png");
		this.scale = toolkit.createCustomCursor(scaleImage, new Point(13, 3), "");
		Image deleteImage = toolkit.getImage("Image/delete_cursor.png");
		this.delete = toolkit.createCustomCursor(deleteImage, new Point(15, 15), "");
		Image defaultImage = toolkit.getImage("Image/cursor.png");
		this.default_cursor = toolkit.createCustomCursor(defaultImage, new Point(3, 0), "");
		Image pencilImage = toolkit.getImage("Image/pencil_cursor.png");
		this.pencil = toolkit.createCustomCursor(pencilImage, new Point(2, 30), "");
		Image fillColorImage = toolkit.getImage("Image/fill_color.png");
		this.fillColor = toolkit.createCustomCursor(fillColorImage, new Point(2, 30), "");

		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC KHỞI TẠO ICON, TOOLTIP, HEIGHT, WIDTH CHO CÁC BUTTON/////////////////
		////////////////////////////////////////////////////////////////////////////////
		btnLine = new JButton(new ImageIcon("Image/line.png"));
		btnLine.setBackground(UIManager.getColor("Button.background"));
		btnLine.setToolTipText("Line");
		btnLine.setBounds(10, 85, 48, 48);
		mainPanel.add(btnLine);

		btnOval = new JButton(new ImageIcon("Image/circle.png"));
		btnOval.setToolTipText("Oval");
		btnOval.setBounds(10, 138, 48, 48);
		mainPanel.add(btnOval);

		btnEllipse = new JButton(new ImageIcon("Image/ellipse.png"));
		btnEllipse.setToolTipText("Ellipse");
		btnEllipse.setBounds(64, 138, 48, 48);
		mainPanel.add(btnEllipse);

		btnRectangle = new JButton(new ImageIcon("Image/rectangle.png"));
		btnRectangle.setToolTipText("Rectangle");
		btnRectangle.setBounds(10, 244, 48, 48);
		mainPanel.add(btnRectangle);

		btnSquare = new JButton(new ImageIcon("Image/square.png"));
		btnSquare.setToolTipText("Square");
		btnSquare.setBounds(64, 244, 48, 48);
		mainPanel.add(btnSquare);

		btnParallelogram = new JButton(new ImageIcon("Image/paralle.png"));
		btnParallelogram.setToolTipText("Parallelogram");
		btnParallelogram.setBounds(10, 297, 48, 48);
		mainPanel.add(btnParallelogram);

		btnTriangleSquare = new JButton(new ImageIcon("Image/triangle_square.png"));
		btnTriangleSquare.setToolTipText("Triangle Square");
		btnTriangleSquare.setBounds(10, 191, 48, 48);
		mainPanel.add(btnTriangleSquare);

		btnTriangleIsosceles = new JButton(new ImageIcon("Image/triangle_isosceles.png"));
		btnTriangleIsosceles.setToolTipText("Triangle Isosceles");
		btnTriangleIsosceles.setBounds(64, 191, 48, 48);
		mainPanel.add(btnTriangleIsosceles);

		btnRhombus = new JButton(new ImageIcon("Image/rhombus.png"));
		btnRhombus.setToolTipText("Rhombus");
		btnRhombus.setBounds(64, 297, 48, 48);
		mainPanel.add(btnRhombus);

		btnPentagon = new JButton(new ImageIcon("Image/pentagon.png"));
		btnPentagon.setToolTipText("Pentagon");
		btnPentagon.setBounds(10, 350, 48, 48);
		mainPanel.add(btnPentagon);

		btnHexagon = new JButton(new ImageIcon("Image/hexagon.png"));
		btnHexagon.setToolTipText("Hexagon");
		btnHexagon.setBounds(64, 350, 48, 48);
		mainPanel.add(btnHexagon);

		btnFourPointStar = new JButton(new ImageIcon("Image/four_star.png"));
		btnFourPointStar.setToolTipText("Four-Point Star");
		btnFourPointStar.setBounds(10, 403, 48, 48);
		mainPanel.add(btnFourPointStar);

		btnFivePointStar = new JButton(new ImageIcon("Image/five_star.png"));
		btnFivePointStar.setToolTipText("Five-Point Star");
		btnFivePointStar.setBounds(64, 403, 48, 48);
		mainPanel.add(btnFivePointStar);

		btnFivePointStarLine = new JButton(new ImageIcon("Image/five_star_line.png"));
		btnFivePointStarLine.setToolTipText("Five-Point Star Line");
		btnFivePointStarLine.setBounds(64, 456, 48, 48);
		mainPanel.add(btnFivePointStarLine);

		btnSixPointStar = new JButton(new ImageIcon("Image/six_star.png"));
		btnSixPointStar.setToolTipText("Six Point Star");
		btnSixPointStar.setBounds(10, 456, 48, 48);
		mainPanel.add(btnSixPointStar);

		btnFlower = new JButton(new ImageIcon("Image/flower.png"));
		btnFlower.setBounds(10, 509, 48, 48);
		mainPanel.add(btnFlower);

		btnPinwheel = new JButton(new ImageIcon("Image/pinwheel.png"));
		btnPinwheel.setBounds(64, 509, 48, 48);
		mainPanel.add(btnPinwheel);

		lbSize = new JLabel("1");
		lbSize.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbSize.setForeground(Color.BLUE);
		lbSize.setBounds(145, 35, 15, 14);
		mainPanel.add(lbSize);

		sliderSize = new JSlider();
		sliderSize.setBackground(new Color(216, 191, 216));
		sliderSize.setToolTipText("Size");
		sliderSize.setValue(0);
		sliderSize.setOrientation(SwingConstants.VERTICAL);
		sliderSize.setBounds(128, 20, 18, 45);
		mainPanel.add(sliderSize);

		btnPencil = new JButton(new ImageIcon("Image/pencil.png"));
		btnPencil.setToolTipText("Pencil");
		btnPencil.setBounds(64, 85, 48, 48);
		mainPanel.add(btnPencil);

		btnColor = new JButton(new ImageIcon("Image/color_board.png"));
		btnColor.setToolTipText("Edit Color");
		btnColor.setBounds(798, 14, 48, 48);
		mainPanel.add(btnColor);

		btnUndo = new JButton(new ImageIcon("Image/undo.png"));
		btnUndo.setBackground(UIManager.getColor("Button.background"));
		btnUndo.setToolTipText("Undo (Ctrl+Z)");
		btnUndo.setBounds(160, 14, 48, 48);
		mainPanel.add(btnUndo);

		btnRedo = new JButton(new ImageIcon("Image/redo.png"));
		btnRedo.setToolTipText("Redo (Ctrl+Y)");
		btnRedo.setBounds(218, 14, 48, 48);
		mainPanel.add(btnRedo);

		btnClear = new JButton(new ImageIcon("Image/recycle_bin.png"));
		btnClear.setToolTipText("Clear");
		btnClear.setBounds(740, 14, 48, 48);
		mainPanel.add(btnClear);

		lblSize = new JLabel("Size");
		lblSize.setForeground(Color.BLUE);
		lblSize.setFont(new Font("VNI-Times", Font.BOLD, 14));
		lblSize.setBounds(125, 4, 32, 14);
		mainPanel.add(lblSize);

		drawPanel = new JPanel();
		drawPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, new Color(0, 0, 0)));
		drawPanel.setToolTipText("");
		drawPanel.setBackground(new Color(255, 255, 255));
		drawPanel.setBounds(120, 70, 1000, 550);
		mainPanel.add(drawPanel);

		btnMove = new JButton(new ImageIcon("Image/move.png"));
		btnMove.setBackground(UIManager.getColor("Button.background"));
		btnMove.setToolTipText("Move");
		btnMove.setBounds(276, 14, 48, 48);
		mainPanel.add(btnMove);

		btnRotate = new JButton(new ImageIcon("Image/rotate.png"));
		btnRotate.setToolTipText("Rotate");
		btnRotate.setBounds(508, 14, 48, 48);
		mainPanel.add(btnRotate);

		btnScale = new JButton(new ImageIcon("Image/resize.png"));
		btnScale.setToolTipText("Resize");
		btnScale.setBounds(392, 14, 48, 48);
		mainPanel.add(btnScale);

		btnDelete = new JButton(new ImageIcon("Image/delete.png"));
		btnDelete.setToolTipText("Delete");
		btnDelete.setBounds(334, 14, 48, 48);
		mainPanel.add(btnDelete);

		btnOxProjection = new JButton(new ImageIcon("Image/ox_reflection.png"));
		btnOxProjection.setToolTipText("Phép chiếu qua Oy");
		btnOxProjection.setBounds(566, 14, 48, 48);
		mainPanel.add(btnOxProjection);

		btnOyProjection = new JButton(new ImageIcon("Image/oy_reflection.png"));
		btnOyProjection.setToolTipText("Phép chiếu qua Ox");
		btnOyProjection.setBounds(624, 14, 48, 48);
		mainPanel.add(btnOyProjection);

		btnOxyProjection = new JButton(new ImageIcon("Image/oxy_reflection.png"));
		btnOxyProjection.setToolTipText("Phép chiếu qua Oxy");
		btnOxyProjection.setBounds(682, 14, 48, 48);
		mainPanel.add(btnOxyProjection);

		btnFlag = new JButton(new ImageIcon("Image/flag.png"));
		btnFlag.setToolTipText("Cờ đánh dấu 1 điểm bất kỳ");
		btnFlag.setBounds(450, 14, 48, 48);
		mainPanel.add(btnFlag);

		lbLogo = new JLabel("");
		lbLogo.setIcon(new ImageIcon("Image/logo.png"));
		lbLogo.setBounds(27, 2, 72, 72);
		mainPanel.add(lbLogo);

		lbMove = new JLabel("");
		lbMove.setForeground(new Color(255, 0, 0));
		lbMove.setIcon(new ImageIcon("Image/move_label.png"));
		lbMove.setFont(new Font("Tahoma", Font.BOLD, 22));
		lbMove.setBounds(125, 628, 180, 32);
		lbMove.setVisible(false);
		mainPanel.add(lbMove);

		lbDrag = new JLabel("");
		lbDrag.setIcon(new ImageIcon("Image/drag_label.png"));
		lbDrag.setFont(new Font("Tahoma", Font.BOLD, 22));
		lbDrag.setForeground(Color.BLUE);
		lbDrag.setBounds(350, 628, 180, 32);
		lbDrag.setVisible(false);
		mainPanel.add(lbDrag);

		lbAngle = new JLabel("");
		lbAngle.setIcon(new ImageIcon("Image/angle.png"));
		lbAngle.setFont(new Font("Tahoma", Font.BOLD, 22));
		lbAngle.setForeground(Color.BLUE);
		lbAngle.setBounds(350, 628, 180, 32);
		lbAngle.setVisible(false);
		mainPanel.add(lbAngle);

		btnPlay = new JButton(new ImageIcon("Image/play.png"));
		btnPlay.setToolTipText("Bật/Tắt chế độ animation");
		btnPlay.setBounds(914, 14, 48, 48);
		mainPanel.add(btnPlay);

		btnConfig = new JButton(new ImageIcon("Image/config.png"));
		btnConfig.setToolTipText("Set Speed cho các hình");
		btnConfig.setBounds(972, 14, 48, 48);
		mainPanel.add(btnConfig);

		btnInformation = new JButton(new ImageIcon("Image/infor.png"));
		btnInformation.setToolTipText("Thông tin chi tiết các hình");
		btnInformation.setBounds(856, 14, 48, 48);
		mainPanel.add(btnInformation);

		sliderSpeed = new JSlider();
		sliderSpeed.setBackground(new Color(51, 102, 204));
		sliderSpeed.setValue(0);
		sliderSpeed.setBounds(1026, 35, 120, 26);
		sliderSpeed.setVisible(false);
		mainPanel.add(sliderSpeed);

		lbSpeed = new JLabel("Speed");
		lbSpeed.setForeground(Color.ORANGE);
		lbSpeed.setFont(new Font("VNI-Times", Font.BOLD, 16));
		lbSpeed.setBounds(1062, 15, 46, 20);
		lbSpeed.setVisible(false);
		mainPanel.add(lbSpeed);

		btnMedal = new JButton(new ImageIcon("Image/medal.png"));
		btnMedal.setToolTipText("Medal");
		btnMedal.setBounds(10, 562, 48, 48);
		mainPanel.add(btnMedal);

		btnNationalFlag = new JButton(new ImageIcon("Image/national_flag.png"));
		btnNationalFlag.setToolTipText("National Flag");
		btnNationalFlag.setBounds(64, 562, 48, 48);
		mainPanel.add(btnNationalFlag);

		lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon("Image/background.jpg"));
		lbBackground.setBounds(0, 0, 1200, 690);
		mainPanel.add(lbBackground);

		// Tính tọa độ bắt đầu và tâm của drawPanel
		// move_start = new Point(drawPanel.getX() + 3, drawPanel.getY() + 26);
		move_start = new Point(drawPanel.getX() + 7, drawPanel.getY() + 30);
		centerAxisPoint = new Point(drawPanel.getWidth() / 2 + move_start.x, drawPanel.getHeight() / 2 + move_start.y);

		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC BẮT SỰ KIỆN CHO CÁC BUTTON KHI ĐƯỢC CLICK////////////////////////////
		////////////////////////////////////////////////////////////////////////////////
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Line";
			}
		});
		btnOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Oval";
			}
		});
		btnEllipse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Ellipse";
			}
		});
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Rectangle";
			}
		});
		btnSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Square";
			}
		});
		btnParallelogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Parallelogram";
			}
		});
		btnTriangleSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "TriangleSquare";
			}
		});
		btnTriangleIsosceles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "TriangleIsosceles";
			}
		});
		btnRhombus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Rhombus";
			}
		});
		btnPentagon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Pentagon";
			}
		});
		btnHexagon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Hexagon";
			}
		});
		btnFourPointStar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "FourPointStar";
			}
		});
		btnFivePointStar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "FivePointStar";
			}
		});
		btnFivePointStarLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "FivePointStarLine";
			}
		});
		btnSixPointStar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "SixPointStar";
			}
		});
		btnPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Pencil";
			}
		});
		btnFlower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Flower";
			}
		});
		btnPinwheel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Pinwheel";
			}
		});
		btnMedal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Medal";
			}
		});
		btnNationalFlag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "NationalFlag";
			}
		});
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Move";
			}
		});
		btnRotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Rotate";
			}
		});
		btnScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Scale";
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Delete";
			}
		});
		btnOxProjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Ox Projection";
			}
		});
		btnOyProjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Oy Projection";
			}
		});
		btnOxyProjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton = "Oxy Projection";
			}
		});
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Config";
			}
		});
		btnInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectButton = "Information";
			}
		});
		btnFlag.addActionListener(new ActionListener() {
			int dem = 0;

			public void actionPerformed(ActionEvent e) {
				selectButton = "Flag";
				if (dem++ % 2 == 0) {
					flagBoolean = true;
				} else {
					flagBoolean = false;
					repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
				}
			}
		});
		btnPlay.addActionListener(new ActionListener() {
			int dem = 0;

			public void actionPerformed(ActionEvent e) {
				selectButton = "Play";
				if (dem++ % 2 == 0) {
					playBoolean = true;
					btnPlay.setIcon(new ImageIcon("Image/pause.png"));
					lbSpeed.setVisible(true);
					sliderSpeed.setVisible(true);
					repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
				} else {
					playBoolean = false;
					btnPlay.setIcon(new ImageIcon("Image/play.png"));
					lbSpeed.setVisible(false);
					sliderSpeed.setVisible(false);
				}
			}
		});
		sliderSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				lbSize.setText(String.valueOf(((sliderSize.getValue() - 1) / 20 + 1)));
			}
		});
		sliderSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				speed = sliderSpeed.getValue();
			}
		});
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color temp_color = JColorChooser.showDialog(null, "Choose a color", color);
				color = (temp_color != null) ? temp_color : color;
			}
		});
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (paint2D.size() != 0) {
					stack.push(paint2D.remove(paint2D.size() - 1));
					repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
					btnRedo.setEnabled(true);
				} else {
					btnUndo.setEnabled(false);
				}
			}
		});
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!stack.isEmpty()) {
					paint2D.add(stack.pop());
					repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
					btnUndo.setEnabled(true);
				} else {
					btnRedo.setEnabled(false);
				}
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!paint2D.isEmpty()) {
					int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all drawings?",
							"Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (x == 0) {
						paint2D.clear();
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
					}
				}
			}
		});

		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC BẮT SỰ KIỆN KHI CÁC BUTTON ĐƯỢC HOVER ///////////////////////////////
		////////////////////////////////////////////////////////////////////////////////
		btnLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnLine.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnLine.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnOval.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnOval.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnOval.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnEllipse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnEllipse.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnEllipse.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnRectangle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnRectangle.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnRectangle.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnSquare.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnSquare.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnSquare.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnParallelogram.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnParallelogram.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnParallelogram.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnTriangleSquare.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnTriangleSquare.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnTriangleSquare.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnTriangleIsosceles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnTriangleIsosceles.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnTriangleIsosceles.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnRhombus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnRhombus.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnRhombus.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnPentagon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnPentagon.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnPentagon.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnHexagon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnHexagon.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnHexagon.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnFourPointStar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnFourPointStar.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnFourPointStar.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnFivePointStar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnFivePointStar.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnFivePointStar.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnFivePointStarLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnFivePointStarLine.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnFivePointStarLine.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnSixPointStar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnSixPointStar.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnSixPointStar.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnFlower.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnFlower.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnFlower.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnPinwheel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnPinwheel.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnPinwheel.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnMedal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnMedal.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnMedal.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnNationalFlag.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(HAND_CURSOR));
				btnNationalFlag.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnNationalFlag.setBackground(UIManager.getColor("Button.background"));
			}
		});
		sliderSize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
			}
		});
		sliderSpeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
			}
		});
		btnPencil.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnPencil.setBackground(new Color(255, 182, 193));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnPencil.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnColor.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnColor.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnUndo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnUndo.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnUndo.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnRedo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnRedo.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnRedo.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnClear.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				btnClear.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnMove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnMove.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(MOVE_CURSOR));
				btnMove.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnRotate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnRotate.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(rotate);
				btnRotate.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnScale.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnScale.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(scale);
				btnScale.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnDelete.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(delete);
				btnDelete.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnOxProjection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnOxProjection.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(W_RESIZE_CURSOR));
				btnOxProjection.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnOyProjection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnOyProjection.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(N_RESIZE_CURSOR));
				btnOyProjection.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnOxyProjection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnOxyProjection.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(SE_RESIZE_CURSOR));
				btnOxyProjection.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnPlay.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(SE_RESIZE_CURSOR));
				btnPlay.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnConfig.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(SE_RESIZE_CURSOR));
				btnConfig.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(HAND_CURSOR));
				btnInformation.setBackground(new Color(204, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(SE_RESIZE_CURSOR));
				btnInformation.setBackground(UIManager.getColor("Button.background"));
			}
		});
		btnFlag.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if (flagBoolean) {
					setCursor(flag);
				} else {
					setCursor(default_cursor);
				}
				btnFlag.setBackground(UIManager.getColor("Button.background"));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(HAND_CURSOR));
				btnFlag.setBackground(new Color(204, 255, 255));
			}
		});
		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC BẮT SỰ KIỆN CHO CÁC TỔ HỢP PHÍM ĐƯỢC NHẤN////////////////////////////
		////////////////////////////////////////////////////////////////////////////////
		btnLine.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					if (startPoint != null && endPoint != null) {
						int length_X = Math.abs(startPoint.x - endPoint.x);
						int length_Y = Math.abs(startPoint.y - endPoint.y);
						if (length_X < length_Y) {
							endPoint = new Point(startPoint.x, endPoint.y);
						} else if (length_X > length_Y) {
							endPoint = new Point(endPoint.x, startPoint.y);
						}
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
					}
				}
			}
		});
		btnOval.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				}
			}
		});
		btnEllipse.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnRectangle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnSquare.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				}
			}
		});
		btnParallelogram.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnTriangleSquare.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnTriangleIsosceles.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnRhombus.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnPentagon.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnHexagon.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnFourPointStar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnFivePointStar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnFivePointStarLine.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnSixPointStar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnFlower.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnPinwheel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnMedal.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnNationalFlag.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		sliderSize.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnPencil.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				}
			}
		});
		btnColor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnUndo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnRedo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnClear.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnMove.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnRotate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnScale.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnDelete.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnFlag.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnOxProjection.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnOyProjection.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnOxyProjection.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnPlay.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnConfig.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		btnInformation.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});
		sliderSpeed.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) { // Bắt sự kiện Ctrl+Z cho Undo
					executeUndo();
				} else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Y)) { // Bắt sự kiện Ctrl+Y cho Redo
					executeRedo();
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					executeShift();
				}
			}
		});

		////////////////////////////////////////////////////////////////////////////////
		// KHU VỰC BẮT SỰ KIỆN CHO CHUỘT ///////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////

		drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				endPoint = new Point(e.getX() + move_start.x, e.getY() + move_start.y);
				size = ((sliderSize.getValue() - 1) / 20) + 1; // kích thước của pixel vẽ hình

				if ("Oval".equals(selectButton) || "Square".equals(selectButton)
						|| "FivePointStarLine".equals(selectButton) || "Flower".equals(selectButton)
						|| "Pinwheel".equals(selectButton) || "Medal".equals(selectButton)
						|| "NationalFlag".equals(selectButton)) {
					lbDrag.setVisible(true);
					int min = Math.abs(Math.min((endPoint.x - startPoint.x), (endPoint.y - startPoint.y)));
					lbDrag.setText(min + " x " + min + "px");
				} else if ("Line".equals(selectButton) || "Ellipse".equals(selectButton)
						|| "TriangleSquare".equals(selectButton) || "TriangleIsosceles".equals(selectButton)
						|| "Rectangle".equals(selectButton) || "Parallelogram".equals(selectButton)
						|| "Rhombus".equals(selectButton) || "FourPointStar".equals(selectButton)
						|| "FivePointStar".equals(selectButton) || "SixPointStar".equals(selectButton)
						|| "Pentagon".equals(selectButton) || "Hexagon".equals(selectButton)) {
					lbDrag.setVisible(true);
					lbDrag.setText(Math.abs((endPoint.x - startPoint.x)) + " x " + Math.abs((endPoint.y - startPoint.y))
							+ "px");
				}

				try {
					Thread.sleep(("Pencil".equals(selectButton) ? 15 : 60));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX() - centerAxisPoint.x + move_start.x;
				int y = e.getY() - centerAxisPoint.y + move_start.y;
				lbMove.setText(x + ", " + y * (-1) + "px");
			}
		});
		drawPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Khi nhấn chuột bắt đầu lấy tọa độ điểm bắt đầu
				startPoint = new Point(e.getX() + move_start.x, e.getY() + move_start.y);
				endPoint = startPoint;
				if ("Pencil".equals(selectButton)) {
					myPencil = new MyPencil();
				} else if ("Move".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							shape_temp = temp;
							paint2D.remove(temp);
							break;
						}
					}
				} else if ("Rotate".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							shape_temp = temp;
							paint2D.remove(temp);
							break;
						}
					}
				} else if ("Scale".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							shape_temp = temp;
							paint2D.remove(temp);
							break;
						}
					}
				} else if ("Delete".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							shape_temp = null;
							stack.push(paint2D.remove(i));
							repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
							break;
						}
					}
				} else if ("Ox Projection".equals(selectButton) || "Oy Projection".equals(selectButton)
						|| "Oxy Projection".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							shape_temp = temp;
							repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
							break;
						}
					}
				} else if ("Flag".equals(selectButton)) {
					if (flagBoolean) {
						flag_Point = new Point(e.getX() + move_start.x, e.getY() + move_start.y);
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
					} else {
						flag_Point = null;
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
					}
				} else if ("Config".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							String str = (JOptionPane.showInputDialog("Input Speed: "));
							float speed = 0;
							if (isNumberic(str) && str != null) {
								speed = Float.parseFloat(str);
							}
							if (temp instanceof MyLine) {
								MyLine line = (MyLine) temp;
								line.setSpeed(speed);
							} else if (temp instanceof MyOval) {
								MyOval oval = (MyOval) temp;
								oval.setSpeed(speed);
							} else if (temp instanceof MyEllipse) {
								MyEllipse ellipse = (MyEllipse) temp;
								ellipse.setSpeed(speed);
							} else if (temp instanceof MyRect) {
								MyRect rect = (MyRect) temp;
								rect.setSpeed(speed);
							} else if (temp instanceof MySquare) {
								MySquare square = (MySquare) temp;
								square.setSpeed(speed);
							} else if (temp instanceof MyTriangleSquare) {
								MyTriangleSquare triSquare = (MyTriangleSquare) temp;
								triSquare.setSpeed(speed);
							} else if (temp instanceof MyTriangleIsosceles) {
								MyTriangleIsosceles triIsosceles = (MyTriangleIsosceles) temp;
								triIsosceles.setSpeed(speed);
							} else if (temp instanceof MyParallelogram) {
								MyParallelogram paralle = (MyParallelogram) temp;
								paralle.setSpeed(speed);
							} else if (temp instanceof MyRhombus) {
								MyRhombus rhombus = (MyRhombus) temp;
								rhombus.setSpeed(speed);
							} else if (temp instanceof MyPentagon) {
								MyPentagon pentagon = (MyPentagon) temp;
								pentagon.setSpeed(speed);
							} else if (temp instanceof MyHexagon) {
								MyHexagon hexagon = (MyHexagon) temp;
								hexagon.setSpeed(speed);
							} else if (temp instanceof MyFourPointStar) {
								MyFourPointStar fourPoint = (MyFourPointStar) temp;
								fourPoint.setSpeed(speed);
							} else if (temp instanceof MyFivePointStar) {
								MyFivePointStar fivePoint = (MyFivePointStar) temp;
								fivePoint.setSpeed(speed);
							} else if (temp instanceof MyFivePointStarLine) {
								MyFivePointStarLine fivePointLine = (MyFivePointStarLine) temp;
								fivePointLine.setSpeed(speed);
							} else if (temp instanceof MySixPointStar) {
								MySixPointStar sixPoint = (MySixPointStar) temp;
								sixPoint.setSpeed(speed);
							} else if (temp instanceof MyFlower) {
								MyFlower flower = (MyFlower) temp;
								flower.setSpeed(speed);
							} else if (temp instanceof MyPinwheel) {
								MyPinwheel pinwheel = (MyPinwheel) temp;
								pinwheel.setSpeed(speed);
							} else if (temp instanceof MyMedal) {
								MyMedal medal = (MyMedal) temp;
								medal.setSpeed(speed);
							} else if (temp instanceof MyNationalFlag) {
								MyNationalFlag nationFlag = (MyNationalFlag) temp;
								nationFlag.setSpeed(speed);
							}
							shape_temp = temp;
							endPoint = null;
							// paint2D.remove(temp);
							break;
						}
					}
				} else if ("Information".equals(selectButton)) {
					int length = paint2D.size();
					for (int i = (length - 1); i >= 0; i--) {
						Shapes2D temp = paint2D.get(i);
						if (temp.impact(startPoint)) {
							if (temp instanceof MyLine) {
								MyLine line = (MyLine) temp;
								String str = "A (" + (realToDeviceX(line.getA().x)) + ","
										+ (realToDeviceY(line.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(line.getB().x)) + "," + (realToDeviceY(line.getB().y)) + ")"
										+ "\nAngle = " + line.getAngle() + " độ" + "\nSpeed = " + line.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Đường thẳng", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyOval) {
								MyOval oval = (MyOval) temp;
								String str = "O (" + (realToDeviceX(oval.getO().x)) + ","
										+ (realToDeviceY(oval.getO().y)) + ")" + "\nR = " + oval.getR() + "\nAngle = "
										+ oval.getAngle() + " độ" + "\nSpeed = " + oval.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình tròn", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyEllipse) {
								MyEllipse elipse = (MyEllipse) temp;
								String str = "O (" + (realToDeviceX(elipse.getO().x)) + ","
										+ (realToDeviceY(elipse.getO().y)) + ")" + "\nR1 = " + elipse.getR1()
										+ "\nR2 = " + elipse.getR2() + "\nAngle = " + elipse.getAngle() + " độ"
										+ "\nSpeed = " + elipse.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình ellipse", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyRect) {
								MyRect rect = (MyRect) temp;
								String str = "A (" + (realToDeviceX(rect.getA().x)) + ","
										+ (realToDeviceY(rect.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(rect.getB().x)) + "," + (realToDeviceY(rect.getB().y)) + ")"
										+ "\nC (" + (realToDeviceX(rect.getC().x)) + ","
										+ (realToDeviceY(rect.getC().y)) + ")" + "\nD ("
										+ (realToDeviceX(rect.getD().x)) + "," + (realToDeviceY(rect.getD().y) + ")")
										+ "\nAngle = " + rect.getAngle() + " độ" + "\nSpeed = " + rect.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình chữ nhật", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MySquare) {
								MySquare square = (MySquare) temp;
								String str = "A (" + (realToDeviceX(square.getA().x)) + ","
										+ (realToDeviceY(square.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(square.getB().x)) + "," + (realToDeviceY(square.getB().y))
										+ ")" + "\nC (" + (realToDeviceX(square.getC().x)) + ","
										+ (realToDeviceY(square.getC().y)) + ")" + "\nD ("
										+ (realToDeviceX(square.getD().x)) + ","
										+ (realToDeviceY(square.getD().y) + ")") + "\nAngle = " + square.getAngle()
										+ " độ" + "\nSpeed = " + square.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình vuông", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyParallelogram) {
								MyParallelogram paralle = (MyParallelogram) temp;
								String str = "A (" + (realToDeviceX(paralle.getA().x)) + ","
										+ (realToDeviceY(paralle.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(paralle.getB().x)) + "," + (realToDeviceY(paralle.getB().y))
										+ ")" + "\nC (" + (realToDeviceX(paralle.getC().x)) + ","
										+ (realToDeviceY(paralle.getC().y)) + ")" + "\nD ("
										+ (realToDeviceX(paralle.getD().x)) + ","
										+ (realToDeviceY(paralle.getD().y) + ")") + "\nAngle = " + paralle.getAngle()
										+ " độ" + "\nSpeed = " + paralle.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình bình hành", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyTriangleIsosceles) {
								MyTriangleIsosceles triIsos = (MyTriangleIsosceles) temp;
								String str = "A (" + (realToDeviceX(triIsos.getA().x)) + ","
										+ (realToDeviceY(triIsos.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(triIsos.getB().x)) + "," + (realToDeviceY(triIsos.getB().y))
										+ ")" + "\nC (" + (realToDeviceX(triIsos.getC().x)) + ","
										+ (realToDeviceY(triIsos.getC().y)) + ")" + "\nAngle = " + triIsos.getAngle()
										+ " độ" + "\nSpeed = " + triIsos.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình tam giác cân", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyTriangleSquare) {
								MyTriangleSquare triSquare = (MyTriangleSquare) temp;
								String str = "A (" + (realToDeviceX(triSquare.getA().x)) + ","
										+ (realToDeviceY(triSquare.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(triSquare.getB().x)) + ","
										+ (realToDeviceY(triSquare.getB().y)) + ")" + "\nC ("
										+ (realToDeviceX(triSquare.getC().x)) + ","
										+ (realToDeviceY(triSquare.getC().y)) + ")" + "\nAngle = "
										+ triSquare.getAngle() + " độ" + "\nSpeed = " + triSquare.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình tam giác vuông",
										JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE,
										new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyRhombus) {
								MyRhombus rhombus = (MyRhombus) temp;
								String str = "A (" + (realToDeviceX(rhombus.getA().x)) + ","
										+ (realToDeviceY(rhombus.getA().y)) + ")" + "\nB ("
										+ (realToDeviceX(rhombus.getB().x)) + "," + (realToDeviceY(rhombus.getB().y))
										+ ")" + "\nC (" + (realToDeviceX(rhombus.getC().x)) + ","
										+ (realToDeviceY(rhombus.getC().y)) + ")" + "\nD ("
										+ (realToDeviceX(rhombus.getD().x)) + ","
										+ (realToDeviceY(rhombus.getD().y) + ")") + "\nAngle = " + rhombus.getAngle()
										+ " độ" + "\nSpeed = " + rhombus.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình thoi", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyPentagon) {
								MyPentagon pentagon = (MyPentagon) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E' };
								Point[] arr = pentagon.getPoint();
								String str = c[0] + " (" + realToDeviceX(arr[0].x) + "," + realToDeviceY(arr[0].y)
										+ ")";
								for (int j = 1; j < 5; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + pentagon.getAngle() + "\nSpeed = " + pentagon.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình ngũ giác", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyHexagon) {
								MyHexagon hexagon = (MyHexagon) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E', 'F' };
								Point[] arr = hexagon.getPoint();
								String str = c[0] + " (" + realToDeviceX(arr[0].x) + "," + realToDeviceY(arr[0].y)
										+ ")";
								for (int j = 1; j < 6; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + hexagon.getAngle() + "\nSpeed = " + hexagon.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình lục giác", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyFourPointStar) {
								MyFourPointStar four = (MyFourPointStar) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
								Point[] arr = four.getPoint();
								String str = c[0] + " (" + realToDeviceX(arr[0].x) + "," + realToDeviceY(arr[0].y)
										+ ")";
								for (int j = 1; j < 8; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + four.getAngle() + "\nSpeed = " + four.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình sao 4 cánh", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyFivePointStar) {
								MyFivePointStar five = (MyFivePointStar) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
								Point[] arr = five.getPoint();
								String str = c[0] + " (" + realToDeviceX(arr[0].x) + "," + realToDeviceY(arr[0].y)
										+ ")";
								for (int j = 1; j < 10; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + five.getAngle() + "\nSpeed = " + five.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình sao 5 cánh", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyFivePointStarLine) {
								MyFivePointStarLine five = (MyFivePointStarLine) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E' };
								Point[] arr = five.getPoint();
								String str = c[0] + " (" + realToDeviceX(arr[0].x) + "," + realToDeviceY(arr[0].y)
										+ ")";
								for (int j = 1; j < 5; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + five.getAngle() + "\nSpeed = " + five.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình sao 5 cánh", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MySixPointStar) {
								MySixPointStar six = (MySixPointStar) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E', 'F' };
								Point[] arr = six.getPoint();
								String str = c[0] + " (" + realToDeviceX(arr[0].x) + "," + realToDeviceY(arr[0].y)
										+ ")";
								for (int j = 1; j < 6; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + six.getAngle() + "\nSpeed = " + six.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình sao 6 cánh", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyFlower) {
								MyFlower flower = (MyFlower) temp;
								String str = "O (" + (realToDeviceX(flower.getO().x)) + ","
										+ (realToDeviceY(flower.getO().y)) + ")" + "\nR = " + flower.getR()
										+ "\nAngle = " + flower.getAngle() + " độ" + "\nSpeed = " + flower.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình bông hoa", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyPinwheel) {
								MyPinwheel pinwheel = (MyPinwheel) temp;
								char[] c = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
								Point[] arr = pinwheel.getPoint();
								String str = "O (" + realToDeviceX(arr[8].x) + "," + realToDeviceY(arr[8].y) + ")";
								for (int j = 0; j < 8; j++) {
									str += "\n" + c[j] + " (" + realToDeviceX(arr[j].x) + "," + realToDeviceY(arr[j].y)
											+ ")";
								}
								str += "\nAngle = " + pinwheel.getAngle() + "\nSpeed = " + pinwheel.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình chong chóng", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyMedal) {
								MyMedal medal = (MyMedal) temp;
								String[] penta = { "pentagon_A", "pentagon_B", "pentagon_C", "pentagon_D",
										"pentagon_E" };
								String[] star = { "star_A", "star_B", "star_C", "star_D", "star_E" };
								Point[] arr_Top = medal.getPoint_top();
								Point[] arr_Bottom = medal.getPoint_bottom();
								String str = penta[0] + " (" + realToDeviceX(arr_Top[0].x) + ","
										+ realToDeviceY(arr_Top[0].y) + ")";
								for (int j = 1; j < 5; j++) {
									str += "\n" + penta[j] + " (" + realToDeviceX(arr_Top[j].x) + ","
											+ realToDeviceY(arr_Top[j].y) + ")";
								}
								str += "\ncircle_O (" + realToDeviceX(medal.getO().x) + ","
										+ realToDeviceY(medal.getO().y) + ")" + "\nR = " + medal.getR();
								for (int j = 0; j < 5; j++) {
									str += "\n" + star[j] + " (" + realToDeviceX(arr_Bottom[j].x) + ","
											+ realToDeviceY(arr_Bottom[j].y) + ")";
								}
								str += "\nAngle = " + medal.getAngle() + "\nSpeed = " + medal.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình huân chương", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							} else if (temp instanceof MyNationalFlag) {
								MyNationalFlag national = (MyNationalFlag) temp;
								String[] flag = { "flag_A", "flag_B", "flag_C", "flag_D", "flag_E" };
								String[] star = { "star_A", "star_B", "star_C", "star_D", "star_E", "star_F", "star_G",
										"star_H", "star_I", "star_J" };
								Point[] arr_flag = national.getPoint_flag();
								Point[] arr_star = national.getPoint_star();

								String str = flag[0] + " (" + realToDeviceX(arr_flag[0].x) + ","
										+ realToDeviceY(arr_flag[0].y) + ")";
								for (int j = 1; j < 5; j++) {
									str += "\n" + flag[j] + " (" + realToDeviceX(arr_flag[j].x) + ","
											+ realToDeviceY(arr_flag[j].y) + ")";
								}
								for (int j = 0; j < 10; j++) {
									str += "\n" + star[j] + " (" + realToDeviceX(arr_star[j].x) + ","
											+ realToDeviceY(arr_star[j].y) + ")";
								}
								str += "\nAngle = " + national.getAngle() + "\nSpeed = " + national.getSpeed();
								JOptionPane.showConfirmDialog(null, str, "Hình lá cờ", JOptionPane.CLOSED_OPTION,
										JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Image/infor.png"));
							}

							break;
						}
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				boolean checkDuplicate = (startPoint != endPoint);
				lbDrag.setVisible(false);
				lbAngle.setVisible(false);
				if ("Line".equals(selectButton) && checkDuplicate) {
					paint2D.add(myLine);
				} else if ("Oval".equals(selectButton) && checkDuplicate) {
					paint2D.add(myOval);
				} else if ("Ellipse".equals(selectButton) && checkDuplicate) {
					paint2D.add(myEllipse);
				} else if ("Rectangle".equals(selectButton) && checkDuplicate) {
					paint2D.add(myRect);
				} else if ("Square".equals(selectButton) && checkDuplicate) {
					paint2D.add(mySquare);
				} else if ("Parallelogram".equals(selectButton) && checkDuplicate) {
					paint2D.add(myParallelogram);
				} else if ("TriangleSquare".equals(selectButton) && checkDuplicate) {
					paint2D.add(myTriangleSquare);
				} else if ("TriangleIsosceles".equals(selectButton) && checkDuplicate) {
					paint2D.add(myTriangleIsosceles);
				} else if ("Rhombus".equals(selectButton) && checkDuplicate) {
					paint2D.add(myRhombus);
				} else if ("Pentagon".equals(selectButton) && checkDuplicate) {
					paint2D.add(myPentagon);
				} else if ("Hexagon".equals(selectButton) && checkDuplicate) {
					paint2D.add(myHexagon);
				} else if ("FourPointStar".equals(selectButton) && checkDuplicate) {
					paint2D.add(myFourPointStar);
				} else if ("FivePointStar".equals(selectButton) && checkDuplicate) {
					paint2D.add(myFivePointStar);
				} else if ("FivePointStarLine".equals(selectButton) && checkDuplicate) {
					paint2D.add(myFivePointStarLine);
				} else if ("SixPointStar".equals(selectButton) && checkDuplicate) {
					paint2D.add(mySixPointStar);
				} else if ("Flower".equals(selectButton) && checkDuplicate) {
					paint2D.add(myFlower);
				} else if ("Pinwheel".equals(selectButton) && checkDuplicate) {
					paint2D.add(myPinwheel);
				} else if ("Medal".equals(selectButton) && checkDuplicate) {
					paint2D.add(myMedal);
				} else if ("NationalFlag".equals(selectButton) && checkDuplicate) {
					paint2D.add(myNationalFlag);
				} else if ("Pencil".equals(selectButton) && checkDuplicate) {
					paint2D.add(myPencil);
				} else if ("Move".equals(selectButton)) {
					if (shape_temp != null && shape_temp.impact(startPoint)) {
						shape_temp.move(startPoint, endPoint);
						paint2D.add(shape_temp);
						// chỗ này repaint lại để vẽ rồi cập nhật mảng Points
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
						shape_temp = null;
					}
				} else if ("Rotate".equals(selectButton)) {
					if (shape_temp != null && shape_temp.impact(startPoint)) {
						shape_temp.rotate(startPoint, endPoint);
						paint2D.add(shape_temp);
						// chỗ này repaint lại để vẽ rồi cập nhật mảng Points
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
						shape_temp = null;
					}
				} else if ("Scale".equals(selectButton)) {
					if (shape_temp != null && shape_temp.impact(startPoint)) {
						shape_temp.scale(startPoint, endPoint);
						paint2D.add(shape_temp);
						// chỗ này repaint lại để vẽ rồi cập nhật mảng Points
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
						shape_temp = null;
					}
				} else if ("Ox Projection".equals(selectButton) || "Oy Projection".equals(selectButton)
						|| "Oxy Projection".equals(selectButton)) {
					if (shape_temp != null && temp_Projection != null && shape_temp.impact(startPoint)) {
						paint2D.add(temp_Projection);
						// chỗ này repaint lại để vẽ rồi cập nhật mảng Points
						repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
						shape_temp = null;
						temp_Projection = null;
					}
				}
				btnUndo.setEnabled(paint2D.isEmpty() ? false : true); // Bật tắt nút Undo
				startPoint = endPoint = null;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if ("Flag".equals(selectButton) && flagBoolean == true) {
					setCursor(flag);
				} else if ("Move".equals(selectButton)) {
					setCursor(new Cursor(MOVE_CURSOR));
				} else if ("Rotate".equals(selectButton)) {
					setCursor(rotate);
				} else if ("Scale".equals(selectButton)) {
					setCursor(scale);
				} else if ("Ox Projection".equals(selectButton)) {
					setCursor(new Cursor(W_RESIZE_CURSOR));
				} else if ("Oy Projection".equals(selectButton)) {
					setCursor(new Cursor(N_RESIZE_CURSOR));
				} else if ("Oxy Projection".equals(selectButton)) {
					setCursor(new Cursor(SE_RESIZE_CURSOR));
				} else if ("Delete".equals(selectButton)) {
					setCursor(delete);
				} else if ("Information".equals(selectButton)) {
					setCursor(default_cursor);
				} else if ("Line".equals(selectButton) || "Oval".equals(selectButton) || "Ellipse".equals(selectButton)
						|| "TriangleSquare".equals(selectButton) || "TriangleIsosceles".equals(selectButton)
						|| "Rectangle".equals(selectButton) || "Square".equals(selectButton)
						|| "Parallelogram".equals(selectButton) || "Rhombus".equals(selectButton)
						|| "Pentagon".equals(selectButton) || "Hexagon".equals(selectButton)
						|| "FourPointStar".equals(selectButton) || "FivePointStar".equals(selectButton)
						|| "FivePointStarLine".equals(selectButton) || "SixPointStar".equals(selectButton)
						|| "Flower".equals(selectButton) || "Pinwheel".equals(selectButton)
						|| "Medal".equals(selectButton) || "NationalFlag".equals(selectButton)) {
					setCursor(new Cursor(CROSSHAIR_CURSOR));
				} else if ("Pencil".equals(selectButton)) {
					setCursor(pencil);
				} else if ("FillColor".equals(selectButton)) {
					setCursor(fillColor);
				} else {
					setCursor(default_cursor);
				}
				lbMove.setVisible(true);
				repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(default_cursor);
				lbMove.setVisible(false);
				repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
			}

		});
	}

	////////////////////////////////////////////////////////////////////////////////
	// KHU VỰC CÁC HÀM PHỤ TRỢ//////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	public void executeShift() {
		if (startPoint != null && endPoint != null) {
			int length_X = Math.abs(startPoint.x - endPoint.x);
			int length_Y = Math.abs(startPoint.y - endPoint.y);
			if ((startPoint.x <= endPoint.x) && (startPoint.y < endPoint.y)) { // góc Đông Nam
				if (length_X < length_Y) {
					endPoint = new Point(new Point(startPoint.x + length_X, startPoint.y + length_X));
				} else if (length_X > length_Y) {
					endPoint = new Point(new Point(startPoint.x + length_Y, startPoint.y + length_Y));
				}
			} else if ((startPoint.x > endPoint.x) && (startPoint.y <= endPoint.y)) { // góc Tây Nam
				if (length_X < length_Y) {
					endPoint = new Point(new Point(startPoint.x - length_X, startPoint.y + length_X));
				} else if (length_X > length_Y) {
					endPoint = new Point(new Point(startPoint.x - length_Y, startPoint.y + length_Y));
				}
			} else if ((startPoint.x >= endPoint.x) && (startPoint.y > endPoint.y)) { // góc Tây Bắc
				if (length_X < length_Y) {
					endPoint = new Point(new Point(startPoint.x - length_X, startPoint.y - length_X));
				} else if (length_X > length_Y) {
					endPoint = new Point(new Point(startPoint.x - length_Y, startPoint.y - length_Y));
				}
			} else if ((startPoint.x < endPoint.x) && (startPoint.y >= endPoint.y)) { // góc Đông Bắc
				if (length_X < length_Y) {
					endPoint = new Point(new Point(startPoint.x + length_X, startPoint.y - length_X));
				} else if (length_X > length_Y) {
					endPoint = new Point(new Point(startPoint.x + length_Y, startPoint.y - length_Y));
				}
			}
			repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
		}
	}

	public void executeUndo() {
		if (paint2D.size() != 0) {
			stack.push(paint2D.remove(paint2D.size() - 1));
			repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
			btnRedo.setEnabled(true);
		} else {
			btnUndo.setEnabled(false);
		}
	}

	public void executeRedo() {
		if (!stack.isEmpty()) {
			paint2D.add(stack.pop());
			repaint(move_start.x, move_start.y, drawPanel.getWidth(), drawPanel.getHeight());
			btnUndo.setEnabled(true);
		} else {
			btnRedo.setEnabled(false);
		}
	}

	public static double areaTriangle(Point A, Point B, Point C) {
		return Math.abs(A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y));
	}

	public void executePainting3Diem(Point A, Point B, Point C, Color color, int size, int dx, int dy, Graphics g) {
		MyLine line;
		if (size > 1) {
			line = new MyLine(new Point(A.x + dx, A.y + dy), new Point(B.x + dx, B.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(new Point(B.x + dx, B.y + dy), new Point(C.x + dx, C.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(new Point(C.x + dx, C.y + dy), new Point(A.x + dx, A.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
		} else {
			g.setColor(color);
			g.drawLine(A.x + dx, A.y + dy, B.x + dx, B.y + dy);
			g.drawLine(B.x + dx, B.y + dy, C.x + dx, C.y + dy);
			g.drawLine(C.x + dx, C.y + dy, A.x + dx, A.y + dy);
		}
	}

	public void executePainting4Diem(Point A, Point B, Point C, Point D, Color color, int size, int dx, int dy,
			Graphics g) {
		MyLine line;
		if (size > 1) {
			line = new MyLine(new Point(A.x + dx, A.y + dy), new Point(B.x + dx, B.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(new Point(B.x + dx, B.y + dy), new Point(C.x + dx, C.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(new Point(C.x + dx, C.y + dy), new Point(D.x + dx, D.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
			line = new MyLine(new Point(D.x + dx, D.y + dy), new Point(A.x + dx, A.y + dy));
			line.setSize(size);
			line.setColor(color);
			line.draw(g);
		} else {
			g.setColor(color);
			g.drawLine(A.x + dx, A.y + dy, B.x + dx, B.y + dy);
			g.drawLine(B.x + dx, B.y + dy, C.x + dx, C.y + dy);
			g.drawLine(C.x + dx, C.y + dy, D.x + dx, D.y + dy);
			g.drawLine(D.x + dx, D.y + dy, A.x + dx, A.y + dy);
		}
	}

	public void executePaintingMultiplePoint(Point[] arr_Point, int dx, int dy, int size, Color color, Graphics g) {
		MyLine line;
		int length = arr_Point.length;
		int[] arr_Point_X = new int[length + 1];
		int[] arr_Point_Y = new int[length + 1];
		for (int i = 0; i < length; i++) {
			arr_Point_X[i] = arr_Point[i].x + dx;
			arr_Point_Y[i] = arr_Point[i].y + dy;
		}
		arr_Point_X[length] = arr_Point[0].x + dx;
		arr_Point_Y[length] = arr_Point[0].y + dy;
		if (size > 1) {
			for (int i = 0; i < length; i++) {
				line = new MyLine(new Point(arr_Point_X[i], arr_Point_Y[i]),
						new Point(arr_Point_X[i + 1], arr_Point_Y[i + 1]));
				line.setColor(color);
				line.setSize(size);
				line.draw(g);
			}
		} else {
			g.setColor(color);
			g.drawPolyline(arr_Point_X, arr_Point_Y, length + 1);
		}
	}

	public void executeDrawStarLine(int[] arr_Point_X, int[] arr_Point_Y, int size, Color color, Graphics g) {
		MyLine line;
		int length = arr_Point_X.length;
		if (size > 1) {
			for (int i = 0; i < length - 1; i++) {
				line = new MyLine(new Point(arr_Point_X[i], arr_Point_Y[i]),
						new Point(arr_Point_X[i + 1], arr_Point_Y[i + 1]));
				line.setColor(color);
				line.setSize(size);
				line.draw(g);
			}
		} else {
			g.setColor(color);
			g.drawPolyline(arr_Point_X, arr_Point_Y, length);
		}
	}

	// Hàm lấy màu tại 1 tọa độ bằng Robot
	public Color getPixelColor(int x, int y) {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return robot.getPixelColor(x + localtionFrame.x + move_start.x, y + localtionFrame.y + move_start.y);
	}

	public void drawAxisCoordinates(Graphics g) {
		int x = drawPanel.getWidth();
		int y = drawPanel.getHeight();
		g.setColor(Color.DARK_GRAY);
		g.drawLine(x / 2 + move_start.x, move_start.y + 2, x / 2 + move_start.x, y + move_start.y - 1);
		g.drawLine(move_start.x + 2, y / 2 + move_start.y, x + move_start.x - 1, y / 2 + move_start.y);
		g.setColor(Color.RED);
		g.fillOval(x / 2 + move_start.x - 2, y / 2 + move_start.y - 2, 5, 5);

		g.drawString("O", centerAxisPoint.x - 15, centerAxisPoint.y + 15);
		g.drawString("X", drawPanel.getWidth() + move_start.x - 20, centerAxisPoint.y - 7);
		g.drawString("Y", centerAxisPoint.x + 5, move_start.y + 20);
	}

	public static float angleBetweenTwoLines(Point A, Point B) {
		float angle1;
		float angle2;
		float calculatedAngle;
		if (flagBoolean) {
			angle1 = (float) Math.atan2(flag_Point.y - A.y, A.x - flag_Point.x);
			angle2 = (float) Math.atan2(flag_Point.y - B.y, B.x - flag_Point.x);
			calculatedAngle = (float) Math.toDegrees(angle1 - angle2);
		} else {
			angle1 = (float) Math.atan2(centerAxisPoint.y - A.y, A.x - centerAxisPoint.x);
			angle2 = (float) Math.atan2(centerAxisPoint.y - B.y, B.x - centerAxisPoint.x);
			calculatedAngle = (float) Math.toDegrees(angle1 - angle2);
		}
		if (calculatedAngle < 0) {
			calculatedAngle += 360;
		}
		return calculatedAngle;
	}

	public static Point rotateAround(Point p, float angle) {
		Point p2 = new Point();
		AffineTransform rotation = new AffineTransform();
		double angleInRadians = (angle * Math.PI / 180);
		if (flagBoolean) {
			rotation.rotate(angleInRadians, flag_Point.x, flag_Point.y);
		} else {
			rotation.rotate(angleInRadians, centerAxisPoint.x, centerAxisPoint.y);
		}
		rotation.transform(p, p2);

		return p2;
	}

	public void execute4DiemOfRotate(float angle, Point A, Point B, Point C, Point D, Color color, int size,
			Graphics g) {
		Point p1 = new Point(rotateAround(A, angle));
		Point p2 = new Point(rotateAround(B, angle));
		Point p3 = new Point(rotateAround(C, angle));
		Point p4 = new Point(rotateAround(D, angle));
		executePainting4Diem(p1, p2, p3, p4, color, size, 0, 0, g);
	}

	public void execute3DiemOfRotate(float angle, Point A, Point B, Point C, Color color, int size, Graphics g) {
		Point p1 = new Point(rotateAround(A, angle));
		Point p2 = new Point(rotateAround(B, angle));
		Point p3 = new Point(rotateAround(C, angle));
		executePainting3Diem(p1, p2, p3, color, size, 0, 0, g);
	}

	public void executeMultipleOfRotate(Point[] arr_Point, float angle, int size, Color color, Graphics g) {
		int length = arr_Point.length;
		Point[] p = new Point[length + 1];
		for (int i = 0; i < length; i++) {
			p[i] = new Point(rotateAround(arr_Point[i], angle));
		}
		p[length] = p[0];
		if (size > 1) {
			MyLine line;
			for (int i = 0; i < length; i++) {
				line = new MyLine(p[i], p[i + 1]);
				line.setColor(color);
				line.setSize(size);
				line.draw(g);
			}
		} else {
			g.setColor(color);
			for (int i = 0; i < length; i++) {
				g.drawLine(p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);
			}
		}
	}

	public void execute4DiemOfScale(float angle, Point A, Point B, Point C, Point D, int size, Color color, int dx,
			int dy, Graphics g) {

		// Cho các điểm quay về theo trục Oxy để dễ tính chiều dài
		Point p1 = new Point(rotateAround(A, angle * (-1)));
		Point p2 = new Point(rotateAround(B, angle * (-1)));
		Point p3 = new Point(rotateAround(C, angle * (-1)));
		Point p4 = new Point(rotateAround(D, angle * (-1)));

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
		// Sau khi tính độ xê lệch cho quay tọa độ về cũ
		p1 = rotateAround(p1, angle);
		p2 = rotateAround(p2, angle);
		p3 = rotateAround(p3, angle);
		p4 = rotateAround(p4, angle);
		executePainting4Diem(p1, p2, p3, p4, color, size, 0, 0, g);
	}

	public static Point convertX(Point p) {
		Point temp = new Point();
		if (flagBoolean) {
			temp.x = (p.x - flag_Point.x) * (-1) + flag_Point.x;
		} else {
			temp.x = (p.x - centerAxisPoint.x) * (-1) + centerAxisPoint.x;
		}
		temp.y = p.y;
		return temp;
	}

	public static Point convertY(Point p) {
		Point temp = new Point();
		temp.x = p.x;
		if (flagBoolean) {
			temp.y = (p.y - flag_Point.y) * (-1) + flag_Point.y;
		} else {
			temp.y = (p.y - centerAxisPoint.y) * (-1) + centerAxisPoint.y;
		}
		return temp;
	}

	public static Point convertXY(Point p) {
		Point temp = new Point();
		if (flagBoolean) {
			temp.x = (p.x - flag_Point.x) * (-1) + flag_Point.x;
			temp.y = (p.y - flag_Point.y) * (-1) + flag_Point.y;
		} else {
			temp.x = (p.x - centerAxisPoint.x) * (-1) + centerAxisPoint.x;
			temp.y = (p.y - centerAxisPoint.y) * (-1) + centerAxisPoint.y;
		}
		return temp;
	}

	private void drawEllip(Graphics g, MyEllipse ellipse, float a) {
		float dx = (float) ellipse.getA().distance(ellipse.getB()) / 2;
		float dy = (float) ellipse.getA().distance(ellipse.getD()) / 2;
		float rxSq = dx * dx;
		float rySq = dy * dy;
		float x1 = 0, y1 = dy, p;
		float px = 0, py = 2 * rxSq * y1;
		Point p1 = new Point(rotateAround(ellipse.getO(), ellipse.getAngle() * (-1)));
		g.setColor(ellipse.getColor());
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
			ellipse.ve4diem(g, p1.x, p1.y, (int) x1, (int) y1, a);
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
			ellipse.ve4diem(g, p1.x, p1.y, (int) x1, (int) y1, a);
		}
	}

	public boolean isNumberic(String input) {
		try {
			Float.parseFloat(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void toMau(int x, int y, Color mauto, Color maubien, Graphics g) {
		int x1, x2;
		do {
			x1 = x;
			x2 = x;
			while (!compareColor(getPixelColor(x1 - 1, y), maubien)) {
				System.out.println(getPixelColor(x1 - 1, y));
				System.out.println(maubien);
				System.out.println("X1: " + x1);
				x1--;
			}
			while (!compareColor(getPixelColor(x2 + 1, y), maubien)) {
				x2++;
			}
			System.out.println(x1 + move_start.x + " " + x2 + move_start.x);
			g.setColor(mauto);
			g.drawLine(x1, y, x2, y);
			while (compareColor(getPixelColor(x1, y + 1), maubien)) {
				x1++;
			}
			if (x1 <= x2) {
				x = x1;
				y = y + 1;
			}

		} while (x1 <= x2);
	}

	public int realToDeviceX(int x) {
		return (x - centerAxisPoint.x);
	}

	public int realToDeviceY(int y) {
		return (y - centerAxisPoint.y) * (-1);
	}

	public boolean compareColor(Color a, Color b) {
		if (a.getRed() == b.getRed() && a.getGreen() == b.getGreen() && a.getBlue() == b.getBlue()) {
			return true;
		} else {
			return false;
		}
	}
}
