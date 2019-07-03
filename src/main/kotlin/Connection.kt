import kotlinx.coroutines.*
import java.util.*

const val TRANSFER_DELAY = 500L

class Connection(private val cell: Cell, private val newConnection: Cell) {
    private var isConnected = true
    private val queue = PriorityQueue<Int>()
    private val job = GlobalScope.launch {

        while (isConnected) {

            coroutineScope {
                if (queue.isNotEmpty()) {
                    val value = queue.poll()

                    println("--------------\n[${cell.id}] → ($value) [${newConnection.id}]")

                    // Loses power
                    cell.decreasePower(value)

                    // Takes some time to reach the other cell
                    delay(TRANSFER_DELAY)

                    // Receives the power
                    newConnection.increasePower(value)
                }
            }
        }
    }

    fun stopConnection() {
        job.cancel()
    }

    fun transferPower(power: Int): Job {
        queue.add(power)

        return job
    }
}