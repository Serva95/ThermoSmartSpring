package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MvcController {

    @Autowired
    private RoomDAO roomDAO;

    @GetMapping("/")
    public ModelAndView viewHomePage(ModelAndView mav) {
        Iterable<Room> listRooms = roomDAO.getAll();
        List<Room> roomList = new ArrayList<>();
        listRooms.forEach(roomList::add);
        mav.setViewName("index");
        mav.addObject("listRooms", roomList);

        return mav;
    }

}