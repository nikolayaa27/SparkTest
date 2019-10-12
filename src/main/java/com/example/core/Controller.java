package com.example.core;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class Controller {

    Logger log = LoggerFactory.getLogger(Controller.class);

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody
    String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file) {

        String contentType = file.getContentType();
        String fileContent = null;
        XmlDto data = null;
        try {
            fileContent = new String(file.getBytes(), "UTF-8");
        } catch (IOException e) {
            log.error(e.getStackTrace().toString());
            return "File cannot be read";
        }

        try {
            data = deserializeXml(fileContent);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            return "XML Structrure is not valid";
        }

        if (!validateData(data)) {
            return "Oops! One of the mandatory fields is empty...";
        }

        UUID uuid = UUID.randomUUID();


        log.info(data.getLastName());
        log.info(data.getFirstName());
        log.info(data.getEmail());
        log.info(data.getHabbits());


//        if (!file.isEmpty()) {
//            try {
//                byte[] bytes = file.getBytes();
//                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
//                stream.write(bytes);
//                stream.close();
//
//                log.info(fileContent);
//                return "Вы удачно загрузили " + name + " в " + name + "-uploaded !";
//            } catch (Exception e) {
//                return "Вам не удалось загрузить " + name + " => " + e.getMessage();
//            }
//        } else {
//            return "Вам не удалось загрузить " + name + " потому что файл пустой.";
//        }
        return uuid.toString();
    }

    //TODO: it is not beautiful - better use Predicates interfacecp
    private boolean validateData(XmlDto data) {
        if (data.getLastName().isEmpty()
                || data.getFirstName().isEmpty()
                || data.getEmail().isEmpty()
                || data.getHabbits().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private XmlDto deserializeXml(String xml) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        XmlDto data = xmlMapper.readValue(xml, XmlDto.class);
        return data;
    }


}
