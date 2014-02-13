import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * GUI Class
 * 
 * Creates all buttons and events to receive user input.
 * 
 * @author Scott Bell
 * @version 12-5-13
 */

public class ShapeGUI extends JPanel {
	private Shape s;

	private JLabel titleLabel;
	private JButton createButton, chooseButton, scaleButton;
	private JButton translateButton, rotateButton, reflectButton;
	private JButton clearButton, zInButton, zOutButton, zResetButton;

	public ShapeGUI() {
		s = new Shape(); // Make display area

		// Creates the label and buttons
		titleLabel = new JLabel("Shape Manipulator");
		titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
		createButton = new JButton("Create Shape");
		chooseButton = new JButton("Choose Shape");
		scaleButton = new JButton("Scale");
		translateButton = new JButton("Translate");
		rotateButton = new JButton("Rotate Shape");
		reflectButton = new JButton("Reflect Shape");
		clearButton = new JButton("Clear");
		zInButton = new JButton("Zoom In");
		zOutButton = new JButton("Zoom Out");
		zResetButton = new JButton("Reset Zoom");

		// Panels to hold the label, display, and buttons
		Panel northPanel = new Panel();
		Panel centerPanel = new Panel();
		Panel operationsPanel = new Panel(new GridLayout(20, 2));
		Panel createPanel = new Panel(new GridLayout(20, 2));
		setLayout(new BorderLayout());

		// Adds the listener to all buttons
		createButton.addActionListener(new ButtonListener());
		chooseButton.addActionListener(new ButtonListener());
		scaleButton.addActionListener(new ButtonListener());
		translateButton.addActionListener(new ButtonListener());
		rotateButton.addActionListener(new ButtonListener());
		reflectButton.addActionListener(new ButtonListener());
		clearButton.addActionListener(new ButtonListener());
		zInButton.addActionListener(new ButtonListener());
		zOutButton.addActionListener(new ButtonListener());
		zResetButton.addActionListener(new ButtonListener());

		// Add label, display, and buttons to correct panels
		northPanel.add(titleLabel, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);

		centerPanel.add(s, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);

		createPanel.add(createButton, BorderLayout.EAST);
		createPanel.add(Box.createVerticalStrut(15)); // a spacer between
														// buttons
		createPanel.add(chooseButton, BorderLayout.EAST);
		createPanel.add(Box.createVerticalStrut(15));
		createPanel.add(clearButton, BorderLayout.EAST);
		add(createPanel, BorderLayout.WEST);

		operationsPanel.add(scaleButton, BorderLayout.WEST);
		operationsPanel.add(Box.createVerticalStrut(15));
		operationsPanel.add(translateButton, BorderLayout.WEST);
		operationsPanel.add(Box.createVerticalStrut(15));
		operationsPanel.add(rotateButton, BorderLayout.WEST);
		operationsPanel.add(Box.createVerticalStrut(15));
		operationsPanel.add(reflectButton, BorderLayout.WEST);
		operationsPanel.add(Box.createVerticalStrut(15));
		operationsPanel.add(zInButton, BorderLayout.WEST);
		operationsPanel.add(Box.createVerticalStrut(15));
		operationsPanel.add(zOutButton, BorderLayout.WEST);
		operationsPanel.add(Box.createVerticalStrut(15));
		operationsPanel.add(zResetButton, BorderLayout.WEST);
		add(operationsPanel, BorderLayout.EAST);

		// Sets size and color of main window
		setPreferredSize(new Dimension(1000, 800));
		setBackground(Color.orange);

	}

	/**
	 * Listener for all buttons.
	 * 
	 * Appropriate methods of the shape are invoked depending on what is
	 * pressed.
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// User created Shape - 3 separate dialogs: Number of points,
			// Coordinates, Connections
			if (e.getSource() == createButton) {
				s.clear();
				int numPoints = 0;
				boolean skip = false;
				ArrayList<JTextField> inputs = new ArrayList<JTextField>(); // Holds
																			// user
																			// input
				ArrayList<Integer> points = new ArrayList<Integer>(); // Instructions
				ArrayList<Integer> connections = new ArrayList<Integer>();
																			
																			

				// First dialog asks how many points
				JTextField pfield = new JTextField(10);
				JPanel pointPanel = new JPanel();
				pointPanel.add(pfield);

				int result = (JOptionPane.showConfirmDialog(null, pointPanel,
						"Number of Points?", JOptionPane.OK_CANCEL_OPTION));
				if (result == JOptionPane.OK_OPTION) {

					// Retrieve input from text field. If invalid, notify user
					// and cancel method.
					try {
						numPoints = Integer.parseInt(pfield.getText());
						if (numPoints == 0) 
							throw new NumberFormatException();

					} catch (NumberFormatException q) {
						JOptionPane.showMessageDialog(null, "Invalid input");
						skip = true;
					}
					if (!skip) {

						// Second dialog asks what coordinates
						JTextField t;
						JPanel textPanel = new JPanel(new GridLayout(numPoints,
								2));

						// A label and two text fields appear for each point
						for (int i = 0; i < 2 * numPoints; i++) {
							t = new JTextField(10);
							if (i % 2 == 0)
								textPanel.add(new JLabel("Point "
										+ ((i / 2) + 1) + ": "));
							textPanel.add(t);
							inputs.add(t);

						}

						int result2 = JOptionPane.showConfirmDialog(null,
								textPanel, "What Points?",
								JOptionPane.OK_CANCEL_OPTION);
						if (result2 == JOptionPane.OK_OPTION) {
							try {
								for (int j = 0; j < inputs.size(); j++) {
									points.add(Integer.parseInt(inputs
											.get(j).getText()));
								}

							} catch (NumberFormatException q) {
								JOptionPane.showMessageDialog(null,
										"Invalid input");
								skip = true;
							}
							;

							// Third dialog asks what points to connect
							if (!skip) {

								inputs.clear();
								JPanel infoPanel = new JPanel(
										new GridLayout(0, 3));
								JPanel connectPanel = new JPanel(
										new GridLayout(0, 3));
								

								// Displays current points and their number
								int count = 1;
								for (int p = 0; p < points.size(); p += 2) {
									infoPanel
											.add(new JLabel("Point " + count
													+ ": ("
													+ points.get(p)
													+ ", "
													+ points.get(p + 1)
													+ ")    "));
									count++;
								}
								count--;
								int value = count % 3;
								switch (value) {
								case 1: {
									infoPanel.add(new JLabel(""));
									infoPanel.add(new JLabel(""));
									break;
								}
								case 2: {
									infoPanel.add(new JLabel(""));
									break;

								}
								}
								for (int spacer = 0; spacer < 3; spacer++)
									infoPanel.add(new JLabel(""));

								// Enough labels and text fields appear to
								// possible connect all points
								for (int k = 0; k < numPoints * (numPoints - 1); k++) {
									t = new JTextField(10);

									if (k % 2 == 0)
										connectPanel.add(new JLabel(
												"Connection " + ((k / 2) + 1)));
									connectPanel.add(t);
									inputs.add(t);

								}
								
								// Scroll area for connections
								final JScrollPane scrollPane = new JScrollPane(connectPanel);
								scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
								if(numPoints * (numPoints - 1) > 12)
									scrollPane.setPreferredSize(new Dimension(500, 305));
								
								
								
								JPanel combined = new JPanel(new BorderLayout());
								combined.add(infoPanel, BorderLayout.NORTH);
								combined.add(scrollPane, BorderLayout.CENTER);
								
								// Connect all checkbox
								JCheckBox connectALL = new JCheckBox("Connect every point");
								connectALL.addItemListener(new ItemListener() {
									@Override
									public void itemStateChanged(ItemEvent e){
										if(e.getStateChange() == ItemEvent.SELECTED)
											scrollPane.setVisible(false);
										else
											scrollPane.setVisible(true);
											
										}
									}
								);
								combined.add(connectALL, BorderLayout.SOUTH);
								
								
								int result3 = JOptionPane.showConfirmDialog(
										null, combined,
										"Possible Connections?  ",
										JOptionPane.OK_CANCEL_OPTION);
								if (result3 == JOptionPane.OK_OPTION) {
									if(connectALL.isSelected()){
										for(int m = 0; m < points.size()/2;m++)
											for (int n= 0; n < points.size()/2-1; n++){
												connections.add(m);
												connections.add(n);
											}
										
									}
									else{
									int check;
									for (int l = 0; l < inputs.size(); l++) {
										try {
											check = -1
													+ Integer.parseInt(inputs
															.get(l).getText());
											if (check >= numPoints)
												throw new NumberFormatException();
											connections.add(check);

										} catch (NumberFormatException q) {
											if (l % 2 == 0)
												l++;
											else
												connections
														.remove(connections
																.size() - 1);
										}
									}
									}
									// Finally draw the shape
									s.drawShape(points, connections);
								}
							}
						}
					}
				}

			}

			// Pre-made shape. User inputs information to choose shape, size,
			// and location
			if (e.getSource() == chooseButton) {

				// Three choices to draw : Square, Equilateral Triangle, Circle

				// RadioButtons to choose
				JRadioButton square = new JRadioButton("Square");
				JRadioButton triangle = new JRadioButton("Triangle");
				JRadioButton star = new JRadioButton("Circle");

				// TextFields for sideLength/Radius, and coordinates for center
				JTextField sField = new JTextField(5);
				JTextField cxField = new JTextField(5);
				JTextField cyField = new JTextField(5);

				ButtonGroup group = new ButtonGroup();
				group.add(square);
				group.add(triangle);
				group.add(star);

				JPanel shapeMaker = new JPanel(new GridLayout(3, 2));
				shapeMaker.add(square);
				shapeMaker.add(triangle);
				shapeMaker.add(star);
				shapeMaker.add(new JLabel("Side Length / Radius : "));
				shapeMaker.add(sField);
				shapeMaker.add(Box.createHorizontalStrut(15));
				shapeMaker
						.add(new JLabel("                          Center :"));
				shapeMaker.add(cxField);
				shapeMaker.add(cyField);

				// Choose shape dialog
				int result = JOptionPane.showConfirmDialog(null, shapeMaker,
						"What Shape, Side Length/Radius, and Center?",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					
					// Creates variables for sideLength/Radius and center
					// If invalid, notifies user and cancels method
					try {
						double sides = Double.parseDouble(sField.getText());
						double centerx = Double.parseDouble(cxField.getText());
						double centery = Double.parseDouble(cyField.getText());
						Vector v = new Vector(centerx, centery);

						if (square.isSelected()) {
							s.drawSquare(sides, v);
						} else if (triangle.isSelected()) {
							s.drawTriangle(sides, v);
						} else if (star.isSelected()) {
							s.drawCircle(sides, v);
						} else
							JOptionPane
									.showMessageDialog(null, "Invalid input");
					} catch (NumberFormatException q) {
						JOptionPane.showMessageDialog(null, "Invalid input");
					}

				}

			}

			// Takes scalar and scales shape
			if (e.getSource() == scaleButton) {
				JTextField field = new JTextField(10);
				JPanel scalePanel = new JPanel();
				scalePanel.add(field);

				int result = (JOptionPane.showConfirmDialog(null, scalePanel,
						"Scale Shape", JOptionPane.OK_CANCEL_OPTION));
				if (result == JOptionPane.OK_OPTION) {
					try {
						double scale = Double.parseDouble(field.getText());
						s.scaleShape(scale);
					} catch (NumberFormatException q) {
						JOptionPane.showMessageDialog(null, "Invalid input");
					}
				}
			}

			// Takes amounts to move x and y by and translates shape
			if (e.getSource() == translateButton) {
				JTextField xField = new JTextField(5);
				JTextField yField = new JTextField(5);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Move x:"));
				myPanel.add(xField);
				myPanel.add(Box.createHorizontalStrut(15)); 
				myPanel.add(new JLabel("Move y:"));
				myPanel.add(yField);
				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Translate Shape", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						double moveX = Double.parseDouble(xField.getText());
						double moveY = Double.parseDouble(yField.getText());

						s.translateShape(moveX, moveY);
					} catch (NumberFormatException q) {
						JOptionPane.showMessageDialog(null, "Invalid input");
					}
				}

			}

			// Takes degrees and if the user wants to rotate clockwise or not.  Then rotates shape
			if (e.getSource() == rotateButton) {
				JTextField dField = new JTextField(5);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Degrees:"));
				myPanel.add(dField);
				myPanel.add(Box.createHorizontalStrut(15));
				JCheckBox clockBox = new JCheckBox("Clockwise?");
				myPanel.add(clockBox);

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Rotate Shape", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						double degrees = Double.parseDouble(dField.getText());
						boolean clockwise;
						if (clockBox.isSelected())
							clockwise = true;
						else
							clockwise = false;

						s.rotateShape(degrees, clockwise);
					} catch (NumberFormatException q) {
						JOptionPane.showMessageDialog(null, "Invalid input");
					}
				}

			}

			// Receives input on whether the user wants to reflect over
			// the x-axis, y-axis, or orign.  Then reflects shape.
			if (e.getSource() == reflectButton) {
				JRadioButton xAxis = new JRadioButton("X-Axis");
				JRadioButton yAxis = new JRadioButton("Y-Axis");
				JRadioButton origin = new JRadioButton("Origin");

				ButtonGroup group = new ButtonGroup();
				group.add(xAxis);
				group.add(yAxis);
				group.add(origin);
				JPanel myPanel = new JPanel();
				myPanel.add(xAxis);
				myPanel.add(yAxis);
				myPanel.add(origin);

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Reflect Shape Over What?",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					char choice = 'o';
					if (xAxis.isSelected())
						choice = 'x';
					else if (yAxis.isSelected())
						choice = 'y';
					else if (origin.isSelected())
						choice = 'o';
					else
						JOptionPane.showMessageDialog(null, "Invalid input");

					s.reflectShape(choice);

				}

			}

			// Clears the display after asking for confirmation
			if (e.getSource() == clearButton) {
				int reply = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to clear?", "Clear Shape",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION)
					s.clear();
			}

			// Increases zoom
			if (e.getSource() == zInButton) {
				s.zoomIn();
			}
			
			// Decreases zoom
			if (e.getSource() == zOutButton) {
				s.zoomOut();
			}
			
			// Resets zoom
			if (e.getSource() == zResetButton) {
				s.resetZoom();
			}

		}
	}

	//Draws a shape without having to create one.  For testing.
	public void test() {
		ArrayList<Integer> points = new ArrayList<Integer>();
		ArrayList<Integer> connections = new ArrayList<Integer>();
		
		points.add(1);
		points.add(-1);
		points.add(5);
		points.add(-5);
		points.add(2);
		points.add(4);
		points.add(-4);
		points.add(-3);

		connections.add(0);
		connections.add(1);
		connections.add(1);
		connections.add(2);
		connections.add(2);
		connections.add(3);
		connections.add(3);
		connections.add(0);
		connections.add(0);
		connections.add(2);
		s.drawSquare(2, new Vector(0, 0));

	}

	//Creates window and runs program
	public static void main(String args[]) {
		JFrame frame = new JFrame("Shape Manipulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ShapeGUI gui = new ShapeGUI();
		frame.getContentPane().add(gui);
		frame.pack();
		frame.setVisible(true);

		//gui.test();
	}
}
