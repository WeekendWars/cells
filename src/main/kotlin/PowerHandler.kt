import kotlinx.coroutines.*

const val REGENERATION_DELAY = 1500L
const val TRANSFERENCE_AMOUNT = 1
const val REGENERATION_VALUE = 1

class PowerHandler(private val cell: Cell) {
    private val connections = HashMap<Int, Connection>()
    private val job = GlobalScope.launch {
        coroutineScope {
            while (cell.getPower() > 0) {
                delay(REGENERATION_DELAY)

                cell.increasePower(REGENERATION_VALUE)

                val iterator = connections.iterator()

                while (iterator.hasNext()) {
                    val connection = iterator.next()

                    if (cell.getPower() > TRANSFERENCE_AMOUNT)
                        connection.value.transferPower(TRANSFERENCE_AMOUNT)
                    else {
                        println("--------------\n○ [${cell.id}] disconnected from (${connection.key}) Remaining connections: ${cell.getConnections()}")
                        connection.value.stopConnection()
                        iterator.remove()
                    }
                }
            }
        }
    }

    fun connect(newConnection: Cell): Job {
        val connection = Connection(cell, newConnection)
        connections[newConnection.id] = connection

        return job
    }

    fun getConnections(): Int = connections.size

    fun disconnect(cellId: Int) {
        if (connections.containsKey(cellId)) {
            connections[cellId]?.stopConnection()
            connections.remove(cellId)

            println("--------------\n○ [${this.cell.id}] disconnected from ($cellId) Remaining connections: ${cell.getConnections()}")
        }
    }
}