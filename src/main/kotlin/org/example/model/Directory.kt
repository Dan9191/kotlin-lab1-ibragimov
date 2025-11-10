package org.example.model

class Directory(name: String) : FileSystemNode(name), Searchable {
    private val children: MutableMap<String, FileSystemNode> = mutableMapOf()
    private var cachedSize: Int? = null

    /**
     * Рекурсивный метод нахождения суммы.
     */
    override fun getSize(): Int {
        return cachedSize ?: children.values.sumOf { it.getSize() }.also {
            cachedSize = it
        }
    }

    /**
     * Добавление нового объекта в папку. Если есть совпадение по имени - объект не добавляется.
     * Для вложенного объекта проставляется родитель.
     */
    fun add(node: FileSystemNode): Boolean {
        if (children.containsKey(node.name)) return false
        children[node.name] = node
        node.parent = this
        node.os = this.os
        invalidateSizeCache()
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
        invalidateSizeCache()
        return true
    }

    /**
     * Список наименований дочерних элементов.
     */
    fun listContents(): List<String> = children.keys.sorted()

    override fun find(name: String): FileSystemNode? {
        children[name]?.let { return it }
        for (child in children.values) {
            if (child is Directory) {
                val found = child.find(name)
                if (found != null) return found
            }
        }
        return null
    }

    /**
     * Сброс кэша при удалении или добавлении элемента
     */
    private fun invalidateSizeCache() {
        cachedSize = null
        parent?.invalidateSizeCache()
    }

    companion object {
        fun createRoot(os: OS = OS.LINUX): Directory {
            val root = Directory("root")
            root.os = os
            return root
        }
    }
}
