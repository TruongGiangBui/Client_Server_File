import com.sun.security.jgss.GSSUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileServices {

    public static void delete(File file) {

        if (file.isDirectory()) {
            // directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());
            } else {
                // list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    // construct the file structure
                    File fileDelete = new File(file, temp);

                    // recursive delete
                    delete(fileDelete);
                }

                // check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        } else {
            // if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
    public static void copyDir(File src,File dest) throws IOException {
        if(!dest.exists())
        {
            dest.mkdir();
        }
        if(!src.exists())
        {
            throw new IllegalArgumentException("not found source");
        }
        docopydir(src,dest);
    }
    public static void docopydir(File src,File dest) throws IOException {
        File[] items=src.listFiles();
        if(items!=null&&items.length>0)
        {
            for(File anitem:items)
            {
                if(anitem.isDirectory())
                {
                    File newdir=new File(dest,anitem.getName());
                    System.out.println("create dir" +newdir.getAbsolutePath());
                    newdir.mkdir();
                    docopydir(anitem,newdir);
                }else
                {
                    File destfile=new File(dest,anitem.getName());
                    docopyfile(anitem,destfile);
                }
            }
        }
    }
    public static void docopyfile(File src,File dest) throws IOException {
        if(!dest.exists())
        {
            dest.createNewFile();
        }
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try{
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest);

            sourceChannel = fis.getChannel();
            destChannel = fos.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
        }
    }


    public static void process(String response,String dir2) throws IOException {
        String[] cmd=response.split(";");
        String subdir=cmd[3];
        if(cmd[2].equals("ENTRY_CREATE"))
        {
            String src=cmd[1]+subdir;
            String dest=dir2 +subdir;
            copyDir(new File(src),new File(dest));
        }
        else if(cmd[2].equals("ENTRY_DELETE"))
        {
            String dest=dir2 +subdir;
            delete(new File(dest));
        }
        else if(cmd[2].equals("ENTRY_MODIFY"))
        {
            String src=cmd[2]+subdir;
            String dest=dir2+subdir;
            docopyfile(new File(src),new File(dest));
        }
    }
}
