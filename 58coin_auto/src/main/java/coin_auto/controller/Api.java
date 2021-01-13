package coin_auto.controller;

import coin_auto.app.AppInit;
import coin_auto.utils.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class Api {

    @Autowired
    private AppInit appInit;

    @Autowired
    private SendMail sendMail;

    @RequestMapping(value = "/init",method = RequestMethod.GET)
    public void init(){

        appInit.init();
    }

    @RequestMapping(value = "/sendMail",method = RequestMethod.GET)
    public void setSendMail(){
        sendMail.send();
    }
}
