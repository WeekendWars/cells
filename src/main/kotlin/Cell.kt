import kotlinx.coroutines.Job
import java.util.concurrent.Semaphore

data class Cell(val id: Int, private var power: Int) {
    private val powerHandler = PowerHandler(this)
    private val semaphore = Semaphore(1, true)

    fun connect(cell: Cell): Job? {
        return if (getMaxConnections() > powerHandler.getConnections()) {
            powerHandler.connect(cell)
        } else
            null
    }

    fun getMaxConnections(): Int {
        return if (power < 15)
            1
        else return if (power < 120)
            2
        else
            3
    }

    fun getPower(): Int = power

    fun increasePower(amount: Int) {
        semaphore.acquire()
        power += amount
        semaphore.release()

        println("--------------\n↑ [$id] ($power)")
    }

    fun decreasePower(amount: Int) {
        semaphore.acquire()
        power -= amount
        semaphore.release()

        println("--------------\n↓ [$id] ($power)")
    }

    fun getConnections(): Int = powerHandler.getConnections()

    fun disconnect(cell: Cell) {
        powerHandler.disconnect(cell.id)
    }
}