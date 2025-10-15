import org.example.model.Directory
import org.example.model.File
import org.example.printTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test


class MainKtTest {

    private lateinit var root: Directory

    @BeforeEach
    fun setUp() {
        if (!::root.isInitialized) {
            root = Directory.createRoot()
        } else {
            root.clearRoot()
        }

        assertTrue(root.listContents().isEmpty(), "Root should be empty after setup")
    }

    private fun String.normalizeWhitespace(): String {
        return this.trim()
            .replace("\\s+".toRegex(), " ")
            .replace("\r\n", "\n")
    }


    @Test
    @DisplayName("Проверка создания пустого корня")
    fun whenRootCreated_ShouldBeEmpty() {
        assertTrue(root.listContents().isEmpty(), "Root should be empty after setup")
    }

    @Test
    @DisplayName("Проверка добавления папки")
    fun add_WhenAddingDirectory_ShouldBeAddedSuccessfully() {
        // given
        val docs = Directory("documents")
        val expectedTree = "+ root/ (0 bytes total) + documents/ (0 bytes total)".trimMargin()

        // when
        root.add(docs)

        //then
        assertEquals(root.listContents().size, 1, "There should be a folder in the root")
        assertEquals(printTree(root).normalizeWhitespace(), expectedTree.normalizeWhitespace(), "Incorrect root printout")

    }

    @Test
    @DisplayName("Проверка добавления файла")
    fun add_WhenAddingFile_ShouldBeAddedSuccessfully() {
        // given
        val readme = File("readme.txt", "the most important document")
        val expectedTree = "+ root/ (27 bytes total) - readme.txt (27 bytes)".trimMargin()

        // when
        root.add(readme)

        //then
        assertEquals(root.listContents().size, 1, "There should be a file in the root")
        assertEquals(printTree(root).normalizeWhitespace(), expectedTree.normalizeWhitespace(), "Incorrect root printout")

    }

    @Test
    @DisplayName("Проверка удаления папки")
    fun remove_WhenRemovingFile_ShouldBeAddedSuccessfully() {
        // given
        val docs = Directory("documents")
        val expectedTree = "+ root/ (0 bytes total)".trimMargin()

        // when
        root.add(docs)
        root.remove("documents")

        //then
        assertEquals(root.listContents().size, 0, "The root must be empty")
        assertEquals(printTree(root).normalizeWhitespace(), expectedTree.normalizeWhitespace(), "Incorrect root printout")

    }

    @Test
    @DisplayName("Проверка удаления файла")
    fun remove_WhenRemovingFile_ShouldBeRemovedSuccessfully() {
        // given
        val readme = File("readme.txt", "the most important document")
        val expectedTree = "+ root/ (0 bytes total)".trimMargin()

        // when
        root.add(readme)
        root.remove("readme.txt")

        //then
        assertEquals(root.listContents().size, 0, "The root must be empty")
        assertEquals(printTree(root).normalizeWhitespace(), expectedTree.normalizeWhitespace(), "Incorrect root printout")

    }

    @Test
    @DisplayName("Проверка множественного добавления папок и файлов")
    fun add_WhenAddingMultiplyDirectoriesAndFiles_ShouldBeAddedSuccessfully() {
        // given
        val docs = Directory("documents")
        val readme = File("readme.txt", "the most important document")
        val subDir = Directory("sub")
        val note = File("note.txt", "Note")

        val expectedTree = ("+ root/ (31 bytes total)" +
                " + documents/ (27 bytes total)" +
                " - readme.txt (27 bytes)" +
                " + sub/ (4 bytes total)" +
                " - note.txt (4 bytes)").trimMargin()

        // when
        root.add(docs)
        root.add(subDir)
        docs.add(readme)
        subDir.add(note)

        //then
        assertEquals(root.listContents().size, 2, "There should be a folders in the root")
        assertEquals(docs.listContents().size, 1, "There should be a folder in the root")
        assertEquals(printTree(root).normalizeWhitespace(), expectedTree.normalizeWhitespace(), "Incorrect root printout")
    }
}