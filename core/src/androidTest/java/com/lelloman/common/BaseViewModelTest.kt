package com.lelloman.common

import com.lelloman.common.androidtestutils.whenever
import com.lelloman.common.data.ResourceProvider
import com.lelloman.common.data.settings.BaseApplicationSettings
import com.lelloman.common.logger.LoggerFactoryImpl
import com.lelloman.common.utils.ActionTokenProvider
import com.lelloman.common.view.AppTheme
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.viewmodel.command.AnimationCommand
import com.lelloman.common.viewmodel.command.Command
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test
import org.mockito.Mockito.mock

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
        actionTokenProvider = actionTokenProvider,
        ioScheduler = trampoline(),
        uiScheduler = trampoline(),
        loggerFactory = LoggerFactoryImpl(),
        timeProvider = mock(),
        actionCoolDownMs = 0L
    )

    private val tested = BaseViewModelImpl(dependencies)

    @Test
    fun emitsManyCommands() {
        val nEvents = 10000

        val tester = tested.commands.test()

        for (i in 0 until nEvents) {
            tested.callEmitCommand(object : Command {})
        }

        tester.assertValueCount(nEvents)
        for (i in 0 until nEvents) {
            tester.assertValueAt(i) { it is Command }
        }
    }

    @Test
    fun emitsThemeChangedEvents() {
        val tester = tested.themeChangedEvents.test()
        tested.onSetupTheme { }

        appThemeSubject.onNext(NON_DEFAULT_THEME1)
        tester.awaitCount(1)

        tester.assertValueCount(1)
        tester.assertValueAt(0) { it == NON_DEFAULT_THEME1 }

        appThemeSubject.onNext(NON_DEFAULT_THEME2)
        tester.awaitCount(2)

        tester.assertValueCount(2)
        tester.assertValueAt(1) { it == NON_DEFAULT_THEME2 }
    }

    @Test
    fun emitsAnimationCommands() {
        val nEvents = 1000

        val tester = tested.commands.test()

        for (i in 0 until nEvents) {
            tested.callAnimate(object :
                AnimationCommand {})
        }

        tester.assertValueCount(nEvents)
        for (i in 0 until nEvents) {
            tester.assertValueAt(i) { it is AnimationCommand }
        }
    }

    private companion object {
        val DEFAULT_THEME = AppTheme("Light", 1, true)
        val NON_DEFAULT_THEME1 = AppTheme("Darcula", 2, false)
        val NON_DEFAULT_THEME2 = AppTheme("Mockito", 3, true)
    }

    private class BaseViewModelImpl(dependencies: BaseViewModel.Dependencies) : BaseViewModel(dependencies) {
        fun callEmitCommand(command: Command) = super.emitCommand(command)
        fun callAnimate(command: AnimationCommand) = super.animate(command)
    }
}