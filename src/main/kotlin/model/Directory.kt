package org.example.model

class Directory(name: String) : FileSystemNode(name), Searchable {

    private val children: MutableMap<String, FileSystemNode> = mutableMapOf()

    /**
     * Рекурсивный метод нахождения суммы.
     */
    override fun getSize(): Int {
        return children.values.sumOf { it.getSize() }
    }

    /**
     * Добавление нового объекта в папку. Если есть совпадение по имени - объект не добавляется.
     * Для вложенного объекта проставляется родитель.
     */
    fun add(node: FileSystemNode): Boolean {
        if (children.containsKey(node.name)) {
            return false
        }
        children[node.name] = node
        node.parent = this
        return true
    }

    /**
     * Рекурсивное удаление объекта и обнуление ссылки на родителя.
     */
    fun remove(name: String): Boolean {
        val node = children[name] ?: return false
        if (node is Directory) {
            val childNames = node.listContents()
            for (childName in childNames) {
                node.remove(childName)
            }
        }
        children.remove(name)
        node.parent = null
        return true
    }

    /**
     * Список наименований дочерних элементов.
     */
    fun listContents(): List<String> {
        return children.keys.sorted()
    }


    override fun find(name: String): FileSystemNode? {
        if (children.containsKey(name)) {
            return children[name]
        }
        for (child in children.values) {
            if (child is Directory) {
                val found = child.find(name)
                if (found != null) {
                    return found
                }
            }
        }
        return null
    }

    companion object {
        fun createRoot(): Directory {
            return Directory("root")
        }
    }
}