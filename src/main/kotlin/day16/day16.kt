package day16

import product
import readInputText

val hexInput =
    readInputText("day16.txt")
//    "8A004A801A8002F478" // 16
//    "620080001611562C8802118E34" // 12
//    "C0015000016115A2E0802F182340" // 23
//    "A0016C880162017C3686B18A3D4780" // 31
val binInput = hexInput.map {
    val binVal = Integer.toBinaryString(Integer.parseInt("$it", 16))
    if (binVal.length < 4) {
        return@map "0".repeat(4 - binVal.length) + binVal
    }
    binVal
}.joinToString("") { it }.toList()

data class Packet(
    val version: Int,
    val typeId: Int,
    val lengthTypeId: Int?,
    val literal: Long?,
    val subPackets: List<Packet>
) {
    fun versionSum(): Int = subPackets.map { it.versionSum() }.sum() + version

    fun compute(): Long {
        return when (typeId) {
            4 -> literal!!
            0 -> subPackets.map { it.compute() }.sum()
            1 -> subPackets.map { it.compute() }.product()
            2 -> subPackets.map { it.compute() }.minOrNull()!!
            3 -> subPackets.map { it.compute() }.maxOrNull()!!
            5 -> if (subPackets[0].compute() > subPackets[1].compute()) 1 else 0
            6 -> if (subPackets[0].compute() < subPackets[1].compute()) 1 else 0
            7 -> if (subPackets[0].compute() == subPackets[1].compute()) 1 else 0
            else -> throw Error("Invalid typeId: $typeId")
        }
    }
}

fun String.fromBinaryToInt(): Int = Integer.parseInt(this, 2)

fun readVersion(binSeq: List<Char>, index: Int = 0): String = binSeq.subList(index, index + 3)
    .joinToString("") { it.toString() }

fun readTypeId(binSeq: List<Char>, index: Int = 0): String = binSeq.subList(index, index + 3)
    .joinToString("") { it.toString() }

fun readLengthTypeId(binSeq: List<Char>, index: Int = 0): String = binSeq.subList(index, index + 1)
    .joinToString("") { it.toString() }

fun readLiteral(binSeq: List<Char>, index: Int = 0): String = binSeq.subList(index, index + 5)
    .joinToString("") { it.toString() }

fun readBitLength(binSeq: List<Char>, index: Int = 0): String = binSeq.subList(index, index + 15)
    .joinToString("") { it.toString() }

fun readPacketNr(binSeq: List<Char>, index: Int = 0): String = binSeq.subList(index, index + 11)
    .joinToString("") { it.toString() }

fun parsePacket(binSeq: List<Char>, startIndex: Int = 0): Pair<Packet, Int> {
    var cursor: Int = startIndex

    val version = readVersion(binSeq, cursor)
    cursor += version.length

    val typeId = readTypeId(binSeq, cursor)
    cursor += typeId.length

    val subPackets = mutableListOf<Packet>()
    var lengthTypeId: String? = null
    var literalValue: String? = null

    println("Version: ${version.fromBinaryToInt()}")
    println("TypeId: ${typeId.fromBinaryToInt()}")

    if (typeId.fromBinaryToInt() == 4) {
        var literal = ""

        do {
            val literalPart = readLiteral(binSeq, cursor)
            cursor += literalPart.length
            literal += literalPart.substring(1)
        } while (literalPart[0] == '1')

        literalValue = literal
    } else {
        lengthTypeId = readLengthTypeId(binSeq, cursor)
        cursor += lengthTypeId.length

        if (lengthTypeId == "0") {
            val bitLength = readBitLength(binSeq, cursor)
            cursor += bitLength.length
            var bitCounter = bitLength.fromBinaryToInt()

            do {
                val subPacket = parsePacket(binSeq, cursor)
                subPackets.add(subPacket.first)
                bitCounter -= subPacket.second
                cursor += subPacket.second
            } while (bitCounter > 0)
        } else {
            val packetNr = readPacketNr(binSeq, cursor)
            cursor += packetNr.length

            repeat(packetNr.fromBinaryToInt()) {
                val subPacket = parsePacket(binSeq, cursor)
                subPackets.add(subPacket.first)
                cursor += subPacket.second
            }
        }
    }
    return Pair(Packet(version.fromBinaryToInt(), typeId.fromBinaryToInt(),
        lengthTypeId?.fromBinaryToInt(), literalValue?.toLong(2), subPackets), cursor - startIndex)
}

fun solveA(): Int {
    val packet = parsePacket(binInput)

    return packet.first.versionSum()
}

fun solveB(): Long {
    val packet = parsePacket(binInput)

    return packet.first.compute()
}

fun main() {
    println("Answer A: ${solveA()}")
    println("Answer B: ${solveB()}")
}
