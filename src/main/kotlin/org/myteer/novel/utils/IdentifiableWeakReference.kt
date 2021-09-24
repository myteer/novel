package org.myteer.novel.utils

import java.lang.ref.WeakReference

class IdentifiableWeakReference<T>(referent: T) : WeakReference<T>(referent) {
    override fun hashCode(): Int = get()?.hashCode() ?: super.hashCode()

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is WeakReference<*> -> false
        else -> if (get() == null) super.equals(other) else get() == other.get()
    }
}