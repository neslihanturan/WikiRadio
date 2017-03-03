package wikiradio.neslihan.tur.org.wikiradio.data;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by nesli on 28.02.2017.
 */

public class FileUtils {
    public static void saveToFile(HashSet<String> categorySet, String path, Context context){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(context.getFilesDir()+path));
            Iterator it = categorySet.iterator();
            while(it.hasNext()) {
                out.write(it.next()+"\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashSet<String> readFromFile(Context context) {
        Scanner file = null;
        try {
            File f = new File(context.getFilesDir()+"categoryset.txt");
            //f.createNewFile(); //if file didnt exist, it will create. Otherwise does nothing
            file = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashSet<String> categorySet = new HashSet<>();
        // For each word in the input
        while (file.hasNext()) {
            categorySet.add(file.nextLine());
        }
        return categorySet;
    }


}
