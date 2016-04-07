/**
 * @(#)Ch12_hw.java
 *
 * Ch12_hw application
 *
 * @author 
 * @version 1.00 2010/4/12
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Ch12_hw extends JApplet implements MouseMotionListener,
												MouseListener, ItemListener
{
	// controls.
	private JComboBox eyeColorDD;
	private JCheckBox avoidCB;
	
	// listbox values
	private String[] dList = 
	{"Black", "Blue", "Brown", "Green"};	
		
	// size of the form
	private final int WIDTH = 500;
	private final int HEIGHT = 300;
	
	// coordinates for Colored Circles.
	private int leftArcX;
	private int rightArcX;
	private int ArcY;
	
	// trigonometric values from mouse cursor to each circles.
	private double leftCos = 0.0;
	private double leftSin = 0.0;
	private double rightCos = 0.0;
	private double rightSin = 0.0;
	
	// checkbox value about avoid
	private boolean avoid = false;
	
	// current color for circle
	private Color currentColor = Color.black;
	
	public void init()
	{
		leftArcX = WIDTH / 4;
		rightArcX = WIDTH / 4 * 3;
		ArcY  = HEIGHT / 2;
		
		eyeColorDD = new JComboBox(dList);
		avoidCB = new JCheckBox("Avoid Cursor");
		
		// set location and size
		eyeColorDD.setSize(80,20);
		eyeColorDD.setLocation(200, HEIGHT - 40);
		avoidCB.setSize(100, 20);
		avoidCB.setLocation(330, HEIGHT - 40);
		
		// container pane & add controls
		Container pane = getContentPane();
		pane.setLayout(null);
		
		pane.add(eyeColorDD);
		pane.add(avoidCB);
		
		// initialize the position of circle
		leftCos = Math.cos(0 * Math.PI / 180);
		leftSin = Math.sin(0 * Math.PI / 180);
		rightCos = Math.cos(0 * Math.PI / 180);
		rightSin = Math.sin(0 * Math.PI / 180);
		
		// add events
		eyeColorDD.addItemListener(this);
		avoidCB.addItemListener(this);
		
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		// set title
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier", Font.PLAIN, 24));
		g.drawString("Click or drag anywhere in the form.", 60, 45);
		
		// set label for combobox
		g.setFont(new Font("Courier", Font.PLAIN, 12));
		g.drawString("Color of the circle: ", 95, HEIGHT - 27);
		
		// draw larger circle
		drawCircle(g, WIDTH / 4, HEIGHT / 2, 75);
		drawCircle(g, WIDTH / 4 * 3, HEIGHT / 2, 75);
		
		// set color for smaller circles
		g.setColor(currentColor);
		
		// draw smaller circles
		fillCircle(g, (int)(WIDTH / 4 + (leftCos * 37)),
			(int)(HEIGHT / 2 - (leftSin * 37)), 37);
		fillCircle(g, (int)(WIDTH / 4 * 3 + (rightCos * 37)),
			(int)(HEIGHT / 2 - (rightSin * 37)), 37);
	}
	
	// draw circle using radius, and center coordinates
	public void drawCircle(Graphics g, int x, int y, int rad)
	{
		int _x = (int)x - rad;
		int _y = (int)y - rad;
		int _w = (int)rad * 2;
		int _h = (int)rad * 2;
		g.drawArc(_x, _y, _w, _h, 0, 360);
	}
	
	// draw circle using radius, and center coordinates
	public void fillCircle(Graphics g, int x, int y, int rad)
	{
		int _x = (int)x - rad;
		int _y = (int)y - rad;
		int _w = (int)rad * 2;
		int _h = (int)rad * 2;
		g.fillArc(_x, _y, _w, _h, 0, 360);
	}
	
	//a function returns the hypotenuse
	public static double getLeg(double a, double b)
	{
		a = Math.pow(a, 2);
		b = Math.pow(b, 2);
		
		return Math.sqrt(a + b);
	}
	
	public void mouseDragged(MouseEvent e)
	{
		calculateArcPosition(e);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		calculateArcPosition(e);
	}
    
    // calculate the position of the arc from mouse coordinates
    public void calculateArcPosition(MouseEvent e)
    {
    	int a =0;
	    int b =0;
		
		double lLeg = getLeg(ArcY - e.getY(), e.getX() - leftArcX);
		double lcos = (e.getX() - leftArcX) / lLeg;
		double lsin = (ArcY - e.getY()) / lLeg;
		
		double rLeg = getLeg(ArcY - e.getY(), e.getX() - rightArcX);
		double rcos = (e.getX() - rightArcX) / rLeg;
		double rsin = (ArcY - e.getY()) / rLeg;
		
		// move opposite if "avoid" is checked
		if (avoid == true)
		{
			lcos = -lcos;
			lsin = -lsin;
			
			rcos = -rcos;
			rsin = -rsin;
		}
		
		leftCos = lcos;
		leftSin = lsin;
		
		rightCos = rcos;
		rightSin = rsin;
		
		repaint();
    }
    
    // event
    public void itemStateChanged(ItemEvent e)
    {
    	// listbox event (color)
    	if (e.getSource() == eyeColorDD)
    	{
    		switch (eyeColorDD.getSelectedIndex())
    		{
    			case 0:
    				currentColor = Color.BLACK;
    				break;
    			case 1:
    				currentColor = Color.BLUE;
    				break;
    			case 2:
    				currentColor = new Color(165, 42, 42); // brown
    				break;
    			case 3:
    				currentColor = new Color(0, 128, 0); // green
    				break;
    			default:
    		}
    	}
    	
    	// checkbox event
    	if (e.getSource() == avoidCB)
    	{
    		if (e.getStateChange() == ItemEvent.SELECTED)
    			avoid = true;
    		else if (e.getStateChange() == ItemEvent.DESELECTED)
    			avoid = false;
    		
    		// recalculate circle immediately
    		leftCos = -leftCos;
    		leftSin = -leftSin;
    		rightCos = -rightCos;
    		rightSin = -rightSin;
    	}
    	
    	repaint();
    }
    
    public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}
