@file:Suppress("unused")

package com.lelloman.common.viewmodel

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.lelloman.common.data.ResourceProvider
import com.lelloman.common.data.TimeProvider
import com.lelloman.common.data.settings.BaseApplicationSettings
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.utils.StringLiveData
import com.lelloman.common.utils.immutable
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.command.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@Suppress("MemberVisibilityCanBePrivate", "DeprecatedCallableAddReplaceWith")
abstract class BaseViewModel(dependencies: Dependencies) :
    ViewModel(),
    ResourceProvider by dependencies.resourceProvider {

    protected val settings = dependencies.settings
    protected val ioScheduler = dependencies.ioScheduler
    protected val uiScheduler = dependencies.uiScheduler
    private val resourceProvider = dependencies.resourceProvider
    private val actionTokenProvider = dependencies.actionTokenProvider

    private val subscriptions = CompositeDisposable()
    private val themeSubscriptions = CompositeDisposable()

    private val commandsSubject: Subject<Command> = PublishSubject.create()
    open val commands: Observable<Command> = commandsSubject.hide()

    private val mutableScreenTitle = StringLiveData()
    val screenTitle = mutableScreenTitle.immutable

    private val themeChangedActionEventSubject = PublishSubject.create<AppTheme>()
    open val themeChangedEvents: Observable<AppTheme> = themeChangedActionEventSubject.hide()

    private val throttledActions = mutableMapOf<String, Long>()
    private val actionCoolDownMs = dependencies.actionCoolDownMs

    private val timeProvider = dependencies.timeProvider

    protected val logger = dependencies.loggerFactory.getLogger(javaClass)

    val currentTheme: AppTheme
        get() = settings
            .appTheme
            .blockingFirst()

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

    protected fun emitCommand(command: Command) = commandsSubject.onNext(command)

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

    protected fun throttledAction(actionId: String = GENERAL_ACTION_ID, action: () -> Unit) {
        val lastExecuted = throttledActions[actionId]
        val now = timeProvider.nowUtcMs()
        throttledActions
            .filter { now - it.value >= actionCoolDownMs }
            .forEach { throttledActions.remove(it.key) }

        val shouldExecute = lastExecuted == null || now - lastExecuted >= actionCoolDownMs
        if (shouldExecute) {
            throttledActions[actionId] = timeProvider.nowUtcMs()
            action()
        }
        logger.i("throttled action <$actionId> should execute $shouldExecute")
    }

    protected fun setScreenTitle(title: String) {
        mutableScreenTitle.value = title
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
        themeSubscriptions.dispose()
    }

    protected fun <T> Single<T>.viewModelSubscription(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(onSuccess, onError)
        }
    }

    protected fun <T> Single<T>.viewModelSubscription(onSuccess: (T) -> Unit) {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(onSuccess)
        }
    }

    protected fun Completable.viewModelSubscription(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(onSuccess, onError)
        }
    }

    protected fun Completable.viewModelSubscription(onSuccess: () -> Unit) {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(onSuccess)
        }
    }

    protected fun Completable.viewModelSubscription() {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe()
        }
    }

    protected fun <T> Observable<T>.viewModelSubscription(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(onSuccess, onError)
        }
    }

    protected fun <T> Observable<T>.viewModelSubscription(onSuccess: (T) -> Unit) {
        subscription {
            subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(onSuccess)
        }
    }

    class Dependencies(
        val settings: BaseApplicationSettings,
        val resourceProvider: ResourceProvider,
        val actionTokenProvider: ActionTokenProvider,
        val ioScheduler: Scheduler,
        val uiScheduler: Scheduler,
        val loggerFactory: LoggerFactory,
        val timeProvider: TimeProvider,
        val actionCoolDownMs: Long
    )

    companion object {
        private const val GENERAL_ACTION_ID = "GeneralAction"
    }
}