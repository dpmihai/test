package switchbutton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

public class SwitchToggleButton extends JComponent {
	
	private JToggleButton leftButton;
	private JToggleButton rightButton;	
	
	private Image leftGrayImage;
	private Image rightGrayImage;
	
	private Action leftAction;
	private Action rightAction;
	
	public SwitchToggleButton() {			
		leftButton = new JToggleButton();
		leftButton.setBorderPainted(false);
		rightButton = new JToggleButton();	
		rightButton.setBorderPainted(false);
		
		leftButton.setSelected(true);
		
		leftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {							
				rightButton.setSelected(!leftButton.isSelected());			
				doAction(e);		
			}			
		});
		
		rightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {					
				leftButton.setSelected(!rightButton.isSelected());
				doAction(e);		
			}			
		});		
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		add(leftButton);
		add(rightButton);
	}
	
	public void setActive(boolean isLeft) {		
		leftButton.setSelected(isLeft);		
		rightButton.setSelected(!isLeft);
		doAction(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "toggle"));
	}		
	
	private void setLeftIcon(ImageIcon leftIcon) {
		leftButton.setSelectedIcon(leftIcon);		
		Image leftImage = leftIcon.getImage();		
		Image leftGrayImage = GrayFilter.createDisabledImage(leftImage); //createDisabledImage(leftImage);
		leftButton.setIcon(new ImageIcon(leftGrayImage));
	}
	
	private void setRightIcon(ImageIcon rightIcon) {
		rightButton.setSelectedIcon(rightIcon);
		Image rightImage = rightIcon.getImage();		
		Image rightGrayImage = GrayFilter.createDisabledImage(rightImage);//createDisabledImage(rightImage);
		rightButton.setIcon(new ImageIcon(rightGrayImage));
	}
		
	public void setPreferredSize(Dimension dim) {
		Dimension half = new Dimension((int)(dim.getWidth()/2), (int)dim.getHeight());		
		leftButton.setPreferredSize(half);		
		rightButton.setPreferredSize(half);
	}
	
	public void setLeftAction(Action leftAction) {
		this.leftAction = leftAction;
		setLeftIcon((ImageIcon)leftAction.getValue(Action.SMALL_ICON));
	}
	
	public void setRightAction(Action rightAction) {
		this.rightAction = rightAction;
		setRightIcon((ImageIcon)rightAction.getValue(Action.SMALL_ICON));
	}
	
	public void setBackground(Color color) {
		leftButton.setBackground(color);
		rightButton.setBackground(color);
	}
	
	public void setLeftText(String text) {
		leftButton.setText(text);
	}
	
	public void setRightText(String text) {
		rightButton.setText(text);
	}
	
	public void setLeftTooltip(String tooltip) {
		leftButton.setToolTipText(tooltip);
	}
	
	public void setRightTooltip(String tooltip) {
		rightButton.setToolTipText(tooltip);
	}
	
	//@see GrayFilter
	private Image createDisabledImage (Image i) {
        GrayFilter filter = new GrayFilter(true, 80);
        ImageProducer prod = new FilteredImageSource(i.getSource(), filter);
        Image grayImage = Toolkit.getDefaultToolkit().createImage(prod);
        return grayImage;
    }
	
	private void doAction(ActionEvent e) {		
		if (leftButton.isSelected()) {
			if  (leftAction != null) {
				leftAction.actionPerformed(e);
			} else {
				onLeftSelected(e);
			}
		} else if (rightButton.isSelected()) {
			if (rightAction != null) {
				rightAction.actionPerformed(e);
			} else {
				onRightSelected(e);
			}
		}  						
	}		
	
	protected void onLeftSelected(ActionEvent e) {		
	}
	
	protected void onRightSelected(ActionEvent e) {		
	}

}
