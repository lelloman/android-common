package com.lelloman.common

import com.lelloman.common.navigation.NavigationEvent
import com.lelloman.common.settings.BaseApplicationSettings
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.AppTheme
import com.lelloman.common.view.ResourceProvider
import com.lelloman.common.view.actionevent.AnimationViewActionEvent
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.instrumentedtestutils.wait
import com.lelloman.instrumentedtestutils.whenever
import io.reactivex.subjects.BehaviorSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.concurrent.TimeoutException

class BaseViewModelTest {

    private val appThemeSubject = BehaviorSubject.createDefault(DEFAULT_THEME)
    private val settings = mock(BaseApplicationSettings::class.java).apply {
        whenever(this.appTheme).thenReturn(appThemeSubject.hide())
    }
    private val resourceProvider = mock(ResourceProvider::class.java)
    private val actionTokenProvider = ActionTokenProvider()

    private val dependencies = BaseViewModel.Dependencies(
        settings = settings,
        resourceProvider = resourceProvider,
        actionTokenProvider = actionTokenProvider
    )

    private val tested = BaseViewModelImpl(dependencies)

    @Test
    fun emitsManyViewActionEvents() {
        val nEvents = 10000

        val tester = tested.viewActionEvents.test()

        for (i in 0 until nEvents) {
            tested.callNavigate(object : NavigationEvent {})
        }

        tester.assertValueCount(nEvents)
        for (i in 0 until nEvents) {
            tester.assertValueAt(i) { it is NavigationEvent }
        }
    }

    @Test
    fun emitsThemeChangedEvents() {
        val emittedEvents = mutableListOf<AppTheme>()
        fun List<AppTheme>.awaitCount(count: Int) {
            for (i in 0 until 20) {
                if (size == count) {
                    return
                }
                wait(0.1)
            }
            throw TimeoutException("Timeout while waiting for list to be size $count.")
        }

        tested.themeChangedEvents.observeForever { emittedEvents.add(it!!) }
        tested.onSetupTheme { }

        appThemeSubject.onNext(NON_DEFAULT_THEME1)
        emittedEvents.awaitCount(1)

        assertThat(emittedEvents).hasSize(1)
        assertThat(emittedEvents[0]).isEqualTo(NON_DEFAULT_THEME1)

        appThemeSubject.onNext(NON_DEFAULT_THEME2)
        emittedEvents.awaitCount(2)

        assertThat(emittedEvents).hasSize(2)
        assertThat(emittedEvents[1]).isEqualTo(NON_DEFAULT_THEME2)
    }

    @Test
    fun emitsAnimationViewActionEvents() {
        val nEvents = 1000

        val tester = tested.viewActionEvents.test()

        for (i in 0 until nEvents) {
            tested.callAnimate(object : AnimationViewActionEvent {})
        }

        tester.assertValueCount(nEvents)
        for (i in 0 until nEvents) {
            tester.assertValueAt(i) { it is AnimationViewActionEvent }
        }
    }

    private companion object {
        val DEFAULT_THEME = AppTheme.LIGHT
        val NON_DEFAULT_THEME1 = AppTheme.DARCULA
        val NON_DEFAULT_THEME2 = AppTheme.MOCKITO
    }

    private class BaseViewModelImpl(dependencies: BaseViewModel.Dependencies) : BaseViewModel(dependencies) {
        fun callNavigate(navigationEvent: NavigationEvent) = super.navigate(navigationEvent)
        fun callAnimate(event: AnimationViewActionEvent) = super.animate(event)
    }
}