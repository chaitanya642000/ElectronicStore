package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ExceptionDuringFolderCreation;
import com.lcwd.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Implementation of Fileupload Sevice
 * */
@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
       String originalFileName =  file.getOriginalFilename();
       /**
        * random to generate unique as two files may have same name
        * */
       String fileName = UUID.randomUUID().toString();
       String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
       String fileNameWithExtension = fileName+extension;

       String fullPathWithFileName = path+ File.separator+fileNameWithExtension;


       if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") ||
           extension.equalsIgnoreCase(".jpeg"))
        {

            File folder = new File(path);

           if(!folder.exists())
           {
               boolean foldercreated = folder.mkdirs();
               if(!foldercreated)
               {
                   throw new ExceptionDuringFolderCreation("Folder not created ");
               }
           }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

        }else
        {
            throw new BadApiRequest("Invalid extension for uploaded file");
        }

       return fileNameWithExtension;

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path+File.separator+name;
        InputStream stream = new FileInputStream(fullPath);
        return stream;
    }
}
