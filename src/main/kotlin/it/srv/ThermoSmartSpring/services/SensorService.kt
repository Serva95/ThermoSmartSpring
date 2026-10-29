package it.srv.ThermoSmartSpring.services

import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.model.Sensor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class SensorService(
    private val sensorDAO: SensorDAO
) {

    @Throws(ObjectAlreadyExistException::class, BlankFieldsException::class)
    open fun saveNewSensor(sensor: Sensor): Sensor {
        if (sensor.id.isEmpty() || sensor.location.isEmpty() || sensor.nome.isEmpty()) {
            throw BlankFieldsException("Completa tutti i campi")
        }
        if (sensorDAO.exists(sensorDAO.get(sensor.id))) {
            throw ObjectAlreadyExistException("Esiste già un sensore con questo id, provane uno diverso.")
        }
        return sensorDAO.save(sensor)
    }

    @Throws(BlankFieldsException::class, ObjectAlreadyExistException::class)
    open fun updateSensor(oldSensor: Sensor, newData: Sensor): Sensor {
        if (newData.id.isEmpty() || newData.location.isEmpty() || newData.nome.isEmpty()) {
            throw BlankFieldsException("Completa tutti i campi")
        }
        if (!oldSensor.id.equals(newData.id, ignoreCase = true) && sensorDAO.exists(sensorDAO.get(newData.id))) {
            throw ObjectAlreadyExistException("Esiste già un sensore con questo id, provane uno diverso.")
        }
        if (oldSensor.id != newData.id) {
            sensorDAO.update(newData, oldSensor.id)
        } else {
            sensorDAO.save(newData)
        }
        return newData
    }
}
