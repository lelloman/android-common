@file:Suppress("unused")

package com.lelloman.common.viewmodel

import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.navigation.*
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.actionevent.AnimationViewActionEvent
import com.lelloman.common.view.actionevent.SnackEvent
import com.lelloman.common.view.actionevent.ToastEvent
import com.lelloman.common.view.actionevent.ViewActionEvent
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@Suppress("MemberVisibilityCanBePrivate", "DeprecatedCallableAddReplaceWith")
abstract class BaseViewModel(dependencies: Dependencies) : ViewModel() {

    protected val settings = dependencies.settings
    protected val ioScheduler = dependencies.ioScheduler
    protected val uiScheduler = dependencies.uiScheduler
    private val resourceProvider = dependencies.resourceProvider
    private val actionTokenProvider = dependencies.actionTokenProvider

    private val subscriptions = CompositeDisposable()
    private val themeSubscriptions = CompositeDisposable()

    private val viewActionEventsSubject: Subject<ViewActionEvent> = PublishSubject.create()
    open val viewActionEvents: Observable<ViewActionEvent> = viewActionEventsSubject.hide()

    private val themeChangedActionEventSubject = PublishSubject.create<AppTheme>()
    open val themeChangedEvents: Observable<AppTheme> = themeChangedActionEventSubject.hide()

    open fun onTokenAction(token: String) = Unit

    open fun onSetupTheme(themeSetter: (AppTheme) -> Unit) {
        val customTheme = settings
            .appTheme
            .blockingFirst()
        themeSetter(customTheme)

        themeSubscriptions.clear()
        themeSubscriptions.add(
            settings
                .appTheme
                .filter { it != customTheme }
                .subscribe(themeChangedActionEventSubject::onNext)
        )
    }

    open fun onContentPicked(uri: Uri, requestCode: Int) = Unit

    open fun onViewShown() = Unit

    open fun onViewHidden() = Unit

    open fun onSaveInstanceState(bundle: Bundle) = Unit

    open fun onRestoreInstanceState(bundle: Bundle) = Unit

    protected fun makeActionToken() = actionTokenProvider.makeActionToken()

    protected fun getString(@StringRes stringId: Int, vararg args: Any = emptyArray()) =
        resourceProvider.getString(stringId, *args)

    protected fun emitViewActionEvent(event: ViewActionEvent) = viewActionEventsSubject.onNext(event)

    protected fun navigate(navigationEvent: NavigationEvent) = emitViewActionEvent(navigationEvent)

    protected fun navigate(deepLink: DeepLink) =
        emitViewActionEvent(DeepLinkNavigationEvent(deepLink))

    protected fun navigate(navigationScreen: NavigationScreen) = navigate(DeepLink(navigationScreen))

    protected fun navigateAndClose(navigationScreen: NavigationScreen) =
        emitViewActionEvent(DeepLinkAndCloseNavigationEvent(DeepLink(navigationScreen)))

    @Deprecated(message = "Use closeScreen() instead")
    protected fun navigateBack() = emitViewActionEvent(CloseScreenViewActionEvent)

    protected fun closeScreen() = emitViewActionEvent(CloseScreenViewActionEvent)

    protected fun animate(animationViewActionEvent: AnimationViewActionEvent) {
        emitViewActionEvent(animationViewActionEvent)
    }

    open fun onCloseClicked() = closeScreen()

    protected fun shortToast(message: String) = toast(message, Toast.LENGTH_SHORT)

    protected fun longToast(message: String) = toast(message, Toast.LENGTH_LONG)

    private fun toast(message: String, duration: Int) = emitViewActionEvent(
        ToastEvent(
            message = message,
            duration = duration
        )
    )

    protected fun longSnack(
        message: String,
        actionLabel: String? = null,
        actionToken: String? = null
    ) {
        emitViewActionEvent(
            SnackEvent(
                message = message,
                actionLabel = actionLabel,
                actionToken = actionToken,
                duration = Snackbar.LENGTH_LONG
            )
        )
    }

    protected fun subscription(action: () -> Disposable) {
        subscriptions.add(action.invoke())
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
        themeSubscriptions.dispose()
    }

    class Dependencies(
        val settings: BaseApplicationSettings,
        val resourceProvider: ResourceProvider,
        val actionTokenProvider: ActionTokenProvider,
        val ioScheduler: Scheduler,
        val uiScheduler: Scheduler,
        val loggerFactory: LoggerFactory
    )
}