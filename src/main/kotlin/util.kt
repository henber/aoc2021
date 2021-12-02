import java.io.File

fun readInput(fileName: String): List<String>
        = File("src/main/resources/$fileName").readLines()

fun readInputText(fileName: String): String
        = File("src/main/resources/$fileName").readText()

tailrec fun <T> listPartition(list: List<T>, result: MutableList<List<T>> = mutableListOf(), predicate: (T) -> Boolean): List<List<T>> {
    val partition = list.takeWhile(predicate)

    if (list.isEmpty()) {
        return result
    }

    if (partition.isNotEmpty()) {
        result.add(partition)
    }

    return listPartition(list.drop(partition.size + 1), result, predicate)
}

fun <E> List<List<E>>.transpose(): List<List<E>> {
    if (isEmpty()) return this

    val width = first().size
    if (any { it.size != width }) {
        throw IllegalArgumentException("All nested lists must have the same size, but sizes were ${map { it.size }}")
    }

    return (0 until width).map { col ->
        (0 until size).map { row -> this[row][col] }
    }
}

fun <A, B> lazyCartesianProduct(
    listA: Iterable<A>,
    listB: Iterable<B>
): Sequence<Pair<A, B>> =
    sequence {
        listA.forEach { a ->
            listB.forEach { b ->
                yield(a to b)
            }
        }
    }

fun <T> cartProduct(vararg iterables: List<T>): Sequence<List<T>> = sequence {

    require(iterables.map { it.size.toLong() }.reduce(Long::times) <= Int.MAX_VALUE) {
        "Cartesian product function can produce result whose size does not exceed Int.MAX_VALUE"
    }

    val numberOfIterables = iterables.size
    val lstLengths = ArrayList<Int>()
    val lstRemaining = ArrayList(listOf(1))

    iterables.reversed().forEach {
        lstLengths.add(0, it.size)
        lstRemaining.add(0, it.size * lstRemaining[0])
    }

    val nProducts = lstRemaining.removeAt(0)

    (0 until nProducts).forEach { product ->
        val result = ArrayList<T>()
        (0 until numberOfIterables).forEach { iterableIndex ->
            val elementIndex = product / lstRemaining[iterableIndex] % lstLengths[iterableIndex]
            result.add(iterables[iterableIndex][elementIndex])
        }
        yield(result.toList())
    }
}

fun <T> List<List<T>>.rotate(steps: Int): List<List<T>> {
    val actualSteps = if (steps >= 0) steps % 4 else (steps % 4) + 4

    return when (actualSteps) {
        0 -> this
        1 -> this.transpose().map { it.reversed() }
        2 -> this.rotate(1).rotate(1)
        3 -> this.map { it.reversed() }.transpose()
        else -> error("unexpected steps: $actualSteps")
    }
}

fun <T> List<List<T>>.flipColumns(): List<List<T>> {
    return this.map { it.reversed() }
}

fun <T> List<List<T>>.flipRows(): List<List<T>> {
    return this.reversed()
}

fun List<Char>.asString(): String = String(this.toCharArray())
