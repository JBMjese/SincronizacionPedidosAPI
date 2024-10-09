package SincronizacionPedidos

import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestBuilder

@Transactional
class PedidoServiceService {

    def rest

    PedidoServiceService() {
        rest = new RestBuilder()
    }
    // Metodo para obtener pedidos de la api
    def obtenerPedidos(String token) {
        def response = rest.get('https://hub.handy.la/api/v2/salesOrder') {
            header 'Authorization', "Bearer ${token}"
        }
        // Verificacion
        if (response.status == 200) {
            return response.data
        } else {
            log.error("Error al obtener pedidos: ${response.status} ${response.message}")
            return null
        }
    }
    // Metodo para detectar y eliminar pedidos que ya no existen en la Aapi
    def detectarPedidosEliminados(List<String> pedidosActuales) {
        // Obtener todos los pedidos de la base de datos
        def pedidosLocales = Pedido.list() // Obtiene todos los pedidos en la base de datos

        pedidosLocales.each { pedidoLocal ->
            // Si el ID del pedido local no está en la lista de pedidos actuales, se elimina
            if (!pedidosActuales.contains(pedidoLocal.id)) {
                log.info("Eliminando pedido local: ${pedidoLocal.id}")
                pedidoLocal.delete()
            }
        }
    }

}
