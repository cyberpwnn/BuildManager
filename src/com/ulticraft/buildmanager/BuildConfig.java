package com.ulticraft.buildmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.ulticraft.buildmanager.group.BuildGroup;

public class BuildConfig
{
	private File file;
	
	public BuildConfig(BuildManager plugin)
	{
		file = new File(plugin.getDataFolder(), "builds.bmc");
		
		if(!plugin.getDataFolder().exists())
		{
			plugin.getDataFolder().mkdir();
		}
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			save(new BuildGroup());
		}
	}
	
	public void save(BuildGroup bh)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);

			oos.writeObject(bh);

			oos.close();

		}

		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public BuildGroup load()
	{
		try
		{
			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fin);

			BuildGroup tbm = (BuildGroup) ois.readObject();
			
			ois.close();
			
			return tbm;
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}

	public static void copyFile(InputStream in, File out) throws Exception
	{
		InputStream fis = in;
		FileOutputStream fos = new FileOutputStream(out);
		try
		{
			byte[] buf = new byte[1024];
			int i = 0;
			while((i = fis.read(buf)) != -1)
			{
				fos.write(buf, 0, i);
			}
		}

		catch(Exception e)
		{
			throw e;
		}

		finally
		{
			if(fis != null)
			{
				fis.close();
			}

			if(fos != null)
			{
				fos.close();
			}
		}
	}
}
