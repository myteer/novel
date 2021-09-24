package org.myteer.novel.gui.login

import org.myteer.novel.db.NitriteDatabase

@FunctionalInterface
interface DatabaseLoginListener {
    fun onDatabaseOpened(database: NitriteDatabase)
}