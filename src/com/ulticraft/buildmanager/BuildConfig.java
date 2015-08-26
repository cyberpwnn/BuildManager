package com.ulticraft.buildmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.ulticraft.buildmanager.group.Build;
import com.ulticraft.buildmanager.group.BuildGroup;

public class BuildConfig
{
	private File file;
	private File xlm;
	
	public BuildConfig(BuildManager plugin)
	{
		file = new File(plugin.getDataFolder(), "builds.bmc");
		xlm = new File(plugin.getDataFolder(),  "builds.xls");
		
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
		
		outputSpreadsheet(plugin);
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
	
	public void outputSpreadsheet(BuildManager bc)
	{
		try
		{
			WritableWorkbook workbook = Workbook.createWorkbook(xlm);
			WritableSheet sheet = workbook.createSheet("Builds", 0);
			
			int row = 1;
			
			sheet.addCell(new Label(0, 0, "Name"));
			sheet.addCell(new Label(1, 0, "Description"));
			sheet.addCell(new Label(2, 0, "ID"));
			sheet.addCell(new Label(3, 0, "Deadline"));
			
			for(Build i : bc.getBuildGroup().getBuilds())
			{
				sheet.addCell(new Label(0, row, i.getTitle()));
				sheet.addCell(new Label(1, row, i.getDescription()));
				sheet.addCell(new jxl.write.Number(2, row, i.getId()));
				sheet.addCell(new Label(3, row, i.getDeadline().format()));
				
				row++;
			}
			
			workbook.write(); 
			workbook.close();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		catch(RowsExceededException e)
		{
			e.printStackTrace();
		}
		
		catch(WriteException e)
		{
			e.printStackTrace();
		}
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
