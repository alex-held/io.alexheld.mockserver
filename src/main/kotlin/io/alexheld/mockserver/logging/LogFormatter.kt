package io.alexheld.mockserver.logging

import io.alexheld.mockserver.serialization.*


object LogFormatter {

    private val LF = System.getProperty("line.separator")

    private fun indentAndFormat(arguments: Array<MockSerializable>) : ArrayList<StringBuilder> {
        val indents = ArrayList<StringBuilder>(arguments.size)
        if (arguments.isEmpty()) return indents

        for (i in arguments.indices){

            val value = arguments[i]

            val serialized = Serializer.serialize(value)
            val serialized_without = serialized.replace("(?m)^", "\t")

            indents.add(StringBuilder(LF)
                .append(LF)
                .append(serialized_without)
                .append(LF))
        }

        return indents
    }

    fun formatLogMessage(message: String, arguments: Array<MockSerializable>) : String {

        val logBuilder = StringBuilder()
        val formattedArguments = indentAndFormat(arguments)
        val parts = message.split("{}")

        for (i in parts.indices){

            logBuilder.append(parts[i])
            if (formattedArguments.size > 0 && formattedArguments.size > i)
                logBuilder.append(formattedArguments[i])

            if (i < parts.size - 1){
                logBuilder.append(LF)
                if (!parts[i + 1].startsWith(" ")) logBuilder.append(" ")
            }
        }

        return logBuilder.toString()
    }


    fun formatLogMessage(parts: ArrayList<String>, arguments: Array<MockSerializable>): String {
        println("[formatLogMessage(parts: ArrayList<String>, vararg arguments: Any)] -  Parts=${parts.size}; Args=${arguments.size}")

        val logBuilder = StringBuilder()
        val formattedArguments = indentAndFormat(arguments)

        for (i in parts.indices) {
            logBuilder.append(parts[i])
            if (formattedArguments.size > 0 && formattedArguments.size > i)
                logBuilder.append(formattedArguments[i])
        }

       return logBuilder.toString()
    }
}
