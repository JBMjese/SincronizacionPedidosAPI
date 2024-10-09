package SincronizacionPedidos

import grails.plugins.quartz.Job
import grails.plugins.quartz.TriggerUtils
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PedidoJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(PedidoJob)
    def grailsApplication

     // Metodo que se ejecuta cuando se activa el trabajo
    void execute() {
        leerPedidos()
    }

    // Metodo para leer los pedidos desde la API de Handy
    def leerPedidos() {
        def handyApiUrl = "https://hub.handy.la/api/v2/salesOrder"
        def dateFormat = "DD/MM/YYYY HH:mm:ss"
        def lastTime = getLastTime()
        def endTime = new Date().format(dateFormat)

        def url = "${handyApiUrl}?start=${lastTime}&end=${endTime}&deleted=false"
        def response = queryHandyAPI(url)

        if (response) {
            def salesOrders = response.salesOrders
            while (response.pagination && response.pagination.nextPage) {
                response = queryHandyAPI(response.pagination.nextPage)
                if (response) salesOrders += response.salesOrders
            }

            guardarPedidos(salesOrders)
        }
        detectarPedidosEliminados(idsActuales)
        saveLastTime(endTime)
    }

    def getLastTime() {
        def file = new File("last_time.txt")
        return file.exists() ? file.text : new Date().format("DD/MM/YYYY HH:mm:ss")
    }

    def saveLastTime(lastTime) {
        def file = new File("last_time.txt")
        file.write(lastTime)
    }

    def guardarPedidos(salesOrders) {
        salesOrders.each { salesOrder ->
            def pedidoLocal = new Pedido(id: salesOrder.id, totalVentas: salesOrder.totalVentas)
            pedidoLocal.save()
        }
    }

    def queryHandyAPI(url) {
        try {
            def headers = ["Authorization": "Bearer ${handyBearerToken}", "Content-Type": "application/json"]
            def response = new URL(url).openConnection()
            response.setRequestMethod("GET")
            response.setRequestProperty("Authorization", headers["Authorization"])
            response.setRequestProperty("Content-Type", headers["Content-Type"])

            def json = new JsonSlurper().parseText(response.getInputStream().getText())
            return json
        } catch (IOException e) {
            log.error("Error al realizar la solicitud GET: ${e.message}")
            return null
        }
    }
}