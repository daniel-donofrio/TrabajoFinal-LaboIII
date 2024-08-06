package ar.edu.utn.frbb.tup.servicios;

import ar.edu.utn.frbb.tup.excepciones.CuentasVaciasException;
import ar.edu.utn.frbb.tup.modelo.Cliente;
import ar.edu.utn.frbb.tup.modelo.Cuenta;
import ar.edu.utn.frbb.tup.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.presentacion.ValidacionesPresentacion;
import ar.edu.utn.frbb.tup.presentacion.inputs.CuentaInputProcessor;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ServicioCuentas {
    Scanner entrada = new Scanner(System.in);
    CuentaInputProcessor inputcuenta = new CuentaInputProcessor();
    ValidacionesServicios validar = new ValidacionesServicios();
    ValidacionesPresentacion validarEntrada = new ValidacionesPresentacion();
    ServicioClientes buscar = new ServicioClientes();
    CuentaDao cuentaDao = new CuentaDao();
    ClienteDao clienteDao = new ClienteDao();

//    public void findAllCuentas() throws CuentasVaciasException {
//        cuentaDao.findAllCuentas();
//    }

    public void inicializarCuentas() {
        cuentaDao.inicializarCuentas();
    }

    public Cuenta buscarCuentas(Set<Cuenta> cuentas, String cbu){
        for (Cuenta c : cuentas) {
            if (c.getCbu() == Long.parseLong(cbu)) {
                return c;
            }
        }
        return null;
    }

    public void mostrarCuentas(List<Cliente> clientes) throws CuentasVaciasException {
        String dni;
        //pedimos el dni del cliente del que queremos mostrar sus cuentas
        do {
            System.out.print("Ingrese el dni del cliente: ");
            dni = entrada.nextLine();
        } while (!validar.validarDni(dni));
        Cliente cliente = buscar.buscarCliente(clientes, dni);
        //validamos que el cliente exista
        if (cliente == null) {
            System.out.println("Cliente no encontrado, primero debe crearlo");
        } else {
            try {
                //si existe creo una lista y la lleno con las cuentas que posea el cliente
                List<Long> cbuList = cuentaDao.getRelacionesDni(Long.parseLong(dni));
                if (cbuList.isEmpty()) {
                    throw new CuentasVaciasException("El cliente no tiene cuentas registradas.");
                }
                //Muestro las cuentas recorriendo la lista de cbus y mostrando los datos de cada una de ellas
                System.out.println("Cuentas del cliente:");
                for (Long cbu : cbuList) {
                    Cuenta cuenta = cuentaDao.findCuenta(cbu);
                    if (cuenta != null) {
                        System.out.println("CBU: " + cuenta.getCbu());
                        System.out.println("Tipo de cuenta: " + cuenta.getTipoCuenta());
                        System.out.println("Moneda: " + cuenta.getTipoMoneda());
                        System.out.println("Alias: " + cuenta.getAlias());
                        System.out.println("Fecha de creación: " + cuenta.getFechaCreacion());
                        System.out.println("Saldo: " + cuenta.getSaldo());
                        System.out.println("-------------------------");
                    }
                }
            } catch (CuentasVaciasException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void crearCuenta(List<Cliente> clientes){
        String dni;
        do{
            System.out.print("Ingrese el dni del cliente: ");
            dni = entrada.nextLine();
        } while (!validar.validarDni(dni));


        Cliente cliente = buscar.buscarCliente(clientes, dni);

        if (cliente == null) {
            System.out.println("Cliente no encontrado, primero debe crearlo");
        }
        else{
            Cuenta cuenta = inputcuenta.ingresarCuenta();
            cuenta.setDniTitular(Long.parseLong(dni));
            //agrego la cuenta a mi lista de cuentas del cliente
//          cliente.addCuenta(cuenta);
            cuentaDao.saveCuenta(cuenta);
            System.out.println("Cuenta creada con exito: " + cuenta.getAlias());
            System.out.println("CBU: " + cuenta.getCbu());
        }
    }

    public void eliminarCuenta (List<Cliente> clientes){
        String dni;
        do{
            System.out.print("Ingrese el dni del cliente: ");
            dni = entrada.nextLine();
        } while (!validar.validarDni(dni));

        Cliente cliente = buscar.buscarCliente(clientes, dni);

        if (cliente == null) {
            System.out.println("Cliente no encontrado, primero debe crearlo");
        }
        else{
            String cbu;
            do {
                System.out.print("Ingrese el CBU de la cuenta a eliminar: ");
                cbu = entrada.nextLine();
            } while (!validarEntrada.esLong(cbu));

            Cuenta cuenta = cuentaDao.findCuentaDelCliente(Long.parseLong(cbu),Long.parseLong(dni));
//            Cuenta cuenta = buscarCuentas(cliente.getCuentas(), cbu);
            if(cuenta == null) {
                System.out.println("Cuenta no encontrada");
            } else {
                System.out.println("La cuenta " + cuenta.getCbu() + " ha sido eliminada." );
                cuentaDao.deleteCuenta(Long.parseLong(cbu));
//                cliente.getCuentas().remove(cuenta);
            }

        }
    }

    public void consultarSaldo(List<Cliente> clientes) {
        String dni;
        do{
            System.out.print("Ingrese el dni del cliente: ");
            dni = entrada.nextLine();
        } while (!validar.validarDni(dni));

        Cliente cliente = buscar.buscarCliente(clientes, dni);
        if (cliente == null) {
            System.out.println("Cliente no encontrado, primero debe crearlo");
        } else {
            String cbu;
            do {
                System.out.print("Ingrese el CBU de la cuenta: ");
                cbu = entrada.nextLine();
            } while (!validarEntrada.esLong(cbu));

            Cuenta cuenta = buscarCuentas(cliente.getCuentas(), cbu);
            if (cuenta == null) {
                System.out.println("Cuenta no encontrada");
            } else {
                System.out.println("Saldo de cuenta: " + cuenta.getSaldo());
            }
        }
    }

    public void realizarDeposito(List<Cliente> clientes) {
        String dni;
        do{
            System.out.print("Ingrese el dni del cliente: ");
            dni = entrada.nextLine();
        } while (!validar.validarDni(dni));

        Cliente cliente = buscar.buscarCliente(clientes, dni);
        if (cliente == null) {
            System.out.println("Cliente no encontrado, primero debe crearlo");
        } else {
            String cbu;
            do {
                System.out.print("Ingrese el CBU de la cuenta: ");
                cbu = entrada.nextLine();
            } while (!validarEntrada.esLong(cbu));

            Cuenta cuenta = buscarCuentas(cliente.getCuentas(), cbu);
            if (cuenta == null) {
                System.out.println("Cuenta no encontrada");
            } else {
                System.out.print("Ingrese el monto a depositar: ");
                double monto = entrada.nextDouble();
                cuenta.setSaldo(monto);
                System.out.println("Deposito realizado con exito");
                System.out.println("Saldo de cuenta: " + cuenta.getSaldo());
            }
        }
    }

    public void realizarExtraccion(List<Cliente> clientes) {
        String dni;
        do{
            System.out.print("Ingrese el DNI del cliente: ");
            dni = entrada.nextLine();
        } while (!validar.validarDni(dni));

        Cliente cliente = buscar.buscarCliente(clientes, dni);
        if (cliente == null) {
            System.out.println("Cliente no encontrado, primero debe crearlo");
        } else {
            String cbu;
            do {
                System.out.print("Ingrese el CBU de la cuenta: ");
                cbu = entrada.nextLine();
            } while (!validarEntrada.esLong(cbu));
            Cuenta cuenta = buscarCuentas(cliente.getCuentas(), cbu);
            if (cuenta == null) {
                System.out.println("Cuenta no encontrada");
            } else {
                String montoStr;
                double monto;
                do {
                    System.out.print("Ingrese el monto a extraer: ");
                    montoStr = entrada.nextLine();
                } while (!validarEntrada.esDouble(montoStr));
                monto = Double.parseDouble(montoStr);
                if (cuenta.getSaldo() < monto) {
                    System.out.println("Fondos insuficientes");
                } else {
                    cuenta.setSaldo(cuenta.getSaldo() - monto);
                    System.out.println("Extraccion realizada con exito");
                    System.out.println("Saldo de cuenta: " + cuenta.getSaldo());
                }
            }
        }
    }

    public void realizarTransferencia(List<Cliente> clientes) {
        String dniOrigen;
        String dniDestino;
        String cbuOrigen;
        String cbuDestino;
        String montoStr;

        do{
            System.out.print("Ingrese el DNI del cliente origen:  ");
            dniOrigen = entrada.nextLine();
        } while (!validar.validarDni(dniOrigen));

        Cliente clienteOrigen = buscar.buscarCliente(clientes, dniOrigen);
        if (clienteOrigen == null) {
            System.out.println("Cliente origen no encontrado, primero debe crearlo");
        }
        else {

            do {
                System.out.print("Ingrese el CBU de la cuenta origen: ");
                cbuOrigen = entrada.nextLine();
            } while (!validarEntrada.esLong(cbuOrigen));
            Cuenta cuentaOrigen = buscarCuentas(clienteOrigen.getCuentas(), cbuOrigen);
            if (cuentaOrigen == null) {
                System.out.println("Cuenta origen no encontrada");
            } else {
                do{
                    System.out.print("Ingrese el DNI del cliente destino: ");
                    dniDestino = entrada.nextLine();
                } while (!validar.validarDni(dniDestino));
                Cliente clienteDestino = buscar.buscarCliente(clientes, dniDestino);
                if (clienteDestino == null) {
                    System.out.println("Cliente destino no encontrado, primero debe crearlo");
                } else {
                    do {
                        System.out.print("Ingrese el CBU de la cuenta destino: ");
                        cbuDestino = entrada.nextLine();
                    } while (!validarEntrada.esLong(cbuDestino) || !validar.validarCuentaDestino(cbuOrigen, cbuDestino));

                    Cuenta cuentaDestino = buscarCuentas(clienteDestino.getCuentas(), cbuDestino);
                    if (cuentaDestino == null) {
                        System.out.println("Cuenta destino no encontrada");
                    } else {
                        double monto;
                        do {
                            System.out.print("Ingrese el monto a transferir: ");
                            montoStr = entrada.nextLine();
                        } while (!validarEntrada.esDouble(montoStr));
                        monto = Double.parseDouble(montoStr);
                        if (cuentaOrigen.getSaldo() < monto) {
                            System.out.println("Fondos insuficientes en la cuenta origen");
                        } else {
                            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
                            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);
                            System.out.println("Transferencia realizada con exito");
                            System.out.println("Saldo cuenta origen: " + cuentaOrigen.getSaldo());
                        }
                    }
                }
            }
        }
    }
}
