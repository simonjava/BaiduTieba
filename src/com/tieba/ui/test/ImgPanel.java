package com.tieba.ui.test;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgPanel extends Panel
{
	String path;
	public ImgPanel(String path)
	{
		this.path = path;
	}
	@Override
	public void paint(Graphics g)
	{
	
		super.paint(g);
		Image img =
		null;
		try
		{
			img = ImageIO.read(new FileInputStream(path));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, null);
	}

}
