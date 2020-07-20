import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * FileHandler get input string path and return list of file(s) to navigate
 */
public class FileHandler {
    String path;

    public FileHandler(){
        this.path = null;
    }
    public FileHandler(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<File> getFileList(){
        List<File> fileList = new ArrayList<File>();
        try {
            File pathFile = new File(path);
            // check if path argument is file or folder, if folder get list of files (non-recursive by design)
            if (pathFile.isDirectory()){
                for (File file : pathFile.listFiles())
                    fileList.add(file);
            }
            else{
                fileList.add(pathFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return fileList;
        }
    }
}
