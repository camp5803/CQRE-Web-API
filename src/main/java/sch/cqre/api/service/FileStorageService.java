package sch.cqre.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sch.cqre.api.config.FileStorageConfig;
import sch.cqre.api.domain.FileEntity;
import sch.cqre.api.exception.FileStorageException;
import sch.cqre.api.repository.FileDAO;
import sch.cqre.api.repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileDAO fileDAO;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig, FileDAO fileDAO) {
        //init
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
        this.fileDAO = fileDAO;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            throw new FileStorageException("invaildInput");
        }



        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        UUID uuid = UUID.randomUUID();
        String randomUUID = uuid.toString();

        Path targetLocation = this.fileStorageLocation.resolve(randomUUID);
        File Folder = new File(String.valueOf(targetLocation));

        uuid_collision:
        while(true) { //???????????? uuid??? ?????? ??? ????????????
            // ?????? ??????????????? ???????????? ??????????????? ???????????????.
            if (!Folder.exists()) {
                try {
                    Folder.mkdir(); //?????? ???????????????.
                    break;
                } catch (Exception e) {
                    throw new FileStorageException("uncheckedError", e);
                }
            } else { //uuid??? ?????????
                uuid = UUID.randomUUID(); //uuid ?????????
                randomUUID = uuid.toString();
                targetLocation = this.fileStorageLocation.resolve(randomUUID);
                Folder = new File(String.valueOf(targetLocation));
                break uuid_collision; //?????? ??????
            }
        }

        try {

            targetLocation = targetLocation.resolve(fileName);

            //log.warn("a " + fileName);

            //????????? ??????
            if (!chkAllowedExtension(extension))
                throw new FileStorageException("notSupportFileFormat");

            Path formac = Paths.get(fileName); //????????? ???????????? ????????? ???????????? ??????????????? ??????

            // Copy file to the target location (Replacing existing file with the same name)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // db??? ??????
            fileDAO.addFile(String.valueOf(formac), String.valueOf(targetLocation),
                    Long.valueOf(file.getSize()).intValue(), extension);

            return randomUUID;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }



    public boolean chkAllowedExtension(String extension){
        String[] allowedExtension = {"zip", "alz", "tar", //????????????
                "jpeg", "jpg", "bmp", "png", "gif", "tif", //?????????
                "hwp", "ppt", "pptx", "doc", "xls", "xlsx", "pdf", "txt" //??????
        };

        for (int i=0; i<allowedExtension.length; i++) {
            if (Objects.equals(extension, allowedExtension[i]))
                return true;
        }
        return false;
    }

    public Resource loadFileAsResource(String fileUUID) {
        // ????????? ????????? null,
        // ????????? ????????? Resource??? ??????

        FileEntity fileDB = fileDAO.getFileDB(fileUUID);

        if (fileDB == null)
            return null;

        String filePathString = fileDB.getFilepath();
        String fileExtension = fileDB.getFiletype(); //not uses yet

        try {
            //??????path??? ??? ??????????????? null??????
            if (filePathString.isBlank())
                return null;

            Path filePath = this.fileStorageLocation.resolve(filePathString).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else { //????????? ????????? null??????
                return null;
            }
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}
