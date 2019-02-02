@file:Suppress("unused")

package com.lelloman.common.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.widget.Toast
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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@Suppress("MemberVisibilityCanBePrivate", "DeprecatedCallableAddReplaceWith")
abstract class BaseViewModel(dependencies: Dependencies) : ViewModel() {

    private val settings = dependencies.settings
    private val resourceProvider = dependencies.resourceProvider
    private val actionTokenProvider = dependencies.actionTokenProvider

    private val subscriptions = CompositeDisposable()

    private val viewActionEventsSubject: Subject<ViewActionEvent> = PublishSubject.create()
    open val viewActionEvents: Observable<ViewActionEvent> = viewActionEventsSubject.hide()

    private val mutableThemeChangedActionEvent = MutableLiveData<AppTheme>()
    open val themeChangedEvents: LiveData<AppTheme> = mutableThemeChangedActionEvent

    open fun onTokenAction(token: String) = Unit

    open fun onSetupTheme(themeSetter: (AppTheme) -> Unit) {
        val customTheme = settings
            .appTheme
            .blockingFirst()
        themeSetter(customTheme)

        subscription {
            settings
                .appTheme
                .filter { it != customTheme }
                .subscribe(mutableThemeChangedActionEvent::postValue)
        }
    }

    open fun onContentPicked(uri: Uri, requestCode: Int) = Unit

    @Deprecated(message = "To be removed without any replacement.")
    open fun onCreate() = Unit

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

    protected fun navigateBack() = navigate(CloseScreenNavigationEvent)

    protected fun animate(animationViewActionEvent: AnimationViewActionEvent) {
        emitViewActionEvent(animationViewActionEvent)
    }

    open fun onCloseClicked() = navigateBack()

    protected fun shortToast(message: String) =
        emitViewActionEvent(
            ToastEvent(
                message = message,
                duration = Toast.LENGTH_SHORT
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
    }

    class Dependencies(
        val settings: BaseApplicationSettings,
        val resourceProvider: ResourceProvider,
        val actionTokenProvider: ActionTokenProvider
    )
}