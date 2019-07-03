import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val cell = Cell(1, 15)
    val cell2 = Cell(2, 1)
    val cell3 = Cell(3, 1)

    cell.connect(cell2)
    cell.connect(cell3)?.join()
}