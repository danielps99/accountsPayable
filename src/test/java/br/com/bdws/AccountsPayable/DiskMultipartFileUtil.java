package br.com.bdws.AccountsPayable;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

public class DiskMultipartFileUtil implements MultipartFile {

    private final File file;
    private final String name;
    private final byte[] content;

    public DiskMultipartFileUtil(File file, String name) throws IOException {
        this.file = file;
        this.name = name;
        this.content = Files.readAllBytes(file.toPath());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        // Optional: return content type (mime type) based on file extension
        return "application/octet-stream"; // For example, use proper content type
    }

    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.copy(file.toPath(), dest.toPath());
    }
}