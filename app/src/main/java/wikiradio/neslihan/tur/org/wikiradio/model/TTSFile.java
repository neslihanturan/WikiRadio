package wikiradio.neslihan.tur.org.wikiradio.model;

import java.io.FileDescriptor;

/**
 * Created by nesli on 04.03.2017.
 */

public class TTSFile extends SourceFile {
    //TODO: burada kaldın
    private FileDescriptor fileDescriptor;
    private String fileName;
    private String extract;
    private String thumbnailSource;
    private int thumbnailHeight;
    private int thumbnailWidth;



    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getThumbnailSource() {
        return thumbnailSource;
    }

    public void setThumbnailSource(String thumbnailSource) {
        this.thumbnailSource = thumbnailSource;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }


}
