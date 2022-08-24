package com.fake.smtp.controller;

import com.fake.smtp.model.Email;
import com.fake.smtp.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class EmailController {

    private static final Sort DEFAULT_SORT =  Sort.by(Sort.Direction.DESC, "receivedOn");
    private static final int DEFAULT_PAGE_SIZE = 10;
    static final String APP_VERSION_MODEL_NAME = "appVersion";
    static final String EMAIL_LIST_VIEW = "email-list";
    static final String EMAIL_LIST_MODEL_NAME = "mails";
    static final String SINGLE_EMAIL_VIEW = "email";
    static final String SINGLE_EMAIL_MODEL_NAME = "mail";
    static final String REDIRECT_EMAIL_LIST_VIEW = "redirect:/email";
    static final String LOGIN_PAGE = "login";

    private final EmailRepository emailRepository;
    private final BuildProperties buildProperties;

    @Autowired
    public EmailController(EmailRepository emailRepository, BuildProperties buildProperties) {
        this.emailRepository = emailRepository;
        this.buildProperties = buildProperties;
    }

    @GetMapping("/email")
    public String getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size, Model model) {
        return getAllEmailsPaged(page, size, model);
    }

    private String getAllEmailsPaged(int page, int size, Model model) {
        if(page < 0 || size <= 0){
            return REDIRECT_EMAIL_LIST_VIEW;
        }
        var result = emailRepository.findAll(PageRequest.of(page, size, DEFAULT_SORT));
        if (result.getNumber() != 0 && result.getNumber() >= result.getTotalPages()) {
            return REDIRECT_EMAIL_LIST_VIEW;
        }
        model.addAttribute(EMAIL_LIST_MODEL_NAME, result);
        addApplicationVersion(model);
        return EMAIL_LIST_VIEW;
    }

    @GetMapping("/email/{id}")
    @Transactional
    public String getEmailById(@PathVariable Long id, Model model) {
        return emailRepository.findById(id).map(email -> appendToModelAndReturnView(model, email)).orElse(REDIRECT_EMAIL_LIST_VIEW);
    }

    private String appendToModelAndReturnView(Model model, Email email) {
        model.addAttribute(SINGLE_EMAIL_MODEL_NAME, email);
        addApplicationVersion(model);
        return SINGLE_EMAIL_VIEW;
    }

    @DeleteMapping("/email/{id}")
    public String deleteEmailById(@PathVariable Long id) {
        emailRepository.deleteById(id);
        emailRepository.flush();
        return REDIRECT_EMAIL_LIST_VIEW;
    }

    @DeleteMapping("/email")
    public String deleteAllEmails() {
        emailRepository.deleteAll();
        emailRepository.flush();
        return REDIRECT_EMAIL_LIST_VIEW;
    }

    @GetMapping("/")
    public String showLoginPage(){
        return LOGIN_PAGE;
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        boolean result = authCredential(email, password);

        if (result) {
            return REDIRECT_EMAIL_LIST_VIEW;
        } else {
            model.addAttribute("errorMsg","Username or Password is wrong!");
            return LOGIN_PAGE;
        }
    }

    private void addApplicationVersion(Model model){
        model.addAttribute(APP_VERSION_MODEL_NAME, buildProperties.getVersion());
    }

    private boolean authCredential(String email, String password){
        return email.equalsIgnoreCase(password);
    }

}
