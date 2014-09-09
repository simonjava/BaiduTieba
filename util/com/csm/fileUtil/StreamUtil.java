package com.csm.fileUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class StreamUtil {

	public static String readInputStream(InputStream is) {
		return readInputStream(is, "utf-8");
	}

	public static String readInputStream(InputStream is, String charsetName) {
		byte[] content = readBinaryStream(is);
		return Charset.forName(charsetName).decode(ByteBuffer.wrap(content))
				.toString();
	}

	public static byte[] readBinaryStream(InputStream is) {
		try {
			List<byte[]> ls = new ArrayList<byte[]>();
			int l = 0;
			int capacity = 0;
			while (l != -1) {
				byte[] tmp = new byte[is.available()];
				l = is.read(tmp);
				ls.add(tmp);
				capacity += tmp.length;
			}
			byte[] content = new byte[capacity];
			int k = 0;
			for (byte[] tmp : ls) {
				for(byte b:tmp)
				{
					content[k++] = b;
				}
			}
			return content;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public static void writeOutputStream(OutputStream os, String content) {
		writeOutputStream(os, content, "utf-8");
		return;
	}

	public static void writeOutputStream(OutputStream os, String content,
			String charsetName) {
		try {
			os.write(Charset.forName(charsetName).encode(content).array());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
