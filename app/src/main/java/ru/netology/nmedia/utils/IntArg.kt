package ru.netology.nmedia.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object IntArg : ReadWriteProperty<Bundle, Int?> {
    override fun getValue(
        thisRef: Bundle,
        property: KProperty<*>
    ) = thisRef.getInt(property.name)

    override fun setValue(
        thisRef: Bundle,
        property: KProperty<*>,
        value: Int?
    ) {
        value?.let { thisRef.putInt(property.name, it) }
    }
}