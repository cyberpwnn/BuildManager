package com.ulticraft.buildmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.ulticraft.buildmanager.group.Build;
import com.ulticraft.buildmanager.group.BuildGroup;
import com.ulticraft.buildmanager.group.Builder;
import com.ulticraft.buildmanager.group.Manager;

public class BuildConfig
{
	private File file;
	private File xlm;

	public BuildConfig(BuildManager plugin)
	{
		file = new File(plugin.getDataFolder(), "builds.bmc");
		xlm = new File(plugin.getDataFolder(), "builds.xls");

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
			GZIPOutputStream gz = new GZIPOutputStream(fout);
			ObjectOutputStream oos = new ObjectOutputStream(gz);

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
			GZIPInputStream gis = new GZIPInputStream(fin);
			ObjectInputStream ois = new ObjectInputStream(gis);

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
	
	public void outputSpreadsheet(BuildGroup bc)
	{
		try
		{
			WritableWorkbook workbook = Workbook.createWorkbook(xlm);
			WritableSheet sheet = workbook.createSheet("Builds", 0);

			int row = 1;

			sheet.addCell(new Label(0, 0, "Build Name"));
			sheet.addCell(new Label(1, 0, "Build Description"));
			sheet.addCell(new Label(2, 0, "Build ID"));
			sheet.addCell(new Label(3, 0, "Deadline"));
			sheet.addCell(new Label(4, 0, "Managers"));
			sheet.addCell(new Label(5, 0, "Builders"));
			sheet.addCell(new Label(6, 0, "Completed"));

			for(Build i : bc.getBuilds())
			{
				sheet.addCell(new Label(0, row, i.getTitle()));
				sheet.addCell(new Label(1, row, i.getDescription()));
				sheet.addCell(new jxl.write.Number(2, row, i.getId()));

				if(i.getDeadline() != null)
				{
					sheet.addCell(new Label(3, row, i.getDeadline().format()));
				}

				else
				{
					sheet.addCell(new Label(3, row, "No DL"));

				}

				String f = "";

				for(Manager j : bc.getManagers(i.getId()))
				{
					f = f + ", " + j.getName();
				}

				if(f.length() > 2)
				{
					f = f.substring(2);
				}

				sheet.addCell(new Label(4, row, f));

				f = "";

				for(Builder j : bc.getBuilders(i.getId()))
				{
					f = f + ", " + j.getName();
				}

				if(f.length() > 2)
				{
					f = f.substring(2);
				}
				
				String per = "";
				
				if(i.isFinished())
				{
					per = "Finished";
				}
				
				else
				{
					per = i.getCompleted() + "%";
				}

				sheet.addCell(new Label(5, row, f));
				sheet.addCell(new Label(6, row, per));
				
				
				row++;
			}

			for(int x = 0; x < sheet.getColumns(); x++)
			{
				CellView cell = sheet.getColumnView(x);
				cell.setAutosize(true);
				sheet.setColumnView(x, cell);
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
