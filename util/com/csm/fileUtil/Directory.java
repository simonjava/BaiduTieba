package com.csm.fileUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author simin 工具类 ，返回文件目录
 */
public final class Directory
{
	
	private Directory()
	{}
	
	/**
	 * @param dir
	 * @param regex
	 * @return 本地目录结构
	 */
	public static File[] local(File dir, final String regex)
	{
		return dir.listFiles(new FilenameFilter()
		{

			@Override
			public boolean accept(File dir, String name)
			{
				return name.matches(regex);
			}
		});
	}

	public static File[] local(String filePath, String regex)
	{
		return local(new File(filePath), regex);
	}

	public static File[] local(File dir)
	{
		return local(dir, ".*");
	}

	public static File[] local(String filePath)
	{
		return local(filePath, ".*");
	}

	/**
	 * @author simin 存储用的数据结构
	 */
	public static class TreeInfo implements Iterable<File>
	{
		public List<File> files = new ArrayList<File>();
		public List<File> dirs = new ArrayList<File>();

		@Override
		public Iterator<File> iterator()
		{
			return files.iterator();
		}

		void addAll(TreeInfo other)
		{
			files.addAll(other.files);
			dirs.addAll(other.dirs);
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("dirs:\n");
			for(String s:dirs.toString().split("[\\[,\\]]"))
			{
				sb.append(s);
				sb.append("\n");
			}
			sb.append("files:\n");
			for(String s:files.toString().split("[\\[,\\]]"))
			{
				sb.append(s);
				sb.append("\n");
			}
			return sb.toString();
		}
	}

	static TreeInfo recurseDirs(File startDir, String regex)
	{
		TreeInfo result = new TreeInfo();
		//这样写第一个目录就被屏蔽掉了 而屏蔽只针对文件不针对目录
//		for (File file : local(startDir, regex))
		for (File file : startDir.listFiles())
		{
			if (file.isDirectory())
			{
				result.dirs.add(file);
				result.addAll(recurseDirs(file, regex));
			} else
			{
				if(file.getName().matches(regex))
					result.files.add(file);
			}
		}
		return result;
	}

	public static TreeInfo walk(File startDir, String regex)
	{
		return recurseDirs(startDir, regex);
	}

	public static TreeInfo walk(String startDir, String regex)
	{
		return recurseDirs(new File(startDir), regex);
	}

	public static TreeInfo walk(File startDir)
	{
		return recurseDirs(startDir, ".*");
	}

	public static TreeInfo walk(String startDir)
	{
		return recurseDirs(new File(startDir), ".*");
	}

	/**
	 * @param dir 要创建的目录，如果目录已经存在，返回
	 */
	public static void creatDirector(String dir)
	{
		File file = new File(dir);
		if(!file.isDirectory())
			file.mkdirs();
	}
	public static void main(String[]a)
	{
		creatDirector("/home/csmubuntu/AIGames/othello/source/");
	}
}
