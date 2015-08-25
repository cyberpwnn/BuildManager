package com.ulticraft.buildmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.ulticraft.buildmanager.buildcollaboration.BuildCollaboration;

public class BuildConfig
{
	private File file;
	private BuildManager plugin;

	public BuildConfig(BuildManager plugin)
	{
		file = new File(plugin.getDataFolder(), "builds.bmc");
		this.plugin = plugin;
		
		if(file.exists())
		{
			BuildCollaboration bm = load();
			
			if(bm != null)
			{
				plugin.setBuildCollaboration(bm);
			}
		}

		else
		{
			save();
		}
	}
	
	public void save()
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);

			oos.writeObject(plugin.getBuildCollaboration());

			oos.close();

		}

		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public BuildCollaboration load()
	{
		try
		{

			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fin);

			BuildCollaboration tbm = (BuildCollaboration) ois.readObject();
			
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
