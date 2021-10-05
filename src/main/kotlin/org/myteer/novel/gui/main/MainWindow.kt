package org.myteer.novel.gui.main

import org.myteer.novel.gui.menubar.AppMenuBar
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.main.PropertiesSetup

class MainWindow(view: MainView, menuBar: AppMenuBar) :
    BaseWindow<MainView>("${System.getProperty(PropertiesSetup.APP_NAME)} - ${view.databaseMeta}", menuBar, view) {
    init {
        minWidth = 530.0
        minHeight = 530.0
        isMaximized = true
        showExitDialog = true
    }
}