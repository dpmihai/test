/**
 *    Copyright 2007 Pieter-Jan Savat
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package book;
 

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;



public class JBookPanelTest {

	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		JBookPanel turnerDefault = new JBookPanel();
		turnerDefault.setMargins(20, 170);
		
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setTopComponent(turnerDefault);
		split.setBottomComponent(new ConfigPanel(turnerDefault));
		split.setDividerLocation(380);
		
		// webstart has some difficulties if resource is not load
		// from the same folder, therefore override the load method
		JBookPanel turnerNewsPaper = new JBookPanel() {
			
			protected Image loadPage(int index) {
				return new ImageIcon(JBookPanelTest.class.getResource(pageLocation + pageName 
						+ index + "." + pageExtension)).getImage();
			}
		};
		turnerNewsPaper.setPages("", "2103DS1-DS", "jpg", 8, 350, 472);
		turnerNewsPaper.setMargins(30, 40);
		turnerNewsPaper.setBackground(new Color(157,185,235));
		turnerNewsPaper.setLeftPageIndex(-1);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.setFocusable(false);
		tabs.addTab("Newspaper example", turnerNewsPaper);
		tabs.addTab("Default constructor", split);
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(tabs);
		f.setSize(800, 600);
		f.setLocationRelativeTo(null);
		f.setTitle("JBookPanel (first version)");
		f.setVisible(true);
	}

	static class ConfigPanel extends JPanel {

		private JLabel jLabel = null;
		private JLabel jLabel1 = null;
		private JLabel jLabel2 = null;
		private JLabel jLabel3 = null;
		private JLabel jLabel4 = null;
		private JCheckBox jCheckBox = null;
		private JCheckBox jCheckBox1 = null;
		private JTextField jTextField = null;
		private JTextField jTextField1 = null;
		private JButton jButton = null;
		private JBookPanel pagePanel;

		public ConfigPanel(JBookPanel pagePanel) {
			super();
			this.pagePanel = pagePanel;
			initialize();
			this.setBackground(new Color(254,255,223));
			
			getJCheckBox().setSelected(pagePanel.isSoftClipping());
			getJCheckBox1().setSelected(pagePanel.isBorderLinesVisible());
			getJTextField().setText(String.valueOf(pagePanel.getRefreshSpeed()));
			getJTextField1().setText(String.valueOf(pagePanel.getShadowWidth()));
			
			getJCheckBox().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ConfigPanel.this.pagePanel.setSoftClipping(getJCheckBox().isSelected());
				}});
			getJCheckBox1().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ConfigPanel.this.pagePanel.setBorderLinesVisible(getJCheckBox1().isSelected());
					ConfigPanel.this.pagePanel.repaint();
				}});
			getJTextField().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int i = Integer.parseInt(getJTextField().getText());
						ConfigPanel.this.pagePanel.setRefreshSpeed(i);
					} catch (Exception ex) {
					}
				}});
			getJTextField1().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int i = Integer.parseInt(getJTextField1().getText());
						ConfigPanel.this.pagePanel.setShadowWidth(i);
					} catch (Exception ex) {
					}
				}});
			getJButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color c = JColorChooser.showDialog(ConfigPanel.this, 
							"Pick shadow color", 
							ConfigPanel.this.pagePanel.getShadowDarkColor());
					if (c != null) {
						ConfigPanel.this.pagePanel.setShadowDarkColor(c);
					}
				}});
		}

		private void initialize() {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints7.gridy = 4;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.weightx = 0.0D;
			gridBagConstraints6.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 0.0D;
			gridBagConstraints5.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints3.gridy = 4;
			jLabel3 = new JLabel();
			jLabel3.setText("Shadow color:");
			jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints2.gridy = 3;
			jLabel2 = new JLabel();
			jLabel2.setText("Shadow width:");
			jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints1.gridy = 2;
			jLabel1 = new JLabel();
			jLabel1.setText("Refresh speed:");
			jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = 1;
			jLabel = new JLabel();
			jLabel.setText("Soft clipping:");
			jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel4 = new JLabel();
			jLabel4.setText("Show borders:");
			jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
			this.setSize(300, 132);
			this.setLayout(new GridBagLayout());
			this.add(jLabel, gridBagConstraints);
			this.add(jLabel4, gridBagConstraints9);
			this.add(jLabel1, gridBagConstraints1);
			this.add(jLabel2, gridBagConstraints2);
			this.add(jLabel3, gridBagConstraints3);
			this.add(getJCheckBox(), gridBagConstraints4);
			this.add(getJCheckBox1(), gridBagConstraints8);
			this.add(getJTextField(), gridBagConstraints5);
			this.add(getJTextField1(), gridBagConstraints6);
			this.add(getJButton(), gridBagConstraints7);
		}

		private JCheckBox getJCheckBox() {
			if (jCheckBox == null) {
				jCheckBox = new JCheckBox();
				jCheckBox.setOpaque(false);
			}
			return jCheckBox;
		}

		private JCheckBox getJCheckBox1() {
			if (jCheckBox1 == null) {
				jCheckBox1 = new JCheckBox();
				jCheckBox1.setOpaque(false);
			}
			return jCheckBox1;
		}
		
		private JTextField getJTextField() {
			if (jTextField == null) {
				jTextField = new JTextField();
				jTextField.setColumns(4);
			}
			return jTextField;
		}

		private JTextField getJTextField1() {
			if (jTextField1 == null) {
				jTextField1 = new JTextField();
				jTextField1.setColumns(4);
			}
			return jTextField1;
		}

		private JButton getJButton() {
			if (jButton == null) {
				jButton = new JButton();
				jButton.setText("...");
			}
			return jButton;
		}
	}
}