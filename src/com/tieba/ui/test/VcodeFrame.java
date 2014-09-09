package com.tieba.ui.test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.csm.common.ThreadUtil;

public class VcodeFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String title = "验证码输入";
	private JLabel label;
	private JTextField text;
	static boolean isOver = false;
	public VcodeFrame()
	{
		super(title);
		label = new JLabel();
		text = new JTextField();
		this.setLayout(null);
		label.setBounds(25, 25, 250, 400);
		text.setBounds(35,400,230,30);
		
		this.add(label);
		this.add(text);
		this.setBounds(0, 0, 300, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void updatePic(String location)
	{
		try
		{
			label.setIcon(new ImageIcon(new URL(location)));
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String []s)
	{
		new VcodeFrame();
	}

	public String getVcode()
	{
		isOver = false;
		text.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyTyped(KeyEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				int k = e.getKeyCode();
		        if (k == KeyEvent.VK_ENTER) {
		        	VcodeFrame.isOver = true;
		        }
			}
		});
		while(!isOver)
		{
			ThreadUtil.sleep(300);
		}
		String result = text.getText();
		text.setText("");
		return result;
	}
}
