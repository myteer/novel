package org.myteer.novel.gui.entry

import javafx.collections.FXCollections
import javafx.collections.ObservableSet
import org.myteer.novel.db.DatabaseMeta
import org.slf4j.LoggerFactory
import java.lang.ref.WeakReference
import java.util.*
import java.util.function.Consumer

class DatabaseTracker {
    companion object {
        private val logger = LoggerFactory.getLogger(DatabaseTracker::class.java)
        val global = DatabaseTracker()
    }
    private val observers: MutableList<WeakReference<Observer>> = Collections.synchronizedList(mutableListOf())
    private val savedDatabases: ObservableSet<DatabaseMeta> = FXCollections.synchronizedObservableSet(FXCollections.observableSet())
    private val savedDatabasesUnmodifiable: ObservableSet<DatabaseMeta> = FXCollections.unmodifiableObservableSet(savedDatabases)
    private val usingDatabases: ObservableSet<DatabaseMeta> = FXCollections.synchronizedObservableSet(FXCollections.observableSet())
    private val usingDatabasesUnmodifiable: ObservableSet<DatabaseMeta> = FXCollections.unmodifiableObservableSet(usingDatabases)

    fun registerObserver(observer: Observer) {
        if (findWeakReference(observer).isEmpty) {
            observers.add(WeakReference(observer))
        }
    }

    fun unRegisterObserver(observer: Observer) {
        findWeakReference(observer).ifPresent(observers::remove)
    }

    fun hasObserver(observer: Observer) = findWeakReference(observer).isPresent

    private fun findWeakReference(observer: Observer) = observers.stream().filter {
        it.get() === observer
    }.findAny()

    fun saveDatabase(databaseMeta: DatabaseMeta) {
        if (savedDatabases.add(databaseMeta)) {
            notifyObservers {
                it.onDatabaseAdded(databaseMeta)
            }
        }
    }

    fun removeDatabase(databaseMeta: DatabaseMeta) {
        if (savedDatabases.remove(databaseMeta)) {
            notifyObservers {
                it.onDatabaseRemoved(databaseMeta)
            }
        }
    }

    fun registerUsedDatabase(databaseMeta: DatabaseMeta) {
        if (usingDatabases.add(databaseMeta)) {
            notifyObservers {
                it.onDatabaseUsing(databaseMeta)
            }
        }
    }

    fun registerClosedDatabase(databaseMeta: DatabaseMeta) {
        if (usingDatabases.remove(databaseMeta)) {
            notifyObservers {
                it.onDatabaseClosing(databaseMeta)
            }
        }
    }

    private fun notifyObservers(consumer: Consumer<Observer>) {
        observers.stream().forEach { wr ->
            wr.get()?.let {
                try {
                    consumer.accept(it)
                } catch (e: Exception) {
                    logger.error("exception caught from observer", e)
                }
            }
        }
    }

    fun isDatabaseSaved(databaseMeta: DatabaseMeta) = savedDatabases.contains(databaseMeta)

    fun isDatabaseUsed(databaseMeta: DatabaseMeta) = usingDatabases.contains(databaseMeta)

    fun getSavedDatabases() = savedDatabasesUnmodifiable

    fun getUsingDatabases() = usingDatabasesUnmodifiable

    interface Observer {
        fun onDatabaseAdded(databaseMeta: DatabaseMeta) {}

        fun onDatabaseRemoved(databaseMeta: DatabaseMeta) {}

        fun onDatabaseUsing(databaseMeta: DatabaseMeta) {}

        fun onDatabaseClosing(databaseMeta: DatabaseMeta) {}
    }
}