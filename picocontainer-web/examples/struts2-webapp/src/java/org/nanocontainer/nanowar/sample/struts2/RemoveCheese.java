package org.nanocontainer.nanowar.sample.struts2;

import com.opensymphony.xwork2.ActionSupport;
import org.nanocontainer.nanowar.sample.model.Cheese;
import org.nanocontainer.nanowar.sample.service.CheeseService;

public class RemoveCheese extends ActionSupport {

    private Cheese cheese = new Cheese();
    private CheeseService cheeseService;

    public RemoveCheese(CheeseService cheeseService) {
        this.cheeseService = cheeseService;
    }

    public Cheese getCheese() {
        return cheese;
    }


    public String execute() throws Exception {
        cheeseService.remove(cheese);
        return SUCCESS;
    }

}
