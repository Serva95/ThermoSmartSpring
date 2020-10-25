package it.srv.ThermoSmartSpring.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AVGDTO {
    BigDecimal getTemp();
    LocalDate getGiorno();
}
