package com.lelloman.common.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.lelloman.common.R
import com.lelloman.common.di.qualifiers.IoScheduler
import com.lelloman.common.di.qualifiers.UiScheduler
import com.lelloman.common.logger.LoggerFactory
import com.lelloman.common.utils.StubViewDataBinding
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.viewmodel.command.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject


abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : AppCompatActivity() {

    protected abstract val viewModel: VM
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var binding: DB

    private lateinit var coordinatorLayout: CoordinatorLayout

    protected open val hasActionBar = true
    protected open val hasInverseTheme = false
    protected open val hasBaseLayout = true
    private val hasLayout get() = layoutResId != NO_LAYOUT_RES_ID
    protected open val hasActionBarBackButton = false
    protected open val hasTransparentNavigationBar = false

    private val pendingActivityResultCodes = mutableSetOf<Int>()

    private lateinit var viewModelCommandsSubscription: Disposable
    private val themeChangedEventsSubscriptions = CompositeDisposable()

    protected open val hasRootContainer = true

    protected open val hasActionbarHideBehavior = true

    protected abstract val layoutResId: Int

    @Suppress("MemberVisibilityCanBePrivate")
    protected val loggerFactory: LoggerFactory by inject()
    @Suppress("MemberVisibilityCanBePrivate")
    protected val ioScheduler: Scheduler by inject(IoScheduler)
    @Suppress("MemberVisibilityCanBePrivate")
    protected val uiScheduler: Scheduler by inject(UiScheduler)
    protected val resourceProvider: ResourceProvider by inject()
    protected val semanticTimeProvider: SemanticTimeProvider by inject()

    protected val logger by lazy { loggerFactory.getLogger(javaClass) }

    @Suppress("MemberVisibilityCanBePrivate")
    protected val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    protected val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.i("onCreate()")

        viewModel.onSetupTheme {
            theme.applyStyle(it.resId, true)
        }
        if (hasInverseTheme) {
            theme.applyStyle(R.style.InverseTheme, true)
        }
        if (hasTransparentNavigationBar) {
            theme.applyStyle(R.style.TransparentActivityWithWallpaper, true)
        }
        viewModel
            .themeChangedEvents
            .observeOn(uiScheduler)
            .subscribeOn(ioScheduler)
            .subscribe { recreate() }
            .also { themeChangedEventsSubscriptions.add(it) }

        if (hasBaseLayout && hasLayout) {
            setContentView(R.layout.activity_base)
            setupActionBar()
            coordinatorLayout = findViewById(R.id.coordinatorLayout)
            val rootContainer = if (hasRootContainer) {
                FrameLayout(this).apply {
                    val mp = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams = ViewGroup.LayoutParams(mp, mp)
                    if (hasActionBar) {
                        setPadding(0, getToolbarHeight(), 0, 0)
                    }
                    coordinatorLayout.addView(this)
                }
            } else {
                coordinatorLayout
            }
            binding = DataBindingUtil.inflate(layoutInflater, layoutResId, rootContainer, true)
        } else if (hasLayout) {
            binding = DataBindingUtil.setContentView(this, layoutResId)
        } else {
            try {
                @Suppress("UNCHECKED_CAST")
                binding = StubViewDataBinding() as DB
            } catch (exception: ClassCastException) {
                throw IllegalStateException(
                    "Generic type DB must be StubViewDataBinding if the BaseActivity has no layout.",
                    exception
                )
            }
        }
        binding.lifecycleOwner = this

        if (hasTransparentNavigationBar) {
            findViewById<View>(android.R.id.content).apply {
                setPadding(paddingLeft, statusBarHeight, paddingRight, paddingBottom)
            }
        }
        try {
            setViewModel(binding, viewModel)
        } catch (exception: Throwable) {
            throw IllegalStateException(
                "BaseActivity with no layout must have a StubViewDataBinding binding type.",
                exception
            )
        }

        viewModelCommandsSubscription = viewModel
            .commands
            .observeOn(uiScheduler)
            .subscribe {
                logger.d("Received ViewModelCommand $it")
                handleCommand(it)
            }

        viewModel.screenTitle.observe(this, Observer {
            if (it != null) title = it
        })
    }

    private fun handleCommand(command: Command) {
        when (command) {
            is CloseScreenCommand -> finish()
            is ShowToastCommand -> showToast(command)
            is ShowSnackCommand -> showSnack(command)
            is AnimationCommand -> onAnimationCommand(command)
            is SwipePageCommand -> onSwipePageCommand(command)
            is ShareFileCommand -> onShareFileCommand(command)
            is PickFileCommand -> launchPickFileIntent(command)
            is CloseKeyboardCommand -> closeKeyboard()
            is GoFullScreenCommand -> goFullScreen()
            else -> onUnhandledCommand(command)
        }
    }

    protected open fun onUnhandledCommand(command: Command) = Unit

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun goFullScreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun recreate() {
        super.recreate()
        logger.i("recreate()")
    }

    override fun onStart() {
        super.onStart()
        logger.i("onStart()")
        viewModel.onViewShown()
    }

    override fun onStop() {
        super.onStop()
        logger.i("onStop")
        viewModel.onViewHidden()
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.i("onDestroy()")
        viewModelCommandsSubscription.dispose()
        themeChangedEventsSubscriptions.dispose()
    }

    protected abstract fun setViewModel(binding: DB, viewModel: VM)

    private fun onShareFileCommand(command: ShareFileCommand) {
        val uri = FileProvider.getUriForFile(this, command.authority, command.file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "*/*"
        startActivity(intent)
    }

    private fun launchPickFileIntent(pickFileCommand: PickFileCommand) {
        val requestCode = pickFileCommand.requestCode
        if (requestCode > 0xFF) {
            logger.w("launchPickFileIntent() called with value greater that 0xFF, $requestCode")
            return
        }
        pendingActivityResultCodes.add(requestCode)

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, requestCode.or(GET_CONTENT_REQUEST_CODE_MASK))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val subCode = requestCode.and(0xFF)
        val expected = pendingActivityResultCodes.contains(subCode)
        if (!expected) {
            logger.w("Unexpected activityResult with code $requestCode")
            return
        }
        pendingActivityResultCodes.remove(subCode)
        if (requestCode.and(GET_CONTENT_REQUEST_CODE_MASK) != 0) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                uri?.let {
                    viewModel.onContentPicked(uri, subCode)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getToolbarHeight(): Int {
        val tv = TypedValue()
        return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
        } else {
            0
        }
    }

    private fun setupActionBar() {
        if (hasActionBar) {
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            if (viewModel.currentTheme.isLight) {
                toolbar.popupTheme = R.style.ThemeOverlay_AppCompat_Light
            }
            setSupportActionBar(toolbar)
            if (hasActionbarHideBehavior) {
                val params = toolbar.layoutParams as AppBarLayout.LayoutParams
                params.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }
        } else {
            findViewById<AppBarLayout>(R.id.app_bar_layout).visibility = View.GONE
        }

        if (hasActionBarBackButton) {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    protected open fun onAnimationCommand(animationCommand: AnimationCommand) = Unit

    protected open fun onSwipePageCommand(swipePageCommand: SwipePageCommand) = Unit

    override fun onSupportNavigateUp() = if (hasActionBarBackButton) {
        onBackPressed()
        true
    } else {
        super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    protected fun hideFabOnScroll(fab: FloatingActionButton, recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
            }
        })
    }

    private fun showToast(command: ShowToastCommand) {
        Toast.makeText(this, command.message, command.duration).show()
    }

    private fun showSnack(command: ShowSnackCommand) {
        val snack = Snackbar.make(coordinatorLayout, command.message, command.duration)
        if (command.hasAction) {
            snack.setAction(command.actionLabel) {
                viewModel.onTokenAction(command.actionToken!!)
            }
        }
        snack.show()
    }

    companion object {
        const val NO_LAYOUT_RES_ID = 0

        private const val GET_CONTENT_REQUEST_CODE_MASK = 0x100
    }

}