package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MvcController {

    @Autowired
    private RoomDAO roomDAO;

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        Iterable<Room> listRooms = roomDAO.listAll();
        List<Room> roomList = new ArrayList<>();
        listRooms.forEach(roomList::add);
        model.addAttribute("listRooms", roomList);

        return "index";
    }

}