import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CellTest {

    @Test
    fun getMaxConnections() {
        Assertions.assertTrue(Cell(1, 10).getMaxConnections() == 1)
        Assertions.assertTrue(Cell(2, 15).getMaxConnections() == 2)
        Assertions.assertTrue(Cell(3, 45).getMaxConnections() == 2)
        Assertions.assertTrue(Cell(4, 120).getMaxConnections() == 3)
    }

    @Test
    fun connect() {
        val cell = Cell(1, 10)
        val cell2 = Cell(2, 100)
        cell.connect(Cell(3, 15))

        Assertions.assertTrue(cell.getConnections() == 1)
        // Connects another cell. But this shouldn't happen since the cell's power only allows to have 1 connection
        cell.connect(cell2)

        Assertions.assertTrue(cell.getConnections() == 1)

        // Connects cell2 to three different cells but it should allow only two connections
        cell2.connect(cell)
        cell2.connect(Cell(4, 30))
        cell2.connect(Cell(5, 1))

        Assertions.assertTrue(cell.getConnections() == 1)
        Assertions.assertTrue(cell2.getConnections() == 2)
    }

    @Test
    fun powerTransfer() {
        val cell = Cell(1, 100)
        val cell2 = Cell(2, 100)

        Assertions.assertNotNull(cell.connect(cell2))

        Thread.sleep(REGENERATION_DELAY + 100)

        Assertions.assertTrue(cell2.getPower() == 101)
        Assertions.assertTrue(cell.getPower() == 100)

        Thread.sleep(TRANSFER_DELAY)

        Assertions.assertTrue(cell2.getPower() == 102)
        Assertions.assertTrue(cell.getPower() == 100)

        Thread.sleep(REGENERATION_DELAY + TRANSFER_DELAY + 100)

        Assertions.assertTrue(cell2.getPower() == 104)
        Assertions.assertTrue(cell.getPower() == 100)
    }

    @Test
    fun regeneration() {
        val cell = Cell(1, 1)

        Thread.sleep(REGENERATION_DELAY + 100)

        Assertions.assertTrue(cell.getPower() == 2)

        Thread.sleep(REGENERATION_DELAY + 100)

        Assertions.assertTrue(cell.getPower() == 3)

        Thread.sleep(REGENERATION_DELAY + 100)

        Assertions.assertTrue(cell.getPower() == 4)
    }

    @Test
    fun disconnect() {
        val cell = Cell(1, 100)
        val cell2 = Cell(2, 100)

        cell.connect(cell2)

        Assertions.assertTrue(cell.getConnections() == 1)

        cell.disconnect(cell2)

        Assertions.assertTrue(cell.getConnections() == 0)
    }

    @Test
    fun increasePower() {
        val cell = Cell(1, 1)

        Assertions.assertTrue(cell.getPower() == 1)

        cell.increasePower(1)

        Assertions.assertTrue(cell.getPower() == 2)

        cell.increasePower(5)

        Assertions.assertTrue(cell.getPower() == 7)
    }

    @Test
    fun decreasePower() {
        val cell = Cell(1, 10)

        Assertions.assertTrue(cell.getPower() == 10)

        cell.decreasePower(1)

        Assertions.assertTrue(cell.getPower() == 9)

        cell.decreasePower(5)

        Assertions.assertTrue(cell.getPower() == 4)
    }

    @Test
    fun testSurvival() {
        val cell = Cell(1, 15)
        val cell2 = Cell(2, 1)
        val cell3 = Cell(3, 1)

        cell.connect(cell2)
        cell.connect(cell3)

        Assertions.assertTrue(cell.getConnections() == 2)

        Thread.sleep((REGENERATION_DELAY + TRANSFER_DELAY) * 20)

        Assertions.assertTrue(cell.getPower() > 0)
        Assertions.assertTrue(cell.getConnections() == 0)
    }
}