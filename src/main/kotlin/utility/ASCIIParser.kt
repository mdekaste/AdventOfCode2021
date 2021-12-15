package utility

object ASCIIParser {
    private const val ALPHABET =
            ".##..###...##..####.####..##..#..#.###...##.#..#.#.....##..###..###...###.#..#.#...#.####\n" +
            "#..#.#..#.#..#.#....#....#..#.#..#..#.....#.#.#..#....#..#.#..#.#..#.#....#..#.#...#....#\n" +
            "#..#.###..#....###..###..#....####..#.....#.##...#....#..#.#..#.#..#.#....#..#..#.#....#.\n" +
            "####.#..#.#....#....#....#.##.#..#..#.....#.#.#..#....#..#.###..###...##..#..#...#....#..\n" +
            "#..#.#..#.#..#.#....#....#..#.#..#..#..#..#.#.#..#....#..#.#....#.#.....#.#..#...#...#...\n" +
            "#..#.###...##..####.#.....###.#..#.###..##..#..#.####..##..#....#..#.###...##....#...####"

    private val alphabetLines = ALPHABET.lines()
    private const val actual = "ABCEFGHIJKLOPRSUYZ"
    private val characterMap = getCharacterMap(alphabetLines)
        .withIndex()
        .associateBy({ (_, value) -> value }, { (index, _) -> actual[index] })

    fun decodeArt(input: String): String = decodeArt(input.lines())

    fun decodeArt(list: List<String>) = getCharacterMap(list).map { characterMap[it] }.joinToString("")

    private fun getCharacterMap(list: List<String>): List<List<String>> {
        val width = list[0].length
        val height = list.size
        var indices = (0 until width).filter { x ->
            (0 until height).all { y ->
                list[y][x] == '.'
            }
        }
        indices = listOf(-1) + indices + width
        val characters = indices.zipWithNext().map { (x1, x2) ->
            (0 until height).map { y ->
                list[y].substring(x1 + 1 until x2)
            }
        }
        return characters
    }
}
