package com.lelloman.common.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.lelloman.common.R
import com.lelloman.common.navigation.NavigationEvent
import com.lelloman.common.utils.StubViewDataBinding
import com.lelloman.common.view.actionevent.*
import com.lelloman.common.viewmodel.BaseViewModel
import io.reactivex.disposables.CompositeDisposable


abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : InjectableActivity() {

    protected lateinit var viewModel: VM
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

    private val viewActionEventSubscriptions = CompositeDisposable()
    private val themeChangedEventsSubscriptions = CompositeDisposable()

    protected abstract val layoutResId: Int

    private val logger by lazy { loggerFactory.getLogger(javaClass) }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.i("onCreate()")

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
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
            coordinatorLayout = findViewById(R.id.coordinator_layout)
            binding = DataBindingUtil.inflate(layoutInflater, layoutResId, coordinatorLayout, true)
        } else if (hasLayout) {
            binding = DataBindingUtil.setContentView(this, layoutResId)
        } else {
            binding = StubViewDataBinding() as DB
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

        viewModel
            .viewActionEvents
            .observeOn(uiScheduler)
            .subscribe {
                logger.d("Received ViewActionEvent $it")
                when (it) {
                    is NavigationEvent -> navigationRouter.onNavigationEvent(this, it)
                    is ToastEvent -> showToast(it)
                    is SnackEvent -> showSnack(it)
                    is AnimationViewActionEvent -> onAnimationViewActionEvent(it)
                    is SwipePageActionEvent -> onSwipePageActionEvent(it)
                    is ShareFileViewActionEvent -> onShareFileViewActionEvent(it)
                    is PickFileActionEvent -> launchPickFileIntent(it)
                }
            }
            .also { viewActionEventSubscriptions.add(it) }
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
        viewActionEventSubscriptions.clear()
        themeChangedEventsSubscriptions.dispose()
    }

    protected abstract fun setViewModel(binding: DB, viewModel: VM)

    private fun onShareFileViewActionEvent(event: ShareFileViewActionEvent) {
        val uri = FileProvider.getUriForFile(this, event.authority, event.file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "*/*"
        startActivity(intent)
    }

    private fun launchPickFileIntent(pickFileActionEvent: PickFileActionEvent) {
        val requestCode = pickFileActionEvent.requestCode
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

    private fun setupActionBar() {
        if (hasActionBar) {
            setSupportActionBar(findViewById(R.id.toolbar))
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

    protected open fun onAnimationViewActionEvent(animationViewActionEvent: AnimationViewActionEvent) {

    }

    protected open fun onSwipePageActionEvent(swipePageActionEvent: SwipePageActionEvent) {

    }

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

    private fun showToast(event: ToastEvent) {
        Toast.makeText(this, event.message, event.duration).show()
    }

    private fun showSnack(event: SnackEvent) {
        val snack = Snackbar.make(coordinatorLayout, event.message, event.duration)
        if (event.hasAction) {
            snack.setAction(event.actionLabel) {
                viewModel.onTokenAction(event.actionToken!!)
            }
        }
        snack.show()
    }

    protected abstract fun getViewModelClass(): Class<VM>

    companion object {
        const val NO_LAYOUT_RES_ID = 0

        private const val GET_CONTENT_REQUEST_CODE_MASK = 0x100
    }

}