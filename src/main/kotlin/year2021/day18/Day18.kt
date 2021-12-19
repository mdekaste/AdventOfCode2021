//package year2021.day18
//
//import Challenge
//
//fun main() = Day18.printSolutions()
//
//object Day18 : Challenge() {
//    val parsed = input.lines()
//    override fun part1() = parsed.map(Node::of).reduce(Node::plus).magnitude()
//
//    override fun part2() = parsed.indices.flatMap { y -> parsed.indices.map { x -> y to x }.filter { (y,x) -> y != x } }
//            .map { (y,x) -> Node.of(parsed[y]) + Node.of(parsed[x]) }
//            .maxOf(Node::magnitude)
//}
//
//sealed interface Node {
//    var parent: Branch?
//    fun flatten(): Sequence<Node>
//    fun magnitude(): Long
//
//    companion object{
//        fun of(input: String) = of(input.iterator())
//        private fun of(charIterator: CharIterator, depth: Int = 0) : Node {
//            val char = charIterator.nextChar()
//            return if(char.isDigit()){
//                Leaf(char.digitToInt(), depth)
//            } else {
//                val left = of(charIterator, depth + 1)
//                charIterator.next()
//                val right = of(charIterator, depth + 1)
//                charIterator.next()
//                Branch(left, right, depth)
//            }
//        }
//    }
//
//    operator fun plus(toAdd: Node) = Branch(this, toAdd).apply {
//        loop@ while (true) {
//            val list = flatten().toMutableList()
//            val iterator = list.listIterator()
//            while (iterator.hasNext()) {
//                val index = iterator.nextIndex()
//                val node = iterator.next()
//                if (explode(node, list, index))
//                    continue@loop
//                if (split(node, list, index))
//                    continue@loop
//            }
//            break@loop
//        }
//    }
//
//    private fun explode(node: Node, list: MutableList<Node>, index: Int) = when{
//        node is Branch && node.depth == 4 -> {
//            val leftNode = node.left as Leaf
//            val rightNode = node.right as Leaf
//            val parent = walkBack(list, index){ it is Branch && (it.left === node || it.right === node) } as Branch
//            val firstLeaf: (Node) -> Boolean = { it is Leaf && it !== leftNode && it !== rightNode }
//            walkBack(list, index, firstLeaf)?.apply { (this as Leaf).value += leftNode.value }
//            walkFront(list, index, firstLeaf)?.apply { (this as Leaf).value += rightNode.value }
//            parent.replace(node, Leaf(0, node.depth))
//            true
//        }
//        else -> false
//    }
//
//    private fun split(node: Node, list: MutableList<Node>, index: Int) = when {
//        node is Leaf && node.value > 9 -> {
//            val leftValue = node.value / 2
//            val rightValue = node.value - leftValue
//            val parent = walkBack(list, index){ it is Branch && (it.left === node || it.right === node) } as Branch
//            parent.replace(node, Branch(Leaf(leftValue, node.depth + 1), Leaf(rightValue, node.depth + 1), node.depth))
//            true
//        }
//        else -> false
//    }
//
//    private fun walkBack(list: List<Node>, index: Int, predicate: (Node) -> Boolean) : Node? {
//        val iterator = list.listIterator(index - 1)
//        while(iterator.hasPrevious()){
//            val candidate = iterator.previous()
//            if(predicate(candidate)){
//                return candidate
//            }
//        }
//        return null
//    }
//
//    private fun walkFront(list: List<Node>, index: Int, predicate: (Node) -> Boolean) : Node? {
//        val iterator = list.listIterator(index + 1)
//        while(iterator.hasNext()){
//            val candidate = iterator.next()
//            if(predicate(candidate)){
//                return candidate
//            }
//        }
//        return null
//    }
//
////            flatten().withIndex().map { (index, node) ->
////                if(node is Branch && depth > 4){
////                    val leftNode = node.left as Leaf
////                    val rightNode = node.right as Leaf
////                    val isLeftChild = parent?.left === node
////                    val sequence = flatten()
////                    sequence.takeWhile { it !== leftNode }.filterIsInstance<Leaf>().lastOrNull()?.apply { value += leftNode.value }
////                    sequence.dropWhile { it !== rightNode }.filterIsInstance<Leaf>().firstOrNull()?.apply { value += rightNode.value }
////                    Leaf(0).also { if(isLeftChild) parent?.left = it else parent?.right = it }
////                    continue@loop
////                }
////            }
////            val (nodes, depths) = flatten(0).unzip()
////            for (i in nodes.indices){
////                val node = nodes[i]
////                val depth = depths[i]
////                if (node is Branch && depth >= 4){
////                    val leftNode = nodes[i-1] as Leaf
////                    val rightNode = nodes[i+1] as Leaf
////                    val (parent, isLeftChild) = nodes.first { it is Branch && (it.left === node || it.right === node) }.let { (it as Branch) to (it.left === node) }
////                    nodes.takeWhile { it !== leftNode }.lastOrNull { it is Leaf }?.apply { (this as Leaf).value += leftNode.value }
////                    nodes.reversed().takeWhile { it !== rightNode }.lastOrNull{ it is Leaf }?.apply { (this as Leaf).value += rightNode.value }
////                    if(isLeftChild){
////                        parent.left = Leaf(0)
////                    } else {
////                        parent.right = Leaf(0)
////                    }
////                    continue@loop
////                }
////            }
////            for(i in nodes.indices){
////                val node = nodes[i]
////                val depth = depths[i]
////                if(node is Leaf && node.value > 9){
////                    val leftValue = node.value / 2
////                    val rightValue = node.value - leftValue
////                    val (parent, isLeftChild) = nodes.first { it is Branch && (it.left === node || it.right === node) }.let { (it as Branch) to (it.left === node) }
////                    if(isLeftChild){
////                        parent.left = Branch(Leaf(leftValue), Leaf(rightValue))
////                    } else {
////                        parent.right = Branch(Leaf(leftValue), Leaf(rightValue))
////                    }
////                    continue@loop
////                }
////            }
////            break@loop
////        }
////    }
//}
//
//class Branch(left: Node, right: Node) : Node{
//    var left = left
//        set(value) {
//            value.parent = this
//        }
//    override fun toString() = "[$left,$right]"
//    override fun flatten(): Sequence<Node> = sequence {
//        yield(this@Branch)
//        yieldAll(left.flatten())
//        yieldAll(right.flatten())
//    }
//    override fun magnitude(): Long = 3L * left.magnitude() + 2L * right.magnitude()
//    fun replace(now: Node, future: Node) = when(now){
//        left -> left = future
//        right -> right = future
//        else -> error("")
//    }
//}
//
//class Leaf(var value: Int) : Node{
//    override fun toString() = value.toString()
//    override fun flatten() = sequenceOf(this)
//    override fun magnitude() = value.toLong()
//}