package com.example.core;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
public class Controller {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataRepositoryJournal dataRepositoryJournal;


    Logger log = LoggerFactory.getLogger(Controller.class);
    private Object Error;

    @RequestMapping("/index")
    public ModelAndView index () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping("/login")
    public ModelAndView login () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login.html");
        return modelAndView;
    }


    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public @ResponseBody
    String handleSignIn(@RequestParam("username") String username,
                        @RequestParam("password") String pass) {

        Optional<User> byEmailAndPassword = dataRepository.findByEmailAndPassword(username, pass);
        if (byEmailAndPassword.isPresent()) {
            //TODO: here you need to add the code for the new page



            return "complete";
        } else
        {

            String error = "unauthorized access attempt";
            Error err = new Error(error);
            dataRepositoryJournal.save(err);

            return error;
        }



    }



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

        String error = null;
        try {
            fileContent = new String(file.getBytes(), "UTF-8");
        } catch (IOException e) {
            log.error(e.getStackTrace().toString());
            error = "File cannot be read";
        }

        try {
            data = deserializeXml(fileContent);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            error = "XML Structrure is not valid";
        }

        if (!validateData(data)) {
            error = "Oops! One of the mandatory fields is empty...";
        }

        if (error != null) {
            // put error record into DB journal table
            Error err = new Error(error);
            dataRepositoryJournal.save(err);
            // return status to user
            return error;
        }

        User user = new User(data);
        dataRepository.save(user);


        log.info(data.getLastName());
        log.info(data.getFirstName());
        log.info(data.getEmail());
        log.info(data.getHabbits());



        return user.getPassword();
    }

    //TODO: it is not beautiful - better use Predicates interfacecp
    private boolean validateData(XmlDto data) {
        return !data.getLastName().isEmpty()
                && !data.getFirstName().isEmpty()
                && !data.getEmail().isEmpty()
                && !data.getHabbits().isEmpty();
    }

    private XmlDto deserializeXml(String xml) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml, XmlDto.class);
    }


}
