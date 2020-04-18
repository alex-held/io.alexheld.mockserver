/*
package io.alexheld.mockserver.infrastracture

import io.alexheld.mockserver.logging.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.joda.time.*


object Setups : IntIdTable("setups") {

    // relations
    var requestId = Requests.reference("source_id", Requests, ReferenceOption.CASCADE).primaryKey(0)
    var actionId = Actions.reference("source_id", Actions, ReferenceOption.CASCADE).primaryKey(0)

    // members
    val createdAt: Column<DateTime> = datetime("createdAt")
}


object Requests : IntIdTable("requests") {

    // relations
    var setupId = reference("source_id", Setups, ReferenceOption.CASCADE).primaryKey(0)

    // members
    val method = varchar("method", 16).uniqueIndex()
    val path = varchar("path", 255).uniqueIndex()
    val query = varchar("query", 255).uniqueIndex()
    val body = varchar("query", 255).uniqueIndex()
    val exactMatch = bool("exactMatch").uniqueIndex()
    val createdAt = datetime("createdAt")
}


object Actions : IntIdTable("actions") {

    // relations
    var setupId = Setups.reference("source_id", Setups, ReferenceOption.CASCADE).primaryKey(0)

    // members
    val statusCode = integer("statusCode").uniqueIndex()
    val message = varchar("message", 255).uniqueIndex()
}


object Logs : IntIdTable("logs") {

    // members
    var logType = enumerationByName("logType", 1, LogMessageType::class).uniqueIndex()
    val apiCtegory = enumerationByName("apiCategory", 1, ApiCategory::class).uniqueIndex()
    val apiOperation = enumerationByName("apiOperation", 1, ApiOperation::class).uniqueIndex()
    val timestamp = datetime("timestamp")
    val data = text("body")
}
*/
