package com.csm.fileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;

import com.tieba.config.TiebaConfig;

/**
 * @author simin 工具类,用于字符文件和二进制文件的读写操作
 */
public class FileIOUtil
{

	private FileIOUtil()
	{
	}

	public static String readText(String filePath)
	{
		return readText(filePath, "utf-8");
	}

	public static String readText(File file)
	{
		return readText(file, "utf-8");
	}

	
	public static String readText(String filePath, String charsetName)
	{
		byte[] content = readBinary(filePath);
		return Charset.forName(charsetName).decode(ByteBuffer.wrap(content))
				.toString();
	}

	public static String readText(File file, String charsetName)
	{
		byte[] content = readBinary(file);
		return Charset.forName(charsetName).decode(ByteBuffer.wrap(content))
				.toString();
	}

	public static byte[] readBinary(String filePath)
	{
		return readBinary(new File(filePath));
	}

	/**
	 * @param file 文件
	 * @return 文件的字节数组
	 */
	public static byte[] readBinary(File file)
	{
		try
		{
			FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
			// 由于int和long读取超大文件可能会出错
			MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, fc.size());
			byte[] content = new byte[mbb.capacity()];
			mbb.get(content);
			fc.close();
			return content;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	
	public static void writeText(String filePath, String content)
	{
		writeText(filePath, content, "utf-8", false);
	}

	public static void writeText(File file, String content)
	{
		writeText(file, content, "utf-8", false);
	}

	public static void writeText(String filePath, String content,
			String charsetName)
	{
		writeText(filePath, content, charsetName, false);
	}

	public static void writeText(File file, String content, String charsetName)
	{
		writeText(file, content, charsetName, false);
	}

	public static void writeText(String filePath, String content,
			boolean addition)
	{
		writeText(filePath, content, "utf-8", addition);
	}

	public static void writeText(File file, String content, boolean addition)
	{
		writeText(file, content, "utf-8", addition);
	}

	public static void writeText(String filePath, String content,
			String charsetName, boolean addition)
	{
		writeText(new File(filePath), content,charsetName, addition);
	}

	public static void writeText(File file, String content, String charsetName,
			boolean addition)
	{
		
		try
		{
			writeBinary(file, content.getBytes(charsetName), addition);
		} catch (UnsupportedEncodingException e)
		{
				e.printStackTrace();
		}
	}

	public static void writeBinary(String filePath, byte[] content,
			boolean addition)
	{
		writeBinary(new File(filePath), content, addition);
	}

	/**
	 * @param file 文件
	 * @param content 内容
	 * @param addition 是否为追加写
	 */
	public static void writeBinary(File file, byte[] content, boolean addition)
	{
		try
		{
			// 如果不是追加写,则删除原来的文件
			if (!addition)
			{
				file.delete();
			}
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(file, addition);
			fos.write(content);
			fos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException
	{
		String str = "http://22222\n";
		writeBinary("/home/csmubuntu/JavaTest/MapTest2.txt", str.getBytes(), true);
	
	}
}
