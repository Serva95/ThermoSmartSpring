package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.TempDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TempService {
    @Autowired
    TempDAO tempDAO;

}
