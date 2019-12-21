@file:Suppress("unused")

package com.lelloman.common.viewmodel

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.navigation.*
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.viewmodel.command.AnimationCommand
import com.lelloman.common.viewmodel.command.Command
import com.lelloman.common.viewmodel.command.ShowSnackCommand
import com.lelloman.common.viewmodel.command.ShowToastCommand
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

    private val commandsSubject: Subject<Command> = PublishSubject.create()
    open val commands: Observable<Command> = commandsSubject.hide()

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

    protected fun emitCommand(command: Command) = commandsSubject.onNext(command)

    protected fun navigate(navigationEvent: NavigationEvent) = emitCommand(navigationEvent)

    protected fun navigate(deepLink: DeepLink) =
        emitCommand(DeepLinkNavigationEvent(deepLink))

    protected fun navigate(navigationScreen: NavigationScreen) =
        navigate(DeepLink(navigationScreen))

    protected fun navigateAndClose(navigationScreen: NavigationScreen) =
        emitCommand(DeepLinkAndCloseNavigationEvent(DeepLink(navigationScreen)))

    @Deprecated(message = "Use closeScreen() instead")
    protected fun navigateBack() = emitCommand(CloseScreenCommand)

    protected fun closeScreen() = emitCommand(CloseScreenCommand)

    protected fun animate(animationCommand: AnimationCommand) = emitCommand(animationCommand)

    open fun onCloseClicked() = closeScreen()

    protected fun shortToast(message: String) = toast(message, Toast.LENGTH_SHORT)

    protected fun longToast(message: String) = toast(message, Toast.LENGTH_LONG)

    private fun toast(message: String, duration: Int) = emitCommand(
        ShowToastCommand(
            message = message,
            duration = duration
        )
    )

    protected fun longSnack(
        message: String,
        actionLabel: String? = null,
        actionToken: String? = null
    ) {
        emitCommand(
            ShowSnackCommand(
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