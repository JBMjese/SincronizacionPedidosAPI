package SincronizacionPedidos

class Pedido {
    String id // Id del pedido
    BigDecimal totalVentas // Monto total ventas
    Date fechaCreacion = new Date() // Fecha de creación del pedido

    static constraints = {
        id nullable: false, blank: false, unique: true // Id no puede ser nulo
        totalVentas nullable: false // El monto total no puede ser nulo
        fechaCreacion nullable: false // La fecha de creación no puede ser nula
    }
}