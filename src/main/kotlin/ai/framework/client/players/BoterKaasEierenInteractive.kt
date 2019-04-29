package ai.framework.client.players

import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.Move
import ai.framework.core.board.VierOpEenRijBoard
import ai.framework.core.helper.logger
import java.awt.BorderLayout
import java.awt.Font
import java.util.*
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.JTextPane

fun main() {
    var frame: JFrame = JFrame("Board")
    var jta: JTextPane = JTextPane()
    frame.setLayout(BorderLayout())
    frame.add(jta, BorderLayout.CENTER)
    jta.setFont(Font("monospaced", Font.PLAIN, 12))
    frame.setSize(400, 300)
    frame.defaultCloseOperation =JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
    val board = BoterKaasEierenBoard(2)
    val scanner = Scanner(System.`in`)
    val player = BoterKaasEierenRandomPlayer()
    while(!board.finished()) {
        board.makeMove(player.makeMove(board)!!)
        jta.text = board.visualize()
        val nextMove = scanner.nextLine()
        val c = when (nextMove[0]){
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> 3
        }
        val r = when (nextMove[1]){
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> 3
        }
        board.makeMove(Move(mutableMapOf("column" to c, "row" to r)))
    }
}
