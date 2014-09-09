package com.csm.common;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;

/**
 * @author simin 宸ュ叿绫�鐢ㄤ簬瀛楃鏂囦欢鍜屼簩杩涘埗鏂囦欢鐨勮鍐欐搷浣� */
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
	 * @param file 鏂囦欢
	 * @return 鏂囦欢鐨勫瓧鑺傛暟缁�	 */
	public static byte[] readBinary(File file)
	{
		try
		{
			FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
			// 鐢变簬int鍜宭ong璇诲彇瓒呭ぇ鏂囦欢鍙兘浼氬嚭閿�			
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
		ByteBuffer bb = Charset.forName(charsetName).encode(content);
		writeBinary(filePath, bb.array(), addition);
	}

	public static void writeText(File file, String content, String charsetName,
			boolean addition)
	{
		ByteBuffer bb = Charset.forName(charsetName).encode(content);
		writeBinary(file, bb.array(), addition);
	}

	public static void writeBinary(String filePath, byte[] content,
			boolean addition)
	{
		writeBinary(new File(filePath), content, addition);
	}

	/**
	 * @param file 鏂囦欢
	 * @param content 鍐呭
	 * @param addition 鏄惁涓鸿拷鍔犲啓
	 */
	public static void writeBinary(File file, byte[] content, boolean addition)
	{
		try
		{
			// 濡傛灉涓嶆槸杩藉姞鍐�鍒欏垹闄ゅ師鏉ョ殑鏂囦欢
			if (!addition)
			{
				file.delete();
			}
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
			long position = fc.size(), size = content.length;
			if (!addition)
			{
				position = 0;
			}
			// 杩欓噷鐨剆ize鏄粠position寰�悗璁＄畻鐨剆ize
			MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, position, size);
			mbb.put(content);
			fc.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException
	{
		writeText("f:\\JavaTest\\MapTest.txt", "aaa鎴戞槸澧炲姞鐨刓n", true);
		System.out.println(readText("f:\\JavaTest\\MapTest.txt", "utf-8"));
		FileInputStream fis = new FileInputStream("f:\\JavaTest\\img.png");
		byte[] b = new byte[fis.available()];
		fis.read(b);
		fis.close();
		writeBinary("f:\\JavaTest\\newimg.png", b, false);
	}
}
