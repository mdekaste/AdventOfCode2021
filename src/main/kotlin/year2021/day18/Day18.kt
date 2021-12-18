package year2021.day18

import Challenge

fun main() = Day18.printSolutions()

object Day18 : Challenge() {
    val parsed = input.lines()
    override fun part1() = parsed.map(Node::of).reduce(Node::plus).magnitude()

    override fun part2() = parsed.indices.flatMap { y -> parsed.indices.map { x -> y to x }.filter { (y,x) -> y != x } }
            .map { (y,x) -> Node.of(parsed[y]) + Node.of(parsed[x]) }
            .maxOf(Node::magnitude)
}

sealed interface Node {
    fun flatten(depth: Int): List<Pair<Node, Int>>
    fun magnitude(): Long
    companion object{
        fun of(input: String) = of(input.iterator())
        private fun of(charIterator: CharIterator) : Node {
            val char = charIterator.nextChar()
            return if(char.isDigit()){
                Leaf(char.digitToInt())
            } else {
                val left = of(charIterator)
                charIterator.next()
                val right = of(charIterator)
                charIterator.next()
                Branch(left, right)
            }
        }
    }

    operator fun plus(toAdd: Node) = Branch(this, toAdd).apply {
        loop@while(true){
            val (nodes, depths) = flatten(0).unzip()
            for (i in nodes.indices){
                val node = nodes[i]
                val depth = depths[i]
                if (node is Branch && depth >= 4){
                    val leftNode = nodes[i-1] as Leaf
                    val rightNode = nodes[i+1] as Leaf
                    val (parent, isLeftChild) = nodes.first { it is Branch && (it.left === node || it.right === node) }.let { (it as Branch) to (it.left === node) }
                    nodes.takeWhile { it !== leftNode }.lastOrNull { it is Leaf }?.apply { (this as Leaf).value += leftNode.value }
                    nodes.reversed().takeWhile { it !== rightNode }.lastOrNull{ it is Leaf }?.apply { (this as Leaf).value += rightNode.value }
                    if(isLeftChild){
                        parent.left = Leaf(0)
                    } else {
                        parent.right = Leaf(0)
                    }
                    continue@loop
                }
            }
            for(i in nodes.indices){
                val node = nodes[i]
                val depth = depths[i]
                if(node is Leaf && node.value > 9){
                    val leftValue = node.value / 2
                    val rightValue = node.value - leftValue
                    val (parent, isLeftChild) = nodes.first { it is Branch && (it.left === node || it.right === node) }.let { (it as Branch) to (it.left === node) }
                    if(isLeftChild){
                        parent.left = Branch(Leaf(leftValue), Leaf(rightValue))
                    } else {
                        parent.right = Branch(Leaf(leftValue), Leaf(rightValue))
                    }
                    continue@loop
                }
            }
            break@loop
        }
    }
}

data class Branch(var left: Node, var right: Node) : Node{
    override fun toString() = "[$left,$right]"
    override fun flatten(depth: Int) = left.flatten(depth + 1) + (this to depth) + right.flatten(depth + 1)
    override fun magnitude(): Long = 3L * left.magnitude() + 2L * right.magnitude()
}

data class Leaf(var value: Int) : Node{
    override fun toString() = value.toString()
    override fun flatten(depth: Int) = listOf(this to depth)
    override fun magnitude() = value.toLong()
}