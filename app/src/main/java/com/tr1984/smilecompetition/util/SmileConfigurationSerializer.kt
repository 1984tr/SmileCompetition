package com.tr1984.smilecompetition.util

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.tr1984.smilecompetition.SmileConfiguration
import java.io.InputStream
import java.io.OutputStream

object SmileConfigurationSerializer : Serializer<SmileConfiguration> {

    override fun readFrom(input: InputStream): SmileConfiguration {
        try {
            return SmileConfiguration.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: SmileConfiguration, output: OutputStream) = t.writeTo(output)

    override val defaultValue: SmileConfiguration = SmileConfiguration.getDefaultInstance()
}