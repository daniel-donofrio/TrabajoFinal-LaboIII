package ar.edu.utn.frbb.tup.servicios;

import ar.edu.utn.frbb.tup.persistencia.MovimientosDao;
import org.springframework.stereotype.Component;

@Component
public class ServicioMovimientos {
    MovimientosDao movimientosDao = new MovimientosDao();

    public void inicializarMovimientos() {
        movimientosDao.inicializarMovimientos();
    }

}
