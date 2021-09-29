package org.myteer.novel.gui.theme

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import javafx.scene.Scene
import org.myteer.novel.utils.IdentifiableWeakReference
import java.lang.ref.WeakReference
import java.util.*

abstract class Theme {
    companion object {
        private val THEMEABLE_SET: MutableSet<WeakReference<Themeable>> = Collections.synchronizedSet(mutableSetOf())
        private val REGISTERED_THEMES: MutableMap<Class<out Theme>, ThemeMeta<out Theme>> = Collections.synchronizedMap(mutableMapOf())
        private val defaultTheme: ObjectProperty<Theme> = SimpleObjectProperty()

        init {
            REGISTERED_THEMES[LightTheme::class.java] = LightTheme.getMeta()
            REGISTERED_THEMES[DarkTheme::class.java] = DarkTheme.getMeta()
            REGISTERED_THEMES[OsSynchronizedTheme::class.java] = OsSynchronizedTheme.getMeta()
        }

        fun getAvailableThemes(): Set<Class<out Theme>> = REGISTERED_THEMES.keys

        fun getAvailableThemesData(): Collection<ThemeMeta<out Theme>> = REGISTERED_THEMES.values

        fun registerThemeable(themeable: Themeable) {
            if (THEMEABLE_SET.add(IdentifiableWeakReference(themeable))) {
                themeable.handleThemeApply(getDefault())
            }
        }

        private fun notifyThemeableInstances(newTheme: Theme, oldTheme: Theme) {
            val iterator = THEMEABLE_SET.iterator()
            while (iterator.hasNext()) {
                val weakReference = iterator.next()
                val themeable = weakReference.get()
                if (null == themeable) {
                    iterator.remove()
                } else {
                    themeable.handleThemeApply(newTheme, oldTheme)
                }
            }
        }

        fun setDefault(theme: Theme) {
            val current = defaultTheme.get()
            if (null == current) {
                notifyThemeableInstances(theme, empty())
                defaultTheme.set(theme)
            } else {
                if (current.javaClass != theme.javaClass) {
                    current.onThemeDropped()
                    notifyThemeableInstances(theme, current)
                    defaultTheme.set(theme)
                }
            }
        }

        fun getDefault(): Theme {
            if (null == defaultTheme.get()) {
                defaultTheme.set(DefaultThemeFactory.get())
            }
            return defaultTheme.get()
        }

        fun empty(): Theme = EmptyTheme()
    }

    protected fun update(oldTheme: Theme = this) {
        if (getDefault() == this) {
            notifyThemeableInstances(this, oldTheme)
        }
    }

    protected open fun onThemeDropped() { }

    abstract fun revoke(scene: Scene)

    abstract fun revoke(parent: Parent)

    abstract fun apply(scene: Scene)

    abstract fun apply(parent: Parent)

    private class EmptyTheme : Theme() {
        override fun revoke(scene: Scene) {
        }

        override fun revoke(parent: Parent) {
        }

        override fun apply(scene: Scene) {
        }

        override fun apply(parent: Parent) {
        }
    }
}