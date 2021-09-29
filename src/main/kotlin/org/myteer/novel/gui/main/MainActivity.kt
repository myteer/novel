package org.myteer.novel.gui.main

import org.myteer.novel.config.Preferences
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import java.lang.ref.WeakReference
import java.util.*

class MainActivity(
    private val database: NitriteDatabase,
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker
) {
    companion object {
        private val instances: MutableSet<WeakReference<MainActivity>> = Collections.synchronizedSet(mutableSetOf())

        fun getByDatabase(databaseMeta: DatabaseMeta): Optional<MainActivity> {
            return instances.stream()
                .map { it.get() }
                .filter {
                    it?.database?.meta?.equals(databaseMeta) ?: false
                }
                .map { it!! }
                .findAny()
        }
    }

    init {
        instances.add(WeakReference(this))
    }

    fun show() {

    }

    fun getContext(): Context {
        TODO("待实现")
    }
}