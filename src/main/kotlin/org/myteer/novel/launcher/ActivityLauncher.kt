package org.myteer.novel.launcher

import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.entry.EntryActivity
import org.myteer.novel.gui.login.DatabaseLoginListener
import org.myteer.novel.gui.login.LoginActivity
import org.myteer.novel.gui.login.quick.QuickLoginActivity
import org.myteer.novel.gui.main.MainActivity
import org.myteer.novel.gui.utils.runInsideUIAsync
import org.myteer.novel.i18n.i18n
import org.myteer.novel.main.ArgumentTransformer
import org.slf4j.LoggerFactory
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

open class ActivityLauncher(
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker,
    private val mode: LauncherMode = LauncherMode.INIT,
    params: List<String> = listOf(),
    databaseMeta: DatabaseMeta? = null,
    private var postLaunchQueue: PostLaunchQueue? = null
) : Runnable {
    private val databaseMeta: DatabaseMeta?

    init {
        this.databaseMeta = databaseMeta ?: ArgumentTransformer.transform(params)
    }

    override fun run() {
        launch()
    }

    private fun launch() {
        logger.debug("$mode mode detected")
        if (null != databaseMeta) {
            logger.debug("argument found")
            handleArgument(mode, databaseMeta)
        } else {
            logger.debug("no argument found")
            handleNoArgument(mode)
        }
    }

    private fun handleArgument(mode: LauncherMode, databaseMeta: DatabaseMeta) {
        when (mode) {
            LauncherMode.INIT -> handleArgumentInit(databaseMeta)
            LauncherMode.ALREADY_RUNNING -> handleArgumentAlreadyRunning(databaseMeta)
            LauncherMode.INTERNAL -> handleArgumentInternal(databaseMeta)
        }
    }

    private fun handleArgumentInit(databaseMeta: DatabaseMeta) {
        onNewDatabaseAdded(databaseMeta)
        logger.debug("trying to sign in into the argument-database database...")
        val database = NitriteDatabase.builder()
            .databaseMeta(databaseMeta)
            .onFailed { message, t ->
                runInsideUIAsync {
                    getLoginData().setSelectedDatabase(databaseMeta)
                    val entryActivity = showEntryActivity()
                    entryActivity.getContext().showErrorDialog(i18n("login.failed"), message, t as Exception?)
                    onActivityLaunched(entryActivity.getContext(), null)
                }
            }
            .build()
        database?.let {
            logger.debug("signed in into the argument-database successfully, launching a MainActivity...")
            runInsideUIAsync {
                val mainActivity = showMainActivity(database)
                onActivityLaunched(mainActivity.getContext(), databaseMeta)
            }
        }
    }

    private fun handleArgumentAlreadyRunning(databaseMeta: DatabaseMeta) {
        MainActivity.getByDatabase(databaseMeta).map(MainActivity::getContext).ifPresentOrElse({
             runInsideUIAsync { it.toFrontRequest() }
        }, {
            handleArgumentInit(databaseMeta)
        })
    }

    private fun handleArgumentInternal(databaseMeta: DatabaseMeta) {
        MainActivity.getByDatabase(databaseMeta).map(MainActivity::getContext).ifPresentOrElse({
            logger.debug("Found gui-context for database: '${databaseMeta.file}'")
            runInsideUIAsync { it.toFrontRequest() }
        }, {
            logger.debug("Didn't found gui-context for database: '${databaseMeta.file}'")
            onNewDatabaseAdded(databaseMeta)
            val databaseLoginListener = object : DatabaseLoginListener {
                override fun onDatabaseOpened(database: NitriteDatabase) {
                    val mainActivity = showMainActivity(database)
                    onActivityLaunched(mainActivity.getContext(), databaseMeta)
                }
            }
            val database = NitriteDatabase.builder()
                .databaseMeta(databaseMeta)
                .onFailed { _, _ ->
                    runInsideUIAsync {
                        val quickLoginActivity = showQuickLoginActivity(databaseMeta, databaseLoginListener)
                        onActivityLaunched(quickLoginActivity.getContext(), databaseMeta)
                    }
                }
                .build()
            database?.let {
                logger.debug("signed in into the argument-database successfully, launching a MainActivity...")
                runInsideUIAsync {
                    databaseLoginListener.onDatabaseOpened(it)
                }
            }
        })
    }

    private fun handleNoArgument(mode: LauncherMode) {
        when (mode) {
            LauncherMode.INIT -> handleNoArgumentInit()
            LauncherMode.ALREADY_RUNNING -> handleNoArgumentAlreadyRunning()
            LauncherMode.INTERNAL -> handleNoArgumentInternal()
        }
    }

    private fun handleNoArgumentInit() {
        if (getLoginData().isAutoLogin()) {
            logger.debug("auto login is turned on, trying to sign in into the auto-login database...")
            autoLogin()
        } else {
            logger.debug("auto login is turned off, launching a EntryActivity...")
            runInsideUIAsync {
                val entryActivity = showEntryActivity()
                onActivityLaunched(entryActivity.getContext(), null)
            }
        }
    }

    private fun handleNoArgumentAlreadyRunning() {
        logger.debug("no argument found, focusing on a random window...")
        runInsideUIAsync {
            EntryActivity.getActiveEntryActivities()
                .stream()
                .findAny()
                .map(EntryActivity::getContext)
                .ifPresent(Context::toFrontRequest)
        }
    }

    private fun handleNoArgumentInternal() {
        runInsideUIAsync {
            LoginActivity.getActiveLoginActivities()
                .stream()
                .findAny()
                .map(LoginActivity::getContext)
                .ifPresentOrElse({
                    it.toFrontRequest()
                }, {
                    val entryActivity = showEntryActivity()
                    onActivityLaunched(entryActivity.getContext(), null)
                })
        }
    }

    private fun autoLogin() {
        val loginData = getLoginData()
        val database = NitriteDatabase.builder()
            .databaseMeta(loginData.getAutoLoginDatabase()!!)
            .onFailed { message, t ->
                runInsideUIAsync {
                    val entryActivity = showEntryActivity()
                    entryActivity.getContext().showErrorDialog(i18n("login.failed"), message, t as Exception?)
                    onActivityLaunched(entryActivity.getContext(), null)
                }
            }
            .build(loginData.getAutoLoginCredentials())
        database?.let {
            logger.debug("signed in into the auto-login database successfully, launching a MainActivity...")
            runInsideUIAsync {
                val mainActivity = showMainActivity(it)
                onActivityLaunched(mainActivity.getContext(), it.meta)
            }
        }
    }

    private fun showEntryActivity(): EntryActivity {
        return EntryActivity(preferences, databaseTracker, getLoginData()).also {
            it.show()
        }
    }

    private fun showMainActivity(database: NitriteDatabase): MainActivity {
        return MainActivity(database, preferences, databaseTracker).also {
            it.show()
        }
    }

    private fun showQuickLoginActivity(databaseMeta: DatabaseMeta, databaseLoginListener: DatabaseLoginListener): QuickLoginActivity {
        return QuickLoginActivity(databaseMeta, databaseLoginListener).also {
            it.show()
        }
    }

    private fun onNewDatabaseAdded(databaseMeta: DatabaseMeta) {
        val loginData = getLoginData()
        if (loginData.addSavedDatabase(databaseMeta)) {
            databaseTracker.saveDatabase(databaseMeta)
            saveLoginData(loginData)
            logger.debug("The launched database is added to LoginData")
        } else {
            logger.debug("The launched database is already in the LoginData")
        }
    }

    protected open fun getLoginData(): LoginData = preferences.get(PreferenceKey.LOGIN_DATA)

    protected open fun saveLoginData(loginData: LoginData) {
        preferences.editor().put(PreferenceKey.LOGIN_DATA, loginData)
    }

    private fun onActivityLaunched(context: Context, launchedDatabase: DatabaseMeta?) {
        onActivityLaunched(context)
        postLaunchQueue?.forEach {
            it.accept(context, launchedDatabase)
        }
    }

    protected open fun onActivityLaunched(context: Context) { }

    class PostLaunchQueue {
        private val items: Queue<BiConsumer<Context, DatabaseMeta?>> = LinkedList()

        fun pushItem(item: BiConsumer<Context, DatabaseMeta?>) {
            items.add(item)
        }

        fun removeItem(item: BiConsumer<Context, DatabaseMeta?>) {
            items.remove(item)
        }

        fun forEach(action: Consumer<BiConsumer<Context, DatabaseMeta?>>) {
            while (items.isNotEmpty()) {
                action.accept(items.poll())
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ActivityLauncher::class.java)
    }
}