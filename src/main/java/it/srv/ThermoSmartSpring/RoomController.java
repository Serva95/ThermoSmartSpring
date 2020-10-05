package it.srv.ThermoSmartSpring;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RoomController {

    @GetMapping("/rooms")
    public ModelAndView Rooms(ModelAndView mav) {
        return mav;
    }

    @GetMapping("/rooms/{id}")
    public ModelAndView viewRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PostMapping("/rooms/{id}")
    public ModelAndView createRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PutMapping("/rooms/{id}")
    public ModelAndView updateRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @DeleteMapping("/rooms/{id}")
    public ModelAndView deleteRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
