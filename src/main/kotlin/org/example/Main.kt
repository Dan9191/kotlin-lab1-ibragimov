package org.example

import org.example.model.Directory
import org.example.model.File
import org.example.model.FileSystemNode

fun main() {
    val root = Directory.createRoot()
    val docs = Directory("documents")
    val readme = File("readme.txt", "the most important document")
    val subDir = Directory("sub")
    val note = File("note.txt", "Note")
    val note2 = File("note.txt", "Note")

    // добавление файлов
    println(root.add(docs)) // true
    println(printTree(root))

    println(root.add(subDir)) // true
    println(docs.add(readme)) // true
    println(subDir.add(note)) // true
    println(subDir.add(note2)) // false - добавление дубликата

    //    + root/ (17 bytes total)
    //    + documents/ (13 bytes total)
    //    - readme.txt (13 bytes)
    //    + sub/ (4 bytes total)
    //    - note.txt (4 bytes)
    println(printTree(root))
    // "Note"
    println(note.read())
    // 27
    println(readme.getSize())
    // /root/documents/readme.txt
    println(readme.getPath())

    // Удаление файла
    docs.remove(readme.name)

    // Удаление папки
    root.remove(subDir.name)

    //    + root/ (0 bytes total)
    //    + documents/ (0 bytes total)
    println(printTree(root))

    println(docs.getPath())
    println(root.os)
}

/**
 * Формирует строковое представление дерева файловой системы.
 */
fun printTree(
    node: FileSystemNode,
    indent: String = "",
): String {
    val builder = StringBuilder()
    if (node is File) {
        builder.append("$indent- ${node.name} (${node.getSize()} bytes)\n")
    } else if (node is Directory) {
        builder.append("$indent+ ${node.name}/ (${node.getSize()} bytes total)\n")
        val sortedChildren = node.listContents().map { node.find(it)!! }
        for (child in sortedChildren) {
            builder.append(printTree(child, "$indent  "))
        }
    }
    return builder.toString()
}
