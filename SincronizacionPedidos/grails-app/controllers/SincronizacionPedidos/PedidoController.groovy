package SincronizacionPedidos


class PedidoController {

    PedidoServiceService pedidoServiceService 

    def index() {
        // Token para autenticacion en la ap ihandy
        String token = 'Bearer c5u6t6rmgel8vslbkfbdls7stcs1l0bm' 
        // Llamo al servicio para obtener pedidos
        def pedidos = pedidoServiceService.obtenerPedidos(token)

        // Verificacion
        if(pedidos) {
            log.info("Pedidos obtenidos: ${pedidos}")
        } else {
            log.error('No se puede obtener pedidos.')
        }
    }
}