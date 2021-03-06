package sch.cqre.api.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sch.cqre.api.domain.FileEntity;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FileDAO {
    private final FileRepository fileRepository;

    @Transactional
    public Long addFile(String orignalName, String filepath, int size, String filetype){
        //db에 파일정보 저장
        FileEntity fileForm = new FileEntity();
        fileForm.setOriginalname(orignalName);
        fileForm.setFilepath(filepath);
        fileForm.setSize(size);
        fileForm.setFiletype(filetype);

        return fileRepository.save(fileForm).getFileId();
    }

    @Transactional
    public String getFilePath(String fileUUID){
        return "";
    }

    public FileEntity getFileDB(String fileUUID){
        return fileRepository.findOneByfilepathIsContaining(fileUUID);

    }

}
