package com.lcwd.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/*Service to upload file*/
public interface FileService {


    /**
     * General method to upload the image file
     * @param file : Multipart file to be uploaded
     * @param path: path where the file needs to be saved
     * @return returns name/url of uploaded image
     */
    String uploadFile(MultipartFile file,String path) throws IOException;

    /**
     * method to return the resource
     * @param path : path from where the resource needs to get extracted
     * @param name : name of the resource
     * */
    InputStream getResource(String path ,String name) throws FileNotFoundException;

}
